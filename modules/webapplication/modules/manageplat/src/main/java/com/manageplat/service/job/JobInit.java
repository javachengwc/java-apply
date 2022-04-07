package com.manageplat.service.job;

import com.manageplat.util.SpringContextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class JobInit {

	private static Logger logger = Logger.getLogger(JobInit.class);
	
	public void init()
	{
		logger.error("JobInit start,");
		if(JobManager.autoStart)
		{
			JobService jobService = SpringContextUtils.getBean("jobService", JobService.class);
			jobService.startAllDriveJobs();
		}
		logger.error("JobInit end,");
	}
}