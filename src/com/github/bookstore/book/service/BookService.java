package com.github.bookstore.book.service;

import java.util.List;

import com.github.bookstore.book.dao.BookDao;
import com.github.bookstore.book.domain.Book;
import com.github.bookstore.paper.domain.PageBean;
import com.github.bookstore.user.dao.Dao;

public class BookService {
	private BookDao bookDao = new BookDao();
	
	public PageBean<Book> findAll(int pc, int ps){
		return bookDao.findAll(pc,ps);
	}

	public List<Book> findByCategory(String cid) {
		
		return bookDao.findByCategory(cid);
	}

	public  Book load(String bid) {
		return bookDao.findByBid(bid);
	}
	
	/**
	 * 添加图书
	 * @param book
	 */
	public void add(Book book) {
		bookDao.add(book);	
	}
	
	public void delete(String bid){
		bookDao.delete(bid);
	}
	
	/**
	 * 修改图片
	 * @param book
	 */
	public void edit(Book book) {
		bookDao.edit(book);
	}

	public PageBean<Book> query(Book criteria, int pc, int ps) {
		return bookDao.query(criteria,pc,ps);
	}
	
}
