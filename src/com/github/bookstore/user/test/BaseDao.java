package com.github.bookstore.user.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.github.jdbcUtils.TxQueryRunner;

public class BaseDao<T> {
	private QueryRunner qr = new TxQueryRunner();
	private Class<T> beanClass;
	private String priKey;
	private Field[] fs;
	
	
	public BaseDao() {
		//在构造器中，反射子类对象传递的泛型信息！！！
		beanClass=(Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		fs=beanClass.getDeclaredFields();
		priKey=fs[0].getAnnotation(Id.class).value();
	}
	
	
	
	
	public void add(T bean) throws SQLException, InstantiationException, IllegalAccessException{
		//获取bean的成员属性数组
		fs = beanClass.getDeclaredFields();
		//sql模板
		String sql ="insert into "+beanClass.getSimpleName()+" values(";
		//循环遍历数组得到参数个数，完成sql语句拼凑
		for(int i=0;i<fs.length;i++){
			sql+="?";
			if(i<fs.length-1){
				sql+=",";
			}
		}
		sql+=")";
		
		/**
		 * 为参数赋值
		 */
		List<Object> params = new ArrayList<Object>();
		for(Field f :fs){
			f.setAccessible(true);
			params.add(f.get(bean));
		}
		
		//执行之
		qr.update(sql,params.toArray());
	}
	public void update(T bean) throws IllegalArgumentException, IllegalAccessException, SQLException{
		//获取bean的成员属性数组
		Field[] fs = beanClass.getDeclaredFields();
		//sql模板
		String sql = "update "+beanClass.getSimpleName()+" set ";
		//获取主键信息
		
		for(int i = 0;i<fs.length;i++){
			if(!fs[i].isAnnotationPresent(Id.class)){
				//获取注解信息
				Column column =fs[i].getAnnotation(Column.class);
				sql+=column.value()+"=?";
					if(i<fs.length-1){
						
						sql+=",";
						System.out.println(column);
						System.out.println(sql);
				}
					
			}
		}
		
		sql+=" where "+priKey+"=?";
		

		/**
		 * 为参数赋值
		 */
		List<Object> params = new ArrayList<Object>();
		Object prikey ="";
		for(Field f :fs){
			f.setAccessible(true);
			if(!f.isAnnotationPresent(Id.class)){
				params.add(f.get(bean));
			}
			prikey=f.get(bean);
		}
		params.add(prikey);
		System.out.println(sql);
		//执行之
		qr.update(sql,params.toArray());
	}
	
	
	
	public void delete(String uuid) throws SQLException{
		String sql = "delete from "+beanClass.getSimpleName()+" where "+priKey
				+"=?";
		System.out.println(sql);
		System.out.println(uuid);
		qr.update(sql,uuid);
	}
	
	
	public T load(String uuid){
		return null;
	}
	public List<T> findAll(){
		return null;
	}
		


	
}
