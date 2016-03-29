package com.manageplat.service.job;

import com.manageplat.model.job.JobInfo;
import com.manageplat.service.job.hander.MonitJobHander;
import com.manageplat.util.SpringContextUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitJob.class);

	private MonitJobHander monitJobHander = SpringContextUtils.getBean("monitJobHander", MonitJobHander.class);
	
	private JobInfo jobInfo;

	public MonitJob() {

	}

	public MonitJob(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
        
		// 获取参数
		if (context != null) {
			JobDataMap map = context.getJobDetail().getJobDataMap();
			jobInfo = (JobInfo) map.get("job");
			
			//应用quartz驱动的任务 需要检查任务是否是已驱动的
			boolean isDrived =JobManager.getInstance().hasDrivedJob(jobInfo,1);
	
			if(!isDrived)
			{
				logger.error("MonitJob execute monit job,jobName=" + jobInfo.getJobName()+ " not start,because job current is not drived,"+System.currentTimeMillis());
				return;
			}
		}

		if(jobInfo==null)
		{
			logger.error("MonitJob execute jobInfo is null");
			return ;
		}
		
		monitJobHander.executeJob(this);
	}
}