package com.solr.quartz;

import com.solr.initialize.InitRuDao;
import com.solr.util.ServiceCfg;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solr.util.HttpClientUtil;

public class UpdateRuDataJob implements Job{
	
	private static final Logger	logger	= LoggerFactory.getLogger(UpdateRuDataJob.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    
		logger.error("更新ru索引及配置数据");
		
		InitRuDao.initAllRuData();
		
		HttpClientUtil.sendHttpClient(UpdateMainSearchDataJob.buildUpdateIndexUrl(ServiceCfg.getServiceParameterByName("ru").get("solrRequestUrl")));
		
		logger.error("更新ru索引及配置数据完成");
	}

}
