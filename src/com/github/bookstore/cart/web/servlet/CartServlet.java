package com.github.bookstore.cart.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bookstore.book.domain.Book;
import com.github.bookstore.book.service.BookService;
import com.github.bookstore.cart.domain.Cart;
import com.github.bookstore.cart.domain.CartItem;
import com.github.web.servlet.BaseServlet;

public class CartServlet extends BaseServlet {

	/**
	 * 添加购物条目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.得到车
		 * 2.得到条目（得到图书和数量）
		 * 3.把条目添加到车中
		 */
		//1）.得到车
		Cart cart =(Cart) req.getSession().getAttribute("cart");
		
		/**
		 * 表单传递的只有bid和数量
		 * 2）.得到条目
		 * >得到图书和数量
		 */
		String bid = req.getParameter("bid");
		Book book=new BookService().load(bid);
		int count = Integer.parseInt(req.getParameter("count"));
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		
		/**
		 * 3.把条目添加到车中
		 */
		cart.add(cartItem);
		
		return null;
		
		
	}
	
	/**
	 * 删除购物条目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.得到车
		 * 2.得到要删除的bid！
		 */
		Cart cart =(Cart) req.getSession().getAttribute("cart");

		String bid = req.getParameter("bid");
		
		cart.delete(bid);
		
		return "f:/jsps/cart/list.jsp";
		
		
	}
	
	public String clear(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.得到车
		 * 2.调用车的clear方法！
		 */
		Cart cart =(Cart) req.getSession().getAttribute("cart");
		
		cart.clear();
		
		return "f:/jsps/cart/list.jsp";
		
		
	}
}
