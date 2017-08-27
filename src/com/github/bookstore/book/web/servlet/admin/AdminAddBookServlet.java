package com.github.bookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.github.bookstore.book.domain.Book;
import com.github.bookstore.book.service.BookService;
import com.github.bookstore.category.domain.Category;
import com.github.bookstore.category.service.CategoryService;
import com.github.commonutils.CommonUtils;

public class AdminAddBookServlet extends HttpServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/**
		 * 一、把表单数据封装到book对象中
		 * .上传三步：
		 * 		创建工厂、创建解析器、解析request
		 */
		//创建工厂
		DiskFileItemFactory factory = new DiskFileItemFactory(20*1024,new File("h:/f/temp"));//缓存大小，缓存目录
		//解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(20*1024);//设置单个文件大小为20kb
		
		//解析request对象，得到List<FileItem>,其中没个fileItem对应一个表单部件
		try {
			List<FileItem> fileItemList = sfu.parseRequest(request);
			/**
			 * 把fileItemList中的数据封装到book对象中。
			 *	>把所有的普通表单项中的数据先封装到map中
			 *	>再把map中的数据封装到book对象中
			 */
			Map<String,String> map = new HashMap<String, String>();
			for(FileItem fileItem : fileItemList){
				if(fileItem.isFormField()){
					map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
				}
			}
			Book book = CommonUtils.toBean(map, Book.class);
			//补全bid
			book.setBid(CommonUtils.uuid());
			
			/**
			 * 需要把map的cid封装到category对象中，再和book对象关联起来
			 */
			Category  category =  CommonUtils.toBean(map, Category.class); 
			book.setCategory(category);
			
			
			/**
			 * 二、保存上传的文件
			 * 保存目录(绝对路径获取
			 * 保存文件名称（文件名冲突问题
			 */
			//得到保存的目录
			
			String savePath = this.getServletContext().getRealPath("/book_img");
			//得到文件名称
			String fileName = CommonUtils.uuid()+"_"+fileItemList.get(1).getName();
			
			
			//使用目录和文件名称创建目标文件
			File destFile = new File (savePath,fileName);
			//保存上传文件到目标文件位置
			fileItemList.get(1).write(destFile);
			
			/**
			 * 三、设置book对象的image,即把图片的路径设置给book的image.
			 */
			book.setImage("book_img/"+fileName);
			
			//校验文件扩展名
			if(!fileName.toLowerCase().endsWith("jpg")){
				request.setAttribute("msg", "您上传的文件不是jpg格式");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return ;
			}
			
			/**
			 * 四、使用bookService完成保存
			 */
				bookService.add(book);
				
				
				/***
				 * 校验图片的尺寸
				 */
				Image image = new ImageIcon(destFile.getAbsolutePath()).getImage();
				if(image.getWidth(null) > 200 || image.getHeight(null) > 200){
					destFile.delete();//删除这个文件
					
					request.setAttribute("msg", "您上传的文件尺寸超过200x200");
					request.setAttribute("categoryList", categoryService.findAll());
					request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
					return;
				}
			
				
			/**
			 * 五、返回到图书列表	
			 */
			request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll").forward(request, response);
				
		} catch (Exception e) {
			if(e instanceof FileUploadBase.FileSizeLimitExceededException){
				request.setAttribute("msg", "您上传的文件超出了20kb");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
			}
			throw new RuntimeException(e);
		}
		
		
	}

}
