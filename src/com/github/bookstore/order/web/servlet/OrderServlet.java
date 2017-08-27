package com.github.bookstore.order.web.servlet;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bookstore.cart.domain.Cart;
import com.github.bookstore.cart.domain.CartItem;
import com.github.bookstore.order.domain.Order;
import com.github.bookstore.order.domain.OrderItem;
import com.github.bookstore.order.service.OrderException;
import com.github.bookstore.order.service.OrderService;
import com.github.bookstore.user.domain.User;
import com.github.commonutils.CommonUtils;
import com.github.web.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	/**
	 * 支付去银行
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String  zhiFu(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	 /**
	  * 准备13参数
	  */
		//加载配置文件
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream(""));
		
		String p0 = "Buy";
		String p1 = props.getProperty("p1_MerId");
		String p2 = req.getParameter("oid");
		String p3 = "0.01";
		String p4 = "CNY";
		String p5 = "";
		String p6 = "";
		String p7 = "";
		String p8 = props.getProperty("p8_Url");
		String p9 = "";
		String pa = "";
		String pd = req.getParameter("pd_FrpId");
		String pr = "1";
		
		/**
		 * 计算hmac
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0, p1, p2, p3, p4,
				p5, p6, p7, p8, p9, pa, pd, 
				pr, keyValue);
		/**
		 * 连接易宝的支付网关
		 */
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0);
		url.append("&p1_MerId=").append(p1);
		url.append("&p2_Order=").append(p2);
		url.append("&p3_Amt=").append(p3);
		url.append("&p4_Cur=").append(p4);
		url.append("&p5_Pid=").append(p5);
		url.append("&p6_Pcat=").append(p6);
		url.append("&p7_Pdesc=").append(p7);
		url.append("&p8_Url=").append(p8);
		url.append("&p9_SAF=").append(p9);
		url.append("&pa_MP=").append(pa);
		url.append("&pd_FrpId=").append(pd);
		url.append("&pr_NeedResponse=").append(pr);
		url.append("&hmac=").append(hmac);
		
		System.out.println(url.toString());
		/**
		 * 重定向到易宝
		 */
		resp.sendRedirect(url.toString());
		return null;
	}
	/**
	 * 易宝回调方法
	 * 必须判断调用本方法的是不是易宝！
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String  back(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.获取11+1参数	
		 */
//		Map params = req.getParameterMap();
		String p1_MerId =req.getParameter("p1_MerId");
		String r0_Cmd =req.getParameter("r0_Cmd");
		String r1_Code =req.getParameter("r1_Code");
		String r2_TrxId =req.getParameter("p2_TrxId");
		String r3_Amt =req.getParameter("r3_Amt");
		String r4_Cur =req.getParameter("r4_Cur");
		String r5_Pid =req.getParameter("r5_Pid");
		String r6_Order =req.getParameter("r6_Order");
		String r7_Uid =req.getParameter("r7_Uid");
		String r8_MP =req.getParameter("r8_Mp");
		String r9_BType =req.getParameter("r9_BType");
		
		String hmac = req.getParameter("hmac");
		/**
		 * 2.校验访问者是否为易宝
		 */
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		String keyValue = props.getProperty("keyValue");
		
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
				r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		if(!bool){//如果校验失败
			req.setAttribute("msg", "您不是什么好东西！");
			return "f:/jsps/msg.jsp";
		}
		
		/**
		 * 3.获取订单状态，确定是否要修改订单状态，以及添加积分等业务操作
		 */
		orderService.zhiFu(r6_Order);
		/**
		 * 4.判断当前回调方式
		 *	如果为点对点，不要回馈success开头的字符串
		 */
		if(r9_BType.equals("2")){
			resp.getWriter().print("success");
		}
		
		/**
		 * 保存成功信息，转发到msg.jsp
		 */
		req.setAttribute("msg", "支付成功！等待卖家发货！");
		return "f:/jsps/msg.jsp";
	}
	/**
	 * 添加订单
	 * 把session中的车用来生成Order对象
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String  add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	/**
	 * 1.从session中得到cart
	 * 2.使用cart生成order对象
	 * 3.调用service方法完成添加订单
	 * 4.保存order到request域中,转发到/jsps/order/desc.jsp
	 */
		//从session中获取cart!
		Cart cart =(Cart) req.getSession().getAttribute("cart");
		//把cart转换成order对象
		/**
		 * 创建Order对象，并设置属性！
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid());//设置订单编号
		order.setOrdertime(new Date());//设置下单时间
		order.setState(1);//设置订单状态为1，表示未付款
		User user = (User) req.getSession().getAttribute("session_user");
		order.setOwner(user);
		order.setTotal(cart.getTotal());//设置订单的合计，从车中获取合计
		
		/**
		 * 创建订单条目集合
		 */
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		//循环遍历cart中的cartItem,使用每一个cartItem对象创建OrderItem对象，并添加到集合中
		for(CartItem cartItem : cart.getCartItems()){
			OrderItem oi= new OrderItem();//创建订单条目
			
			oi.setIid(CommonUtils.uuid());//设置订单条目的id
			oi.setCount(cartItem.getCount());//设置条目的数量
			oi.setBook(cartItem.getBook());//设置条目图书
			oi.setSubtotal(cartItem.getSubtotal());//设置小计
			oi.setOrder(order);//设置所属订单
			
		
			orderItemList.add(oi);//添加订单条目到集合中
		}
	//把所有的订单条目添加到订单中
		order.setOrderItemList(orderItemList);
		//清空购物车
		cart.clear();
		
	//////////////////////////////////////////////////////////////
		/**
		 * 3.调用service方法添加订单
		 */
	orderService.addOrder(order);
	
	/**
	 * 4.保存order到request域中，转发到/jsps/order/desc.jsp
	 */
	req.setAttribute("order", order);
	return "f:/jsps/order/desc.jsp";
	}
	
	/**
	 * 我的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.从session中得到当前用户，从而得到uid
		 * 2.使用uid调用orderService#myOrders(uid)得到该用户的所有订单List<Order>
		 * 3.把订单列表保存到request域中，转发到/jsps/order/list.jsp
		 */
		User user = (User) req.getSession().getAttribute("session_user");
		List<Order> orderList=orderService.myOrders(user.getUid());
		req.setAttribute("orderList", orderList);
		return "f:/jsps/order/list.jsp";
	}
	
	/**
	 * 加载订单 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.得到oid
		 * 2.使用Oid通过service获得order
		 * 3.保存到request域中，转发到/jsps/order/desc.jsp
		 */
		req.setAttribute("order", orderService.load(req.getParameter("oid")));
		
		return "f:/jsps/order/pay.jsp";
	}
	
	/**
	 * 确认收货
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.获取oid参数
		 * 2.调用service方法
		 *	>如果有异常，保存异常信息，转发到msg.jsp 
		 * 3.保存成功信息，转发到msg.jsp
		 */
			String oid = req.getParameter("oid");
			try {
				orderService.confirm(oid);
				req.setAttribute("msg", "恭喜，交易完成！");
			} catch (OrderException e) {
				req.setAttribute("msg", e.getMessage());
			}
			return "f:/jsps/msg.jsp";
		
	}
}
