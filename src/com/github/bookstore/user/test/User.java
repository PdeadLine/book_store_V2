package com.github.bookstore.user.test;

@Table("tb_user")//它表示当前类对应的表
public class User {
	@Id("u_id")//表示当前类属性对应表字段，并且是主键
	private String id;
	@Column("uname")
	private String username;
	@Column("pwd")
	private String password;
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + "]";
	}
	
	
}
