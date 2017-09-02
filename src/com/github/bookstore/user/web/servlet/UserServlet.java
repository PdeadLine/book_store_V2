package com.github.bookstore.user.web.servlet;



import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bookstore.cart.domain.Cart;
import com.github.bookstore.user.domain.User;
import com.github.bookstore.user.service.UserException;
import com.github.bookstore.user.service.UserService;
import com.github.commonutils.CommonUtils;
import com.github.mail.Mail;
import com.github.web.servlet.BaseServlet;

/**
 * User表述层
 * @author Administrator
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	

		public String login(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			/**
			 * 封装表单
			 * 输入校验
			 * 调用service#login()完成登陆
			 * >保存错信息、form到request中，转发到"/login.jsp"
			 * 保存用户信息到session中， 然后重定向到index.jsp 
			 */
			User form = CommonUtils.toBean(req.getParameterMap(), User.class);
						
			try {
				User user=userService.login(form);
				req.getSession().setAttribute("session_user", user);
				/**
				 * 给用户添加一辆购物车！即向session中保存一个cart对象。
				 */
				req.getSession().setAttribute("cart", new Cart());
				
				return "r:/index.jsp";
			} catch (UserException e) {
				req.setAttribute("msg", e.getMessage());
				req.setAttribute("form",form);
				return "f:/jsps/user/login.jsp";
			}
		
		}
			
	
	
	
	/**
	 * 激活功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String active(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String code = req.getParameter("code");
		
		try {
			userService.active(code);
			req.setAttribute("msg", "恭喜，您已激活成功！请马上登陆！");
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());
		}
		return "f:/client/msg.jsp";
	}
	
	
	/**
	 * 注册功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//封装表单数据
		User form = CommonUtils.toBean(req.getParameterMap(), User.class);
		System.out.println(req.getParameter("password"));
		
		//补全
		form.setUid(CommonUtils.uuid().trim());
		form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		/**
		 * 输入校验
		 */
		Map<String,String> errors = new HashMap<String,String>();
		String username =form.getUsername();
		if(username ==null || username.trim().isEmpty()){
			errors.put("username", "用户名不能为空");//非空校验
		}else if(username.length()<3 ||username.length()>10){
			errors.put("username", "用户名长度必须在3~10之间");//长度校验
		}
		
		
		String password =form.getPassword();
		if(password ==null || password.trim().isEmpty()){
			errors.put("password", "密码不能为空！");//非空校验
		}else if(password.length()<3 ||password.length()>10){
			errors.put("password", "密码长度必须在3~10之间！");//长度校验
		}
		
		String email =form.getEmail();
		if(email ==null || email.trim().isEmpty()){
			errors.put("email", "邮箱不能为空！");//非空校验
		}else if(!email.matches("\\w+@\\w+\\.\\w+")){//逻辑错，导致邮件没法出去， 格式合法不抛异常
			errors.put("email", "email格式错误！");//长度校验
		}
		/**
		 * 判断是否存在错误信息
		 */
		if(errors.size()>0){
			req.setAttribute("errors", errors);
			req.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		
		/**
		 * 调用service的regist()方法
		 */
		try {
			userService.regist(form);
			/**
			 * 执行到这里说明userService执行成功，没有抛出异常
			 */
			Properties props = new Properties();
			props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
			String host = props.getProperty("host");
			String uname = props.getProperty("uname");
			String pwd = props.getProperty("pwd");
			String from = props.getProperty("from");
			String to = form.getEmail();
			String subject =props.getProperty("subject");
			String content = props.getProperty("content");
				content = MessageFormat.format(content, form.getCode());//替换{0}
			
				Mail mail	= new Mail(from,pwd);
				mail.setMailBody(content);
				mail.setSubject(subject);
				mail.addRecipient(to);
				
			
				try {
					mail.send();
				} catch (MessagingException e) {

				}
				
			 /*	1.保存成功信息
			 *	2.转发到msg.jsp
			 */
			req.setAttribute("msg", "恭喜您！注册成功，请马上到邮箱确认激活");
			return "f:/jsps/user/login.jsp";
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
	}
	
	
	
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();
		return "r:/index.jsp";
	}
	
	
}
