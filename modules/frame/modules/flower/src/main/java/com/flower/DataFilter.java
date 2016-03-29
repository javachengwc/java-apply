package com.flower;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.flower.annotation.Scope;
/**
 * DataFilter将request和response对象存放在静态ThreadLocal的实例变量中，保证每个请求的request和response能通过静态方法获取，从而减少传递参数。
 * DataFitler提供request的set方法，以便后面做request的重新封装。
 */
public class DataFilter implements Filter {

	static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();
	
	protected final Logger m_logger = Logger.getLogger(DataFilter.class);

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
//		Context previous = localContext.get();
		try {
			HttpServletResponse hsr = (HttpServletResponse) servletResponse;
			// servletRequest.setCharacterEncoding("UTF-8");
			// hsr.setHeader("Pragma", "No-cache");
			// hsr.setHeader("Cache-Control", "no-cache");
			// hsr.setHeader("Expires", "0");
			localContext.set(new Context((HttpServletRequest) servletRequest,
					hsr));
			filterChain.doFilter(servletRequest, servletResponse);
		}catch(ServletException se){
			throw se;
		} catch (Exception e) {
			 ((HttpServletResponse) servletResponse).sendRedirect("error.jsp");
			 m_logger.error("unknown error when executing doFilter:", e);
		}
		finally {
			localContext.remove();
		}
	}

	public static HttpServletRequest getRequest() {
		return getContext().getRequest();
	}
	
	public static void setRequest(HttpServletRequest request){
		HttpServletResponse response = getContext().getResponse();
		localContext.set(new Context(request, response));
	}

	public static HttpServletResponse getResponse() {
		return getContext().getResponse();
	}
	
	/**
	 * 从Request或Session或ApplicationContext中获得一个Attribute
	 * 
	 * @param key
	 * @return Attribut的值
	 */
	public static Object getAttribute(String key, Scope scope) {
		if (scope == null)
			return getAttribute(key);
		switch (scope) {
		case SESSION:
			return getRequest().getSession().getAttribute(key);
		case APPLICATION:
			return getRequest().getSession().getServletContext().getAttribute(
					key);
		default:
			return getRequest().getAttribute(key);
		}
	}
	
	/**
	 * 从Request,Session,ApplicationContext中获得一个Attribute 该方法将按照Request, Session,
	 * ApplicationContext的顺序搜索指定的Attribute
	 * 
	 * @param key
	 * @return Attribut的值
	 */
	protected static Object getAttribute(String key) {
		Object value = getAttribute(key, Scope.REQUEST);
		if (value == null) {
			value = getAttribute(key, Scope.SESSION);
			if (value == null) {
				value = getAttribute(key, Scope.APPLICATION);
			}
		}
		return value;
	}

	static Context getContext() {
		Context context = localContext.get();
		if (context == null) {
			throw new RuntimeException("Please apply "
					+ DataFilter.class.getName()
					+ " to any request which uses servlet scopes.");
		}
		return context;
	}

	static class Context {

		final HttpServletRequest request;

		final HttpServletResponse response;

		Context(HttpServletRequest request, HttpServletResponse response) {
			this.request = request;
			this.response = response;
		}

		HttpServletRequest getRequest() {
			return request;
		}

		HttpServletResponse getResponse() {
			return response;
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}
}
