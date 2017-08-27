package com.github.bookstore.book.web.servlet.admin;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bookstore.book.domain.Book;
import com.github.bookstore.book.service.BookService;
import com.github.bookstore.category.domain.Category;
import com.github.bookstore.category.service.CategoryService;
import com.github.bookstore.paper.domain.PageBean;
import com.github.commonutils.CommonUtils;
import com.github.web.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	
	/**
	 * 高级搜索
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
//	public String query(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		/**
//		 * 封装表单数据到Book对象中，它只有三个属性，它就是一个条件
//		 * 使用book调用service方法
//		 * 保存到request域中
//		 * 转发到list.jsp
//		 */
//		Book criteria =CommonUtils.toBean(req.getParameterMap(), Book.class);
//		System.out.println(criteria);
//		List<Book> bookList = bookService.query(criteria);
//		req.setAttribute("bookList",bookList);
//		return "f:/adminjsps/admin/book/list.jsp";
//		
//	}
	
	public String query(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Book criteria =CommonUtils.toBean(req.getParameterMap(), Book.class);
		/**
		 * 处理get请求方式编码问题
		 */
		criteria = encoding(criteria);
		
		
		
		int pc = getPc(req);//得到pc
		int ps = 8;//给定ps的值
		
		PageBean<Book> pb = bookService.query(criteria,pc,ps);
		/**
		 * 得到url,保存到pb中
		 */
		pb.setUrl(getUrl(req));
		req.setAttribute("pb", pb);
		req.setAttribute("bookList",pb.getBeanlist());
		return "f:/adminjsps/admin/book/list.jsp";
		
	}
	
	/**
	 * 处理GET编码问题
	 * @param criteria
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private Book encoding(Book criteria) throws UnsupportedEncodingException {
		String bname = criteria.getBname();
		String author = criteria.getAuthor();
		if(bname!=null && !bname.trim().isEmpty()){
			bname = new String(bname.getBytes("ISO-8859-1"),"UTF-8");
			criteria.setBname(bname);
		}
		
		if(author!=null && !author.trim().isEmpty()){
			author = new String(author.getBytes("ISO-8859-1"),"UTF-8");
			criteria.setAuthor(author);
		}
		return criteria;
	}


	/**
	 * 添加图书之前
	 * 查询所有图书分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setAttribute("categoryList",categoryService.findAll());
		return "f:/adminjsps/admin/book/add.jsp";
	}
	/**
	 * 加载图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.获取参数bid，通过bid调用service方法得到book对象
		 * 2.获取所有分类，保存到request域中
		 * 3.保存book,category到request域中，转发到desc.jsp
		 */
		
		req.setAttribute("book", bookService.load(req.getParameter("bid")));
		req.setAttribute("categoryList",categoryService.findAll());
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
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
		
		/**
		 * 设置url
		 */
		pb.setUrl(getUrl(req));
		
		req.setAttribute("pb", pb); //保存到request域中
		req.setAttribute("bookList",pb.getBeanlist() );
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * 删除图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		bookService.delete(bid);
		return findAll(req,resp);
		
	}
	
	/**
	 * 修改图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		book.setCategory(category);
		System.out.println(book);
		bookService.edit(book);
		return findAll(req,resp);
		
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
	/**
	 * 截取url
	 *  /项目名/Servlet路径?参数字符串
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		String contextPath = req.getContextPath();//获取项目
		String servletPath = req.getServletPath();//获取servlet path
		String queryString = req.getQueryString();//获取问号之后的参数部分
		
		//判断参数中是否包含pc这个参数，如果包含，截取掉
		if(queryString.contains("&pc=")){
			int index = queryString.lastIndexOf("&pc=");
			queryString = queryString.substring(0,index);
		}
		
		return contextPath+servletPath+"?"+queryString;
	}
	
}
