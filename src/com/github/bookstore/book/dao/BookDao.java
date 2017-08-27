package com.github.bookstore.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.github.bookstore.book.domain.Book;
import com.github.bookstore.category.domain.Category;
import com.github.bookstore.paper.domain.PageBean;
import com.github.commonutils.CommonUtils;
import com.github.jdbcUtils.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查询所有图书
	 * @param ps 
	 * @param pc 
	 * @return
	 */
	public PageBean<Book> findAll(int pc, int ps){
		try {
			PageBean<Book> pb = new PageBean<Book>();
	 		pb.setPc(pc);
	 		pb.setPs(ps);
	 		/**
	 		 * 得到tr
	 		 */
 			String sql = "select count(*) from book where del=false";
			Number num=(Number)qr.query(sql, new ScalarHandler());
			int tr = num.intValue();
			pb.setTr(tr);
			 sql ="select * from book  where del=false order by bname limit ?,?";
		List<Book> bookList =qr.query(sql,
				new BeanListHandler<Book>(Book.class),(pc-1)*ps,ps);
			pb.setBeanlist(bookList);
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Book> findByCategory(String cid) {
		String sql = "select * from book where cid=? and del=false";
		try {
			return qr.query(sql, new BeanListHandler<Book>(Book.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载方法
	 * @param bid
	 * @return
	 */
	public Book findByBid(String bid) {
		/**
		 * 我们需要在book对象中保存category的信息
		 */
		String sql = "select * from book where bid=?";
		try {
			/**
			 * 使用一个map映射两个对象，再给两个对象建立关系！
			 */
			Map<String,Object> map= qr.query(sql, new MapHandler(),bid);
			Category category = CommonUtils.toBean(map, Category.class);
			Book book = CommonUtils.toBean(map, Book.class);
			book.setCategory(category);
			return book;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询指定图书分类下的图书本数
	 * @param cid
	 * @return
	 */
	public int getCountByCid(String cid) {
		String sql = "select count(*) from book where cid=?";
		try {
			Number num =(Number) qr.query(sql, new ScalarHandler(),cid);
			return num.intValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加图书	
	 * @param book
	 */
	public void add(Book book) {
		try {
			
			String sql = "insert into book values(?,?,?,?,?,?,?)";
			Object[] params = {book.getBid(),book.getBname(),book.getPrice(),
					book.getCategory().getCid(),book.getAuthor(),book.getImage(),false
			};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 删除图书
	 * 假删除，其实就一个update语句
	 * @param bid
	 */
	public void delete(String bid){
			try {
			String sql = "update book set del=true where bid=?";
			qr.update(sql,bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void edit(Book book) {
		try {
			String sql = "update book set bname=?,price=?,cid=?,author=?,image=? where bid=?";
			Object[] params = {book.getBname(),book.getPrice(),
					book.getCategory().getCid(),book.getAuthor(),book.getImage(),
					book.getBid()};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 多条件组合查询
	 * @param criteria
	 * @return
	 */
//	public List<Book> query(Book criteria) {
//		System.out.println(criteria);
//		/**
//		 * 给出sql模板
//		 */
//		StringBuilder sql =new StringBuilder("select * from book where del=0");
//		/**
//		 * 判断条件，完sql where子句
//		 */
//		
//		/**
//		 * 创建一个arrayList用来装载参数值
//		 */
//		List<Object> params = new ArrayList<Object>();
//		
//		String bname =criteria.getBname();
//		if(!bname.trim().isEmpty() && bname!=null){/////我竟然在这里犯了一个逻辑错误，害我找了半天！！！复制粘贴不好用！
//			sql.append(" and bname like ?");
//			params.add("%"+bname+"%");
//		}
//		String author =criteria.getAuthor();
//		if(!author.trim().isEmpty() && author!=null){
//			sql.append(" and author like ?");
//			params.add("%"+author+"%");
//		}
//		System.out.println(sql.toString());
//		try {
//			return qr.query(sql.toString(), new BeanListHandler<Book>(Book.class),params.toArray());
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//		
//	}
	
	
	public PageBean<Book> query(Book criteria,int pc,int ps) {
		try {
			/**
			 * 1.创建pageBean对象
			 * 2.设置已有属性
			 * 3.得到tr
			 * 4.得到beanList
			 */
			PageBean<Book> pb = new PageBean<Book>();
			pb.setPc(pc);
			pb.setPs(ps);
			
			/**
			 * 得到tr
			 */
			StringBuilder cntSql =new StringBuilder("select count(*) from book");
			StringBuilder whereSql = new StringBuilder(" where del=0");
			List<Object> params = new ArrayList<Object>();
			
			String bname =criteria.getBname();
			if(!bname.trim().isEmpty() && bname!=null){/////我竟然在这里犯了一个逻辑错误，害我找了半天！！！复制粘贴不好用！
				whereSql.append(" and bname like ?");
				params.add("%"+bname+"%");
			}
			String author =criteria.getAuthor();
			if(!author.trim().isEmpty() && author!=null){
				whereSql.append(" and author like ?");
				params.add("%"+author+"%");
			}
			/**
			 * select count(*) ... +where 子句
			 * 执行之
			 */
			Number num =(Number) qr.query(cntSql.append(whereSql).toString(), new ScalarHandler(),params.toArray());
			int tr=num.intValue();
			pb.setTr(tr);
			
			/**
			 * 得到beanList
			 */
			StringBuilder sql = new StringBuilder("select * from book");
			//where子句还需要给出limit子句！
			StringBuilder limitSql = new StringBuilder(" limit ?,?");
			//params中需要给出limit后两个参数的值
			params.add((pc-1)*ps);
			params.add(ps);
			//执行之
			List<Book> bookList = qr.query(sql.append(whereSql).append(limitSql).toString(),
					new BeanListHandler<Book>(Book.class),params.toArray());
			pb.setBeanlist(bookList);
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
}
