package com.solr.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.solr.service.InitInvokeService;
import com.solr.service.SolrService;

public class AdminServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//设置请求头的信息
		setHttpResponseHead(response);
		//构建服务service
		SolrService service=new InitInvokeService();
		//完成请求处理结果
		if(null!=service){//非空才会执行
			service.dealSolrService(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void setHttpResponseHead(HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
	}

}
