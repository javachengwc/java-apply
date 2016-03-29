package com.solr.service;

import com.solr.service.impl.*;
import com.solr.service.qwg.QwgSolrService;
import com.solr.service.ru.RuSolrDataService;
import com.solr.service.ru.RuSolrService;
import com.solr.service.ru.RuUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class SolrServiceFactory {
	
	private final static Logger logger = LoggerFactory.getLogger(SolrServiceFactory.class);
	/**
	 * 创建具体的Service的工厂类,根据不同url找不同的service实现类
	 * @return 具体的service实现类
	 */
	public static SolrService getServiceInstance(HttpServletRequest request){
		SolrService service=null;
		String requestUrl=request.getRequestURI();
		String serviceName=getServerName(requestUrl);
		if("main".equals(serviceName)){
			UrlService urlService=new UrlService();
			SolrDataService dataService=new DefalutSolrDataService(urlService);
			ResultHandler resultHandler=new DefualtResultHandler();
			service=new DefaultSolrService(urlService,dataService,resultHandler);
			return service;
		}else if("ru".equals(serviceName)){
			RuUrlService urlService = new RuUrlService();
			RuSolrDataService dataService = new RuSolrDataService(urlService);
			ResultHandler resultHandler=new DefualtResultHandler();
			service = new RuSolrService(urlService,dataService,resultHandler);
			return service;
		}else if ("qwg".equals(serviceName)) {
			ResultHandler resultHandler=new DefualtResultHandler();
			service = new QwgSolrService(resultHandler);
			return service;
		}
		return null;
	}
   
	private static String getServerName(String requestUrl) {
		if (requestUrl.contains("qwg")) { return "qwg"; }
        if(requestUrl.contains("ybl")){return "qwg";}
		if(requestUrl.contains("russia")){return "russia";}
		return "main";
	}
}