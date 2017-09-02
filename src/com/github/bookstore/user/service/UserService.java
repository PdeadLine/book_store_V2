package com.github.bookstore.user.service;

import com.github.bookstore.user.dao.Dao;
import com.github.bookstore.user.domain.User;
/**
 * user业务层
 * @author Administrator
 *
 */
public class UserService {
	private Dao userDao = new Dao();
	
	/**
	 * 注册功能
	 * @param form
	 */
	public void regist(User form)throws UserException{
		
		//校验用户名
		User user = userDao.findByUserName(form.getUsername());
		if(user!=null) throw new UserException("用户名已被注册！");
		
		//校验email
		user = userDao.findByEmail(form.getEmail());
		if(user!=null) throw new UserException("邮箱已被注册！");
		
		//添加用户到数据库中
		userDao.addUser(form);
	}
	/**
	 * 激活功能
	 * @param code
	 */
	public void active(String code)throws UserException{
		User user= userDao.findByCode(code);
		if(user==null)throw new UserException("激活码无效！");
		
		if(user.isState())throw new UserException("用户已经激活！");
		
		/**
		 * 修改用户状态
		 */
		userDao.updateState(user.getUid(), true);
		
	}
	
	//登录功能
	public User login(User form) throws UserException{
		
		/**
		 * user为null ，抛出异常
		 * 用户名和密码不匹配 ， 抛出异常
		 * 用户为激活，抛出异常
		 * 返回user
		 */
		User user = userDao.findByUserName(form.getUsername());
		if(user==null)throw new UserException("用户名不存在！");
		if(!user.getPassword().equals(form.getPassword()))
			throw new UserException("密码错误！");
		if(!user.isState())throw new UserException("用户未激活，请激活后在登陆！");
		
		return user;
	}
	
}
