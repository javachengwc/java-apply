package com.manageplat.service.job.executor;

import com.manageplat.model.job.JobInfo;
import com.manageplat.service.job.MonitJob;
import com.manageplat.service.job.QuartzService;
import com.manageplat.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitJobExecutor implements IJobExecutor {
	
	public static String PREFIX="MONITOR_";

    private static Logger logger = LoggerFactory.getLogger(DefaultJobExecutor.class);
	
	protected JobInfo jobInfo;
	
	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	
	public MonitJobExecutor(JobInfo jobInfo)
	{
		this.jobInfo= jobInfo;
	}

	public boolean execute()
	{
		QuartzService quartzService = SpringContextUtils.getBean("quartzService", QuartzService.class);
		boolean rt=true;
		try{
		    rt =quartzService.updateOrAddJob(jobInfo, MonitJob.class,PREFIX+jobInfo.getJobName());
		}catch(Exception e)
		{
			logger.error("MonitJobExecutor execute error,jobInfo name="+jobInfo.getJobName()+",expr="+jobInfo.getExpression(),e);
			rt=false;
		}
		return rt;
	}
 
	public boolean del()
	{
		QuartzService quartzService =SpringContextUtils.getBean("quartzService",QuartzService.class);
		boolean rt=true;
		try{
		    rt =quartzService.deleteJob(PREFIX+jobInfo.getJobName());
		}catch(Exception e)
		{
			logger.error("MonitJobExecutor del error,jobInfo name="+jobInfo.getJobName()+",expr="+jobInfo.getExpression(),e);
			rt=false;
		}
		return rt;
	}
	
}
