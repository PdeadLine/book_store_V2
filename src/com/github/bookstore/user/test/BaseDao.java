package com.github.bookstore.user.test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

import com.github.commonutils.CommonUtils;
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
			//判断该属性是否为主键（即查询条件）
			f.setAccessible(true);
			if(!f.isAnnotationPresent(Id.class)){
				params.add(f.get(bean));
			}
			prikey=f.get(bean);
		}
		//最后添加查询条件，即主键
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
	
	
	public T load(String uuid) throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException{
		String sql = "select * from "+beanClass.getSimpleName()+" where "
				+priKey+"=?";
		/**
		 * 因为表字段和bean属性名不对应，需要特殊处理
		 */
		//bean对象实例化
		T t = beanClass.newInstance();
		//查询返回一个map对象
		Map<String, Object> map =qr.query(sql, new MapHandler(),uuid);
		//将map对象封装到bean对象中！
		for(Object o : map.keySet()){
			for(Field f : fs){
				f.setAccessible(true);
				f.set(t, map.get(o));
			}
		}
		return t;
	}
	public List<T> findAll(){
		
		
		
		return null;
	}
		


	
}
