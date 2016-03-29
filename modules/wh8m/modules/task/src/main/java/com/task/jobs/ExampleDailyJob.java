package com.task.jobs;

import java.sql.SQLException;

import com.task.util.SpringContextUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ExampleDailyJob extends QuartzJobBean{

	private static Logger logger = LoggerFactory.getLogger(ExampleDailyJob.class);

	private QueryRunner runner = null;

	public ExampleDailyJob() {
		runner = new QueryRunner(SpringContextUtils.getDataSource());
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			String update ="update db_table set rank =1 where id=1";
			int result = runner.update(update);
			logger.info("----------updatesql:" + update +" result:"+result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
