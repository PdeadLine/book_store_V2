package com.github.bookstore.category.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bookstore.category.domain.Category;
import com.github.bookstore.category.service.CategoryService;
import com.github.commonutils.CommonUtils;
import com.github.web.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 修改之前的加载工作
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.封装表单数据
		 * 2.调用service方法完成修改工作
		 * 3.调用findAll()
		 */
		Category category =  CommonUtils.toBean(req.getParameterMap(), Category.class);
		categoryService.edit(category);
		return findAll(req,resp);
	}
	
	/**
	 * 修改分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		req.setAttribute("category",categoryService.load(cid));
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	
	
	/**
	 * 查询所有分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 调用service#findAll得到所有List<Category>
		 * 保存到request域中，转发/adminjsps/admin/category/list.jsp
		 */
		List<Category> categoryList=categoryService.findAll();
		req.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.封装表单数据
		 * 2.补全javabean
		 * 3.调用service方法完成添加
		 * 4.调用findAll（）[刷新页面
		 */
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		
		categoryService.add(category);
		return findAll(req,resp);
	}
	/**
	 * 删除分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.获取参数
		 * 2.调用service方法
		 * >如果抛出异常，保存异常信息到request域中，转发到msg.jsp中
		 * 3.调用findAll()
		 */
		String cid = req.getParameter("cid");
		try {
			categoryService.delete(cid);
			return findAll(req,resp);
		} catch (CategoryException e) {
			req.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
	}
}
