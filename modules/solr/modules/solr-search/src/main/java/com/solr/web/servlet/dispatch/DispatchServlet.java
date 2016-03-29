package com.solr.web.servlet.dispatch;

import com.solr.service.SolrService;
import com.solr.service.SolrServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求拦截和分发器
 */
public class DispatchServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置请求头的信息
		setHttpResponseHead(response);
		// 构建服务service
		SolrService service = SolrServiceFactory.getServiceInstance(request);
		// 完成请求处理结果
		if (null != service) {// 非空才会执行
			service.dealSolrService(request, response);
		}
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 设置请求头信息
	 */
	private void setHttpResponseHead(HttpServletResponse response) {

		response.setCharacterEncoding("utf-8");

	}

}
