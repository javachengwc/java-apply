package com.manageplat.service.job.filter;


import com.manageplat.model.job.JobInfo;

public interface IJobFilter {
	
	public JobInfo filter(JobInfo info);

}
