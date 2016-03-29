package com.task.jobs;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class WebRequestJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(WebRequestJob.class);

	@Override
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		final JobDataMap map = ctx.getMergedJobDataMap();
		final String url = map.getString("url");
		final String auth = map.getString("auth");
		final String name = map.getString("name");
		final String password = map.getString("password");

		HttpClient client = new HttpClient();
		//设置请求超时三秒
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		GetMethod get = new GetMethod(url);
		if (StringUtils.isNotBlank(auth) && Boolean.valueOf(auth)) {
			client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(name, password));
			get.setDoAuthentication(true);
		}		
		try {
			int status = client.executeMethod(get);
			logger.info("WgetTask :" + " request url: [" + url + "],return: " + status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			get.releaseConnection();
		}
	}

}
