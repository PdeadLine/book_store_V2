package com.github.bookstore.order.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.bookstore.user.domain.User;

public class Order {
	
	private String oid;
	private Date ordertime;//下单时间
	private double total;//合计
	private int state;//下单状态，1未付款 2已付款未发货 3已发货，未确认收货  4。交易完成
	private User owner;//订单所有者
	private String reciveAddress;//收货地址
	private String reciveName;//收货人姓名
	public String getReciveName() {
		return reciveName;
	}
	public void setReciveName(String reciveName) {
		this.reciveName = reciveName;
	}
	public String getRecivePhone() {
		return recivePhone;
	}
	public void setRecivePhone(String recivePhone) {
		this.recivePhone = recivePhone;
	}
	private String recivePhone;//收货人电话
	private List<OrderItem> orderItemList ;//当前订单下所有条目

	
	
	
	public String getReciveAddress() {
		return reciveAddress;
	}
	public void setReciveAddress(String reciveAddress) {
		this.reciveAddress = reciveAddress;
	}
	
	
	
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	
}
