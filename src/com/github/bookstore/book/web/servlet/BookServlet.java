package com.github.bookstore.book.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bookstore.book.domain.Book;
import com.github.bookstore.book.service.BookService;
import com.github.bookstore.paper.domain.PageBean;
import com.github.web.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	
	/**
	 * 查询所有图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);//得到pc
		int ps = 8;//给定ps的值
		PageBean<Book> pb = bookService.findAll(pc,ps);//传递ps,pc给service,得到pagebean
		
		req.setAttribute("pb", pb); //保存到request域中
		req.setAttribute("bookList",pb.getBeanlist() );
		
		return "f:/jsps/book/list.jsp";
	}
	/**
	 * 按分类 查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		req.setAttribute("bookList",bookService.findByCategory(cid));
		return "f:jsps/book/list.jsp";
	}
	
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		req.setAttribute("book",bookService.load(bid));
		return "f:/jsps/book/desc.jsp";
	}
	
	
	/**
	 * 获取pc当前页的值
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		String value = req.getParameter("pc");
		if(value==null || value.isEmpty()){
			return 1;
		}
		return Integer.parseInt(value);
	}
}
