package com.manageplat.service.job;

import com.manageplat.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobInit {

	private static Logger logger = LoggerFactory.getLogger(JobInit.class);
	
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