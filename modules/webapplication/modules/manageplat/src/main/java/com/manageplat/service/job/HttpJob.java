package com.manageplat.service.job;

import com.manageplat.model.job.JobInfo;
import com.manageplat.service.job.hander.HttpJobHandler;
import com.manageplat.util.SpringContextUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpJob implements Job {

	public static final Logger logger = LoggerFactory.getLogger(HttpJob.class);

	private JobInfo jobInfo;

	private HttpJobHandler httpJobHandler = SpringContextUtils.getBean("httpJobHandler", HttpJobHandler.class);

	// 是否独立执行
	private boolean standalone = false;

	public HttpJob() {

	}

	public HttpJob(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
		this.standalone = true;
	}

	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	public boolean isStandalone() {
		return standalone;
	}

	public void setStandalone(boolean standalone) {
		this.standalone = standalone;
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
				logger.error("HttpJob execute job,jobName=" + jobInfo.getJobName()+ " not start,because job current is not drived,"+System.currentTimeMillis());
				//JobManager.getInstance().delJob(jobInfo.getId());
				return;
			}
		}

		logger.error("HttpJob execute begin,id="+jobInfo.getId()+",jobName=" + jobInfo.getJobName()+ ",url=" + jobInfo.getExeUrl());

		httpJobHandler.executeJob(this);

	}
}