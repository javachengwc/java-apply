package com.task.jobs;

import java.util.concurrent.ThreadPoolExecutor;

import com.task.util.SpringContextUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ServerHealthJob extends QuartzJobBean {

	private static Logger logger = LoggerFactory.getLogger(ServerHealthJob.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		try {
			showExecutorInfo();
		} catch (Exception e) {
			logger.error("showExecutorInfo error,",e);
		}
	}

	private void showExecutorInfo() throws Exception {
		ThreadPoolTaskExecutor taskExecutor = SpringContextUtils.getThreadPoolTaskExecutor();
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		logger.info("----------TaskExecutor Info-----------");
        logger.info("\tActiveCount:" + taskExecutor.getActiveCount());
        logger.info("\tCorePoolSize:" + taskExecutor.getCorePoolSize());
        logger.info("\tMaxPoolSize:" + taskExecutor.getMaxPoolSize());
        logger.info("\tPoolSize:" + taskExecutor.getPoolSize());
		ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        logger.info("\tQueueUseingCapacity:" + threadPoolExecutor.getQueue().size());
        logger.info("\tQueueRemainingCapacity:" + threadPoolExecutor.getQueue().remainingCapacity());
	}
}
