package com.github.bookstore.user.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.github.bookstore.user.domain.User;

/**
 * Servlet Filter implementation class loginFilter
 */
public class loginFilter implements Filter {

	public void destroy() {
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/**
		 * 1.从session中获取用户信息
		 * 2.判断session中是否存在用户信息，如有则放行！
		 * 3.否则，保存错误信息，转发到login.jsp显示
		 */
		//强转httpservlet
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		User user=(User) httpRequest.getSession().getAttribute("sessoin_user");
		if(user!=null){
		chain.doFilter(request, response);
		}else{
			httpRequest.setAttribute("msg", "您还没登陆！");
			httpRequest.getRequestDispatcher("/jsps/user/login.jsp").forward(httpRequest, response);
		}
	}
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
