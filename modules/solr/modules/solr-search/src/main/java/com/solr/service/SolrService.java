package com.solr.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SolrService {

	/**
	 * 处理所有的slor请求，包括数据查询请求和索引更新需求
	 */
	public boolean dealSolrService(HttpServletRequest request, HttpServletResponse response);

}
