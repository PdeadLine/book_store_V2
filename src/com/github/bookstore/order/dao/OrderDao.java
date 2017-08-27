package com.github.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.github.bookstore.book.domain.Book;
import com.github.bookstore.order.domain.Order;
import com.github.bookstore.order.domain.OrderItem;
import com.github.commonutils.CommonUtils;
import com.github.jdbcUtils.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 添加订单
	 * @param order
	 */
	public void addOrder(Order order){
		/**
		 * 处理util的date转换成sql的timestamp
		 */
		Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
		
		String sql = "insert into orders(`oid`,`ordertime`,`total`,`state`,`uid`,`reciveAddress`) values(?,?,?,?,?,?)";
		Object[] params ={order.getOid(),timestamp,
				order.getTotal(),order.getState(),
				order.getOwner().getUid(),order.getReciveAddress()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 插入订单条目
	 * @param orderItemList
	 */
	public void addOrderItemList(List<OrderItem> orderItemList){
		/**
		 * QueryRunner类的batch(String sql,Object[][] params)
		 * 其中params是多个一维数组！
		 */
		try {
			String sql = "insert into orderitem values(?,?,?,?,?)";
			/**
			 * 把orderItemList转换成二维数组
			 */
			Object[][] params = new Object[orderItemList.size()][];
			
			//循环遍历orderItemList,使用每个orderItem对象为params中每个一维数组赋值
			for(int i=0;i<orderItemList.size();i++){
				OrderItem item = orderItemList.get(i);
				params[i]=new Object[]{item.getIid(),item.getCount(),item.getSubtotal(),
						item.getOrder().getOid(),item.getBook().getBid()};
			}
			qr.batch(sql, params);//执行批处理
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按uid查询订单
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		/**
		 * 1.通过uid查出当前用户所有的List<Order>
		 * 2.循环遍历每个order,为其加载它的所有orderItem
		 */
		try {
			/**
			 * 1)得到当前用户的所有的订单
			 */
			String sql = "select * from orders where uid=?";
			List<Order> orderList= qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			/**
			 * 2）循环遍历每个order,为其加载所有的订单条目
			 */
			for(Order order : orderList){
				loadOrderItems(order);//该方法为order对象加载其所有的订单条目
			}
			
			/**
			 * 3)返回订单列表
			 */
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 加载指定的订单所有的订单条目
	 * @param order
	 */
	private void loadOrderItems(Order order) {
		/**
		 * 查询两张表：orderitem,book
		 */
		String sql = "select * from orderitem i,book b where i.bid=b.bid and oid=?";
		try {
			/**
			 * 因为一行结果集对应的不再是一个javaBean,所以不能再使用BeanListHandler,而是MapListHandler!
			 */
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),order.getOid());
			
			/**
			 * mapList是多个map,每个map对应一行记果集
			 * 
			 * 我们需要使用一个map，生成两个对象：orderItem,book,然后建立两者的关系（把book设置给orderItem)
			 */
			/**
			 * 循环遍历每个map,使用map生成两个对象，然后建立关系（最终结果得到一个orderItem),然后把orderItem保存起来
			 */
			List<OrderItem> orderItemList = toOrderItemList(mapList);
			order.setOrderItemList(orderItemList);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 把mapList中每个Map转换成两个对象，并建立关系。
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList =new ArrayList<OrderItem>(); 
		for(Map<String,Object> map:mapList){
			
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}

	/**
	 * 把一个Map转换成一个OrderItem对象
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book =CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	public Order findByOid(String oid) {
	
		try {
			/**
			 * 1)得到当前用户的所有的订单
			 */
			String sql = "select * from orders where oid=?";
			Order order= qr.query(sql, new BeanHandler<Order>(Order.class), oid);
			/**
			 * 2）循环遍历每个order,为其加载所有的订单条目
			 */
				loadOrderItems(order);//该方法为order对象加载其所有的订单条目
			
			/**
			 * 3)返回订单列表
			 */
			return order;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 */
	public int getStateByOid(String oid){
		try {
			String sql = "select state from orders where oid=?";
			return  (Integer)qr.query(sql, new ScalarHandler(),oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @param state
	 */
	public void updateState(String oid,int state){
		try {
			String sql = "update orders set state=? where oid=?";
			
			qr.update(sql,state,oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
