package com.flower;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.inject.BeanFactory;

import com.flower.invocation.ActionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action的分发器，本身是一个servlet，收到request后并不做实际的业务处理，而是组装action并交给处理。
 * 将Action对象的依赖注入交给common-util的Injector处理
 */
public class ActionDispatcher extends HttpServlet {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = -6663019953908113831L;
	
	private FlowerContext flowerContext;

	public void init(ServletConfig servletConfig) throws ServletException {
		String configFilePath = servletConfig.getInitParameter("config");
		if(configFilePath == null){
			// 由第三方调用初始化
			flowerContext = (FlowerContext) BeanFactory.getBean("flowerContext");
			if(flowerContext == null){
				logger.error("fail to init ActionDispatcher cause flower context is null");
				throw new IllegalArgumentException(ActionDispatcher.class + " init FAIL : fail to get flower context from facotry");
			}
			return;
		}else{
			// 自己调用初始化
			flowerContext = new FlowerContext(configFilePath);
		}
	}
	
	public void destroy() {
		flowerContext = null;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		String url = request.getRequestURI();
		String path = url;
		ActionWrapper actionWrapper = null;
		if (path.endsWith(".action")) {
			actionWrapper = flowerContext.getActionWrapper(path);
		} 
		if (actionWrapper == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Can NOT find specified action for name : "
					+ path);
			logger.warn("action not found : " + path);
			return;
		}
		try {
			ActionProxy proxy = flowerContext.getActionProxy(actionWrapper);
			proxy.execute();
		} catch (Exception e) {
			logger.error("error when executing action : ", e);
			sendHttpErrorPage(response);
		}
	}
	
	private void sendHttpErrorPage(HttpServletResponse response) throws IOException{
		response.getWriter().append("Server Error").flush();
	}
	

}
