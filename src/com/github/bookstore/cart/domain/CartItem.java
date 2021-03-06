package com.github.bookstore.cart.domain;

import java.math.BigDecimal;

import com.github.bookstore.book.domain.Book;

/**
 * 购物车条目类
 * @author Administrator
 *
 */
public class CartItem {
	
	private Book book;//商品
	private int count; //数量
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * 小计方法
	 * 处理了二进制运算误差问题！
	 * @return
	 */
	public double getSubtotal(){//小计方法，但它没有对应的成员！
		//使用bigdecimal避免误差！
		BigDecimal d1 = new BigDecimal(book.getPrice()+"");
		BigDecimal d2 = new BigDecimal(count +"");
		return d1.multiply(d2).doubleValue();
	}
}
