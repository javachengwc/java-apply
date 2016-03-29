package com.manageplat.service.job.executor;

import com.manageplat.model.job.JobInfo;
import com.manageplat.service.job.HttpJob;
import com.manageplat.service.job.QuartzService;
import com.manageplat.util.SpringContextUtils;
import org.apache.log4j.Logger;

/**
 * http类型定时任务执行器
 *
 */
public class HttpJobExecutor extends DefaultJobExecutor {
	
	private static Logger logger = Logger.getLogger(HttpJobExecutor.class);
	
	public HttpJobExecutor(JobInfo jobInfo)
	{
		super(jobInfo);
	}

	public boolean execute()
	{
		QuartzService quartzService = SpringContextUtils.getBean("quartzService", QuartzService.class);
		boolean rt=true;
		try{
		  rt =quartzService.updateOrAddJob(jobInfo, HttpJob.class);
		}catch(Exception e)
		{
			logger.error("HttpJobExecutor execute error,jobInfo name="+jobInfo.getJobName()+",expr="+jobInfo.getExpression(),e);
			rt=false;
		}
		return rt;
	}
}
