package com.github.bookstore.user.test;

import org.junit.Test;


public class Demo01 {
	private User user = new User();
	private UserDao userDao = new UserDao();
	@Test
	public void fun1(){
		
		
//		user.setId("10000");
//		user.setPassword("9527");
//		user.setUsername("zhangsan");
//		
//		
//		userDao.addUser(user);
		
/*		user.setUsername("lisi");
		user.setPassword("10086");
		userDao.updatUser(user);
		*/
	
		userDao.deleteById("10000");
		
	}
}
