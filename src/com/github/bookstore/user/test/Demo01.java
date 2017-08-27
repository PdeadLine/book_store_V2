package com.github.bookstore.user.test;

import java.sql.SQLException;

import org.junit.Test;


public class Demo01 {
	private User user = new User();
	private UserDao userDao = new UserDao();
	@Test
	public void fun1() throws SQLException{
		
		user.setId("789");

		
		System.out.println(userDao.load(user.getId()));
	}
}
