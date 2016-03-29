package com.manageplat.service.job.executor;

/**
 * 定时任务执行器接口
 *
 */
public interface IJobExecutor {

	public boolean execute();
	
	public boolean del();
	
}
