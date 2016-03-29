package com.manage.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manage.service.rbac.AdminorHolder;

public class SecurityFilter implements Filter{

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse hresponse =(HttpServletResponse) response;
		HttpServletRequest hrequest =(HttpServletRequest) request;
		String url=hrequest.getRequestURL().toString();
		
		if(url.indexOf("login.jsp")!=-1 || url.indexOf("login!login.action")!=-1 )
		{
			chain.doFilter(request, response);
			return;
		}
		Integer userId =(Integer)hrequest.getSession().getAttribute("userId");
		if(userId==null)
		{
			hresponse.sendRedirect(hrequest.getContextPath()+"/login.jsp");
			return;
		}
		
//		hresponse.addHeader("Cache-Control", "no-cache,no-store,max-age=0");
//		hresponse.addHeader("Cache-pragma", "no-cache");
//		hresponse.addHeader("expires", "0");
		AdminorHolder.setCurrentUserId(userId);
		
		chain.doFilter(request, response);
		
		AdminorHolder.cleanUserId();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
