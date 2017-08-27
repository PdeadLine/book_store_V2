package com.github.bookstore.user.test;

import java.sql.SQLException;

public class UserDao extends BaseDao<User>{
		public void addUser(User user){
			try {
					super.add(user);
				
				} catch (Exception e) {
					throw new RuntimeException(e);
			}
		}
		
		public void updatUser(User user){
			try {
				super.update(user);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		public void deleteById(String uid){
			try {
				super.delete(uid);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}

