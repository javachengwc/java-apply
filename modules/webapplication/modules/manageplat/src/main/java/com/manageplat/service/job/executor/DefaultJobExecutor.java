package com.manageplat.service.job.executor;

import com.manageplat.model.job.JobInfo;
import com.manageplat.service.job.QuartzService;
import com.manageplat.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultJobExecutor implements IJobExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultJobExecutor.class);
	
	protected JobInfo jobInfo;
	
	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	
	public DefaultJobExecutor(JobInfo jobInfo)
	{
		this.jobInfo= jobInfo;
	}

	public boolean execute()
	{
		return false;
	}
 
	public boolean del()
	{
		QuartzService quartzService = SpringContextUtils.getBean("quartzService", QuartzService.class);
		boolean rt=true;
		try{
		  rt =quartzService.deleteJob(jobInfo.getJobName());
		}catch(Exception e)
		{
			logger.error("DefaultJobExecutor del error,jobInfo name="+jobInfo.getJobName()+",expr="+jobInfo.getExpression(),e);
			rt=false;
		}
		return rt;
	}
}
