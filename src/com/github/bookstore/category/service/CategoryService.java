package com.github.bookstore.category.service;

import java.util.List;

import com.github.bookstore.book.dao.BookDao;
import com.github.bookstore.category.dao.CategoryDao;
import com.github.bookstore.category.domain.Category;
import com.github.bookstore.category.web.servlet.admin.CategoryException;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() {
		
		return categoryDao.findAll();
	}
	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category) {
		
		categoryDao.add(category);
	}
	
	/**
	 * 删除分类
	 * @param cid
	 * @throws CategoryException 
	 */
	public void delete(String cid) throws CategoryException {
		//获取该分类下的图书本书
		int count = bookDao.getCountByCid(cid);
		//如果该分类下存在图书，不让删除，抛出异常信息！
		if(count>0)throw new CategoryException("该分类下还有图书，不能进行删除！");
		//删除分类
		categoryDao.delete(cid);
	}
	/**
	 * 加载分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid) {
		return categoryDao.load(cid);
	}
	public void edit(Category category) {
		categoryDao.edit(category);
	} 
	
}
