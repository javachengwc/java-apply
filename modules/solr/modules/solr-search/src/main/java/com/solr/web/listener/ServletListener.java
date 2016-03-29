package com.solr.web.listener;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.solr.initialize.*;
import com.solr.util.ServiceCfg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solr.initialize.InitQwgSolrDao;

public class ServletListener implements ServletContextListener {
	
	private final Logger log = LoggerFactory.getLogger(ServletListener.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {
		log.error("contextDestroyed================"+new Date().getTime());
	}

	public void contextInitialized(ServletContextEvent arg0) {

		
		log.error("开始初始化项目数据");
		log.error("service.cfg.xml读取....");
		ServiceCfg.init();
		
		InitDao.initUserDefinedPropertyOnly();
		InitDao.initGroupInfo();// 初始化搜索数据

		//加载IK用户词典
		log.error("加载用户词典....");
		IKDictionary.addWordToFile();
		log.error("加载用户词典完成....");

		log.error("搜索项数据加载...");
		InitQwgSolrDao.initQwgSolrData();
		log.error("搜索项数据加载完成");

		log.error("项目数据初始化完成");
	}
}
