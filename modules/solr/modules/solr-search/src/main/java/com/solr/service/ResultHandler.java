package com.solr.service;

import com.solr.model.context.MyContext;

import javax.servlet.http.HttpServletResponse;

public interface ResultHandler {
	
	public void handlerResultData(HttpServletResponse response, MyContext context);

}
