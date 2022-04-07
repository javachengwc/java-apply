package com.manageplat.service.job.executor;

import com.manageplat.model.job.JobInfo;
import org.apache.commons.lang3.StringUtils;

public class JobExecuteFactory {
	
	public static IJobExecutor getExecuter(JobInfo jobInfo)
	{
		IJobExecutor executor=null;
		if(!StringUtils.isBlank(jobInfo.getType()) && "http".equalsIgnoreCase(jobInfo.getType()))
		{
			executor = new HttpJobExecutor(jobInfo);
		}
		return executor;
	}
	
	public static boolean executeJob(JobInfo jobInfo)
	{
		IJobExecutor executor = getExecuter(jobInfo);
		if(executor==null)
		{
			return false;
		}
		return executor.execute();
	}
    
	public static boolean delJob(JobInfo jobInfo)
	{
		IJobExecutor executor = new DefaultJobExecutor(jobInfo);
		return executor.del();
	}
}
