package com.manageplat.service.job.hander;

import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobHttpUrl;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.web.BaseResponse;
import com.manageplat.model.vo.web.JobResponse;
import com.manageplat.service.job.*;
import com.util.http.UrlUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class HttpJobHandler {

	public static final Logger logger = LoggerFactory.getLogger(HttpJobHandler.class);

	private static final ExecutorService executors = Executors.newCachedThreadPool();

	private static Timer timer = new Timer();

	@Autowired
	private PretendLoginService pretendLoginService;

	@Autowired
	private JobService jobService;

	public void executeJob(HttpJob job) {
		
		executors.execute(new JobThread(job));
	}

	private class JobThread implements Runnable {

		private JobInfo jobInfo;

		private int returnStatus;

		private String returnValue;

		private long beginTime;

		private JobHttpUrl jobUrl;

		private boolean logined = false;

		// 登陆态cookie
		private String cookieValue;

		// 是否独立执行
		private boolean standalone = false;

		private HttpJob httpJob;

		public JobThread(HttpJob httpJob) {

			this.httpJob = httpJob;
			this.jobInfo = httpJob.getJobInfo();
			this.standalone = httpJob.isStandalone();
		}

		public void run() {
			// 获取参数
			logger.error("HttpJobHandler execute begin,jobName="+ jobInfo.getJobName() + ",url=" + jobInfo.getExeUrl());
			long begin = System.currentTimeMillis();
			beginTime = begin / 1000;

			BaseResponse pass = new BaseResponse(0);
			if (!standalone) {
				pass = checkExe();
			}
			if (pass.getError() == null || 0 != pass.getError().intValue()) {
				JobExecute jobExecute = new JobExecute();
				jobExecute.setState(2);//未执行--2
				jobExecute.setStartTime(new Long(beginTime).intValue());
				jobExecute.setEndTime(new Long(System.currentTimeMillis() / 1000).intValue());
				jobExecute.setJobId(jobInfo.getId());
				jobExecute.setJobName(jobInfo.getJobName());
				if (1 == pass.getError().intValue()) {
					jobExecute.setNote("任务已被另一进程执行或修改");
				}
				if (2 == pass.getError().intValue()) {
					String msg = pass.getMsg();
					if (!StringUtils.isBlank(msg) && msg.length() > 150) {
						msg = msg.substring(0, 150);
					}
					jobExecute.setNote(msg);
				}
				jobService.recordActBegin(jobExecute);
				return;
			}

			String exeUrl = jobInfo.getExeUrl();

			jobUrl = new JobHttpUrl(exeUrl);

			Map<String, String> loginRt = null;
			// 需要登陆
			if (jobUrl.isNeedLogin()) {
				loginRt = pretendLoginService.login(jobUrl);
				logger.error("HttpJobHandler before execute,login "+ loginRt.get("error") + " " + loginRt.get("msg"));
				if (!"0".equals(loginRt.get("error"))) {
					// 登陆失败;
					JobExecute jobExecute = new JobExecute();
					jobExecute.setState(3);//登陆失败--3
					jobExecute.setStartTime(new Long(beginTime).intValue());
					jobExecute.setEndTime(new Long(
							System.currentTimeMillis() / 1000).intValue());
					jobExecute.setJobId(jobInfo.getId());
					jobExecute.setJobName(jobInfo.getJobName());
					String msg = loginRt.get("msg");
					if (!StringUtils.isBlank(msg)) {
						int length = ((msg.length() > 150) ? 150 : msg.length());
						jobExecute.setNote(msg.substring(0, length));
					}
					jobService.recordActBegin(jobExecute);
					return;
				}
				logined = true;
				if (!StringUtils.isBlank(loginRt.get("Cookie"))) {
					cookieValue = loginRt.get("Cookie");
				}
				// 替换敏感参数
				String pwd = jobUrl.getParam("password");
				if (!StringUtils.isBlank(pwd)) {
					exeUrl = exeUrl.replace("password=" + pwd, "_t_now="+ System.currentTimeMillis());
				}
			}

			// 记录开始执行
			int jobExecuteId = recordExecuteInfo();
			if (exeUrl.indexOf("?") >= 0) {
				exeUrl += "&jobExecuteId=" + jobExecuteId;
			} else {
				exeUrl += "?jobExecuteId=" + jobExecuteId;
			}

			// 实际的任务执行
			HttpClient client = JobHttpClient.getClient();
			if(client==null)
			{
				client =new HttpClient();
				client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
				client.getHttpConnectionManager().getParams().setSoTimeout(30000);
			}
			exeUrl = encodeUrl(exeUrl);
			HttpMethod request = new GetMethod(exeUrl);
			request.addRequestHeader("Content-type" , "text/html; charset=utf-8");
			// String cookieStr = request.getHeader("Cookie");
			// String agent = request.getHeader("User-Agent");
			// 登陆态
			if (!StringUtils.isBlank(cookieValue)) {
				request.addRequestHeader("Cookie", cookieValue);
			}
			try {
				returnStatus=client.executeMethod(request);
				// if (HttpStatus.SC_OK == returnStatus) {
				String traceJobs = jobService.getTraceJob();
				boolean needTrace=false;
				if(!StringUtils.isBlank(traceJobs))
				{
					String ts [] = traceJobs.split(",");
					List<String> tsArray = Arrays.asList(ts);
					if(tsArray.contains(""+jobInfo.getId()))
					{
						needTrace=true;
					}
				}
				if(needTrace)
				{
					 returnValue=request.getResponseBodyAsString();
					 logger.error("HttpJobHandler do job return,returnStatus=" + returnStatus + ",returnBody=" + returnValue);
					 JobExecute jobExecute = new JobExecute();
					 jobExecute.setId(jobExecuteId);
					 jobExecute.setState(returnStatus);//http-return code
					 
					 int length =(returnValue==null)?0:returnValue.length();
                     length=(length>100)?100:length;
                     if(length>0) {
                         jobExecute.setNote(returnValue.substring(0, length));
                     }
					 jobService.uptJobExecute(jobExecute);
				}
			} catch (Exception e) {
				logger.error("HttpJobHandler execute htttclient  url=" + exeUrl, e);
			} finally {
				request.releaseConnection();
			}

			logger.info("HttpJobHandler execute end,jobName=" + jobInfo.getJobName()
					+ ",status=" + returnStatus + ",value=" + returnValue
					+ ",cost=" + (System.currentTimeMillis() - begin));
			// 回调 记录执行结果
			callBack();
		}

		public BaseResponse checkExe() {

			BaseResponse lock = jobService.doLuckLock(jobInfo);
			if (lock.getError() == null || 0 != lock.getError().intValue()) {
				logger.info("HttpJobHandler do job=" + jobInfo + ",bug jobInfo changeed ,not get exe lock");
				// 重启动
				timer.schedule(new TimerTask() {
					public void run() {
						try{
							JobInfo info = jobService.getJobById(jobInfo.getId());
							//驱动相关信息没改变,那就是运行状态变了，被别的进程运行或启动了
							if (!JobManager.getInstance().hasDriveRelaChange(info)) {
								jobInfo.setRunStatus(info.getRunStatus());
							} else {
								logger.info("HttpJobHandler restart job " + info.getId() + ", " + info.getJobName());
								// 重启
								jobService.endJob(info);
								jobService.startJob(info);
								logger.info("HttpJobHandler restart end job " + info.getId());
							}
						}catch(Exception e)
						{
							logger.error("HttpJobHandler timer schedule error,",e);
						}
					}
				}, 3 * 1000);// 3秒后
			}
			return lock;
		}

		public int recordExecuteInfo() {
			int jobExeId = 0;
			JobExecute jobExecute = new JobExecute();
			jobExecute.setState(4);//定时到点执行---4
			jobExecute.setStartTime(new Long(beginTime).intValue());
			jobExecute.setJobId(jobInfo.getId());
			jobExecute.setJobName(jobInfo.getJobName());
			jobExecute.setIp(JobManager.getInstance().getName());
			JobResponse rsp = jobService.saveJobExecute(jobExecute);
			if (rsp != null && rsp.getError() != null && 0 == rsp.getError() && rsp.getKey() != null)
			{
				jobExeId = rsp.getKey();
			}
			return jobExeId;
		}

		public void callBack() {
			// 执行后，保存结果到表中
			// JobExecute jobExecute = new JobExecute();
			// jobExecute.setExecuteStatus(String.valueOf(returnStatus));
			// jobExecute.setStartTime(new Long(beginTime).intValue());
			// jobExecute.setEndTime(new Long(endTime).intValue());
			// jobExecute.setJobId(jobInfo.getId());
			// jobExecute.setJobName(jobInfo.getJobName());
			// if(!StringUtils.isBlank(returnValue))
			// {
			// int length =(
			// (returnValue.length()>150)?150:returnValue.length());
			// jobExecute.setNote(returnValue.substring(0,length));
			// }
			// jobService.recordActBegin(jobExecute);

			// 之前登陆，执行完后登出,
			// 异步实现后，任务可能还在跑，别登出
//			if (logined) {
//				Map<String, String> logoutRt = pretendLoginService.logout(jobUrl, cookieValue);
//				logger.error("HttpJobHandler after execute,logout "+ logoutRt.get("error") + " " + logoutRt.get("msg"));
//			}
		}

		public String encodeUrl(String url) {
			try {
				String prefix = url;
				if (prefix.indexOf("?") > 0) {
					prefix = prefix.substring(0, prefix.indexOf("?"));
					Map<String, String> params = UrlUtil.getParamMap(url);
					String joinStr = "&";
					StringBuffer buf = new StringBuffer("");
					if (params != null && params.size() > 0) {
						int i = 0;
						for (String key : params.keySet()) {

							if (i == 0) {
								joinStr = "?";
							} else {
								joinStr = "&";
							}
							String v = params.get(key);
							if (!StringUtils.isBlank(v)) {
								v = URLEncoder.encode(v, "UTF-8");
							}
							buf.append(joinStr).append(key).append("=")
									.append(v);

							i++;
						}
					}
					prefix = prefix + buf.toString();
					return prefix;
				} else {
					return url;
				}

			} catch (Exception e) {
				logger.error("HttpJobHandler encodeUrl " + url + " error,", e);
				return url;
			}

		}

	}
}
