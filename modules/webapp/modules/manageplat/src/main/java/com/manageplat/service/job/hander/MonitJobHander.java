package com.manageplat.service.job.hander;

import com.manageplat.model.job.JobInfo;
import com.manageplat.service.job.JobExecuteService;
import com.manageplat.service.job.JobMonitService;
import com.manageplat.service.job.JobService;
import com.manageplat.service.job.MonitJob;
import com.util.NumberUtil;
import com.util.date.SysDateTime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MonitJobHander {
	
	public static final Logger logger = LoggerFactory.getLogger(MonitJobHander.class);

	private static final ExecutorService executors = Executors.newFixedThreadPool(20);
    
    public final int DELAY_CHECK_TIME=2*60;//延迟执行时间,1是先等被监控的程序写入记录，2是数据库主从复制需要点时间
	
	public final int QUERY_CHECK_TIME=2*60;//检验查询记录时间范围

	@Autowired
	private JobService jobService;
	
	@Autowired
	private JobMonitService jobMonitService;

    @Autowired
    private JobExecuteService jobExecuteService;

	public void executeJob(MonitJob job) {
		
		executors.execute(new MonitJobThread(job));
	}

	private class MonitJobThread implements Runnable {

		private MonitJob monitJob;

		public MonitJobThread(MonitJob monitJob) {

			this.monitJob = monitJob;
			
		}

		public void run() {
			
			int now = SysDateTime.getNow();
			
			JobInfo jobInfo = monitJob.getJobInfo();

			if(jobInfo==null)
			{
				logger.error("MonitJobHandler execute jobInfo is null");
				return ;
			}
			
			logger.error("MonitJobHandler execute begin,jobId=" + jobInfo.getId()+ ",name=" + jobInfo.getJobName());
			
			try{
				//具体执行
				
				int step=getMinDoStep(jobInfo.getExpression());
				
				long delay =DELAY_CHECK_TIME*1000l;
				
				if(step<=5)
				{  
					delay = delay*step/5;
				}
				
				Thread.sleep(delay);
				
				int start = now-QUERY_CHECK_TIME;
				int end = now +QUERY_CHECK_TIME;

				Integer count = jobExecuteService.queryJobExecuteCount(jobInfo.getId(),start,end);
	    		
	    		//System.out.println(delay+" "+start+" "+end+" count="+count);
	    		//表示未到点执行
	    		if(count==null || count<=0)
	    		{
	    			jobMonitService.recordMonitInfo(jobInfo, now, 1, "存在未正常执行的情况");
	    		}
				
			}catch(Exception e)
			{
				logger.error("MonitJobHandler execute error,",e);
			}
			
			logger.error("MonitJobHandler execute end,jobId="+jobInfo.getId());
			
		}
	}
	
	public int getMinDoStep(String expr)
	{
		int rt=60;
		if(StringUtils.isBlank(expr))
		{
			return rt;
		}
		String exp= expr.split(" ")[0];
		
    	if(!StringUtils.isBlank(exp) && exp.indexOf("/")>0)
    	{
    		String ps [] = exp.split("\\/");
			if(ps.length==2 && NumberUtil.isNumeric(ps[1]))
			{
                rt =Integer.parseInt(ps[1]);
			}
    	}
    	return rt;
	}
}