package com.task.jobs;

import java.sql.SQLException;

import com.task.util.SpringContextUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ExampleJob extends QuartzJobBean {

	private static Logger logger = LoggerFactory.getLogger(ExampleJob.class);

	private QueryRunner runner = null;

	public ExampleJob() {
		runner = new QueryRunner(SpringContextUtils.getDataSource());
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			Object[] objs = (Object[]) runner.query("select count(*) from db_table", new ArrayHandler());
			logger.info("---------->> example job : " + objs[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
