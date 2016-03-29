package com.solr.quartz;

import com.solr.util.ServiceCfg;
import com.solr.util.SysConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solr.initialize.InitDao;
import com.solr.util.HttpClientUtil;

/**
 * 定时更新任务的job
 */
public class UpdateMainSearchDataJob implements Job {
    
	private static final Logger	logger	= LoggerFactory.getLogger(UpdateMainSearchDataJob.class);
	private static final String URLSUFFIX="dataimport?command=full-import&clean=true&commit=true&verbose=false";
	/**
	 * 更新主站索引数据
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//作业开始时间
		long beginTime = System.currentTimeMillis();

        logger.error("开始主站数据存储过程更新");
		boolean isExecuteSucess=InitDao.dataImportProcedurePre();

		//如果存储执行失败，就不执行刷新数据操作
		if(isExecuteSucess==false){
			logger.error("存储调用失败");
			return;
		}

        logger.error("更新主站索引");
		HttpClientUtil.sendHttpClient(buildUpdateIndexUrl(ServiceCfg.getServiceParameterByName("default").get("solrRequestUrl")));

		logger.error("更新产品分类索引");
		HttpClientUtil.sendHttpClient(buildUpdateIndexUrl(SysConfig.getValue("bottom_cat_url")));

		logger.error("更新初始化数据");
		InitDao.initGroupInfo();

        long endTime=System.currentTimeMillis();
        logger.error("作业执行:"+(endTime-beginTime)/(60*1000)+"分钟");
	}
	
	public static String buildUpdateIndexUrl(String serviceName){
		if(serviceName.endsWith("/")){
			return serviceName+URLSUFFIX;
		}else{
			return serviceName+"/"+URLSUFFIX;
		}
		
	}
}
