package com.solr.quartz;

import com.solr.initialize.InitQwgSolrDao;
import com.solr.util.ServiceCfg;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solr.util.HttpClientUtil;

public class UpdateQwgJob implements Job{
	
	private static final Logger	logger	= LoggerFactory.getLogger(UpdateQwgJob.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    
		logger.error("更新qwg索引及配置数据");
		
		InitQwgSolrDao.initQwgSolrData();
		
		HttpClientUtil.sendHttpClient(UpdateMainSearchDataJob.buildUpdateIndexUrl(ServiceCfg.getServiceParameterByName("qwg").get("solrRequestUrl")));
		
		logger.error("更新qwg索引及配置数据完成");
	}


}
