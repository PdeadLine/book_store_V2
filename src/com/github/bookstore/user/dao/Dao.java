package com.github.bookstore.user.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.github.bookstore.user.domain.User;
import com.github.jdbcUtils.TxQueryRunner;

/**
 * 
 * @author Administrator
 *
 */
public class Dao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 按用户名查询
	 * 
	 * @param username
	 * @return
	 */
	public User findByUserName(String username){
		try{
		String sql ="select * from t_user where username=?";
		
		return qr.query(sql, new BeanHandler(User.class),username);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 用邮件名查询
	 * @param email
	 * @return
	 */
	public User findByEmail(String email){
		try{
		String sql ="select * from t_user where email=?";
		return qr.query(sql, new BeanHandler(User.class),email);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加用户
	 * @param user
	 */
	public void addUser(User user){
		try{
		String sql ="insert into t_user values(?,?,?,?,?,?)";
		
		Object[] params ={user.getUid(),user.getUsername(),user.getPassword(),user.getEmail(),
				user.getCode(),user.isState()};
		 qr.update(sql,params);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 按激活码查询
	 * 
	 * @param code
	 * @return
	 */
	public User findByCode(String code){
		try{
		String sql ="select * from t_user where code=?";
		
		return qr.query(sql, new BeanHandler(User.class),code);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void updateState(String uid,boolean state){
		try{
		String sql ="update t_user set state=? where uid=?";
		qr.update(sql, state,uid);
		
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
