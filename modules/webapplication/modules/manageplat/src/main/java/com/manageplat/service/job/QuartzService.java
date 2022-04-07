package com.manageplat.service.job;

import java.util.Date;

import com.manageplat.model.job.JobInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuartzService {
	
	private static Logger logger = Logger.getLogger(QuartzService.class);
	  
	@Autowired
	private Scheduler quartzScheduler;  
	
	private static String trigger_suf="_trigger";//触发器后缀
	  
	/** 
	 * 增加模板解析任务
	 *
	 */
	public boolean addJob(JobInfo jobInfo,Class<? extends Job> jobClass,String specName) throws SchedulerException {
		
		String name = jobInfo.getJobName();
		if(!StringUtils.isBlank(specName))
		{
			name =specName;
		}
		
		//初始化JobDetail  
		JobDataMap dataMap = new JobDataMap(); 
		dataMap.put("create_job_time", DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date())); 
		dataMap.put("job", jobInfo);
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, Scheduler.DEFAULT_GROUP)
				              .withDescription(jobInfo.getId()+"-"+name).usingJobData(dataMap).build();  
		
		//初始化CronTrigger  
		String cronPattern =transExpress(jobInfo.getExpression());
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name + trigger_suf, Scheduler.DEFAULT_GROUP)  
		.forJob(jobDetail).withSchedule(CronScheduleBuilder.cronSchedule(cronPattern)).build();  
		
		quartzScheduler.scheduleJob(jobDetail, trigger);
		logger.error("QuartzService addJob jobInfo="+jobInfo.toString()+",jobClass="+jobClass.getName());
		
		return true;
	}  
	  
	/** 
	* 删除定时任务 
	* @param jobName 
	* @throws SchedulerException 
	*/  
	public boolean deleteJob(String jobName) throws SchedulerException { 
		
	    quartzScheduler.deleteJob(new JobKey(jobName, Scheduler.DEFAULT_GROUP));
	    
	    logger.error("QuartzService deleteJob jobName="+jobName);
	    
	    return true;
	}
	
	/**
	 * 更新定时任务的触发表达式
	 * @param triggerName 触发器名字
	 * @param start 
	 * @return 成功则返回true，否则返回false
	 */
	public boolean startOrStopJob(String triggerName,boolean start) {
		try {
			CronTrigger trigger = (CronTrigger) getTrigger(triggerName,Scheduler.DEFAULT_GROUP);
			if(trigger==null)
			{
				logger.error("QuartzService startOrStopJob getTrigger return null,triggerName="+triggerName);
				return false;
			}
			if(start){
				quartzScheduler.resumeTrigger(new TriggerKey(triggerName,Scheduler.DEFAULT_GROUP));
				logger.error("QuartzService startOrStopJob trigger "+ triggerName+" start successfully!");
			}else{
				quartzScheduler.pauseTrigger(new TriggerKey(triggerName,Scheduler.DEFAULT_GROUP));
				logger.error("QuartzService startOrStopJob trigger "+ triggerName+" pause successfully!");
			}
			return true;
			
		}  catch (SchedulerException e) {
			logger.error("QuartzService startOrStopJob error,",e);
			return false;
		}
	}

	/**
	 * 更新定时任务的触发表达式
	 * @return 成功则返回true，否则返回false
	 */
	public boolean updateJob(JobInfo jobInfo,Class<? extends Job> jobClass) {
		String triggerName = jobInfo.getJobName()+trigger_suf;
		try {
			
			CronTrigger trigger = (CronTrigger) getTrigger(triggerName,Scheduler.DEFAULT_GROUP);
			if (trigger == null) {
				return false;
			}
			if (StringUtils.equals(trigger.getCronExpression(), jobInfo.getExpression())) {
				logger.error("QuartzService updateJob cronExpression is same with the running Schedule , no need to update.");
				return true;
			}
			boolean delRt =deleteJob(jobInfo.getJobName());
			if(!delRt)
			{
				logger.error("QuartzService updateJob deleteJob  "+jobInfo.getJobName()+" fail!");
				return false;
			}
			boolean rt =addJob(jobInfo,jobClass,null);
			logger.error("QuartzService updateJob trigger "+triggerName+" ,cromExpression="+jobInfo.getExpression()+" result="+rt);
			return rt;
		} catch (Exception e) {
			logger.error("QuartzService updateJob error, triggerName="+triggerName+",cronExpression="+jobInfo.getExpression() + e);
			return false;
		}
	}
	
	/**
	 * 更新或添加定时任务
	 * @return 成功则返回true，否则返回false
	 */
	public boolean updateOrAddJob(JobInfo jobInfo,Class<? extends Job> jobClass) {
		
		return updateOrAddJob(jobInfo,jobClass,null);
	}
	
	public boolean updateOrAddJob(JobInfo jobInfo,Class<? extends Job> jobClass,String specName) {
		
		String name = jobInfo.getJobName();
		if(!StringUtils.isBlank(specName))
		{
			name = specName;
		}
		
		String triggerName = name+trigger_suf;
		
		try {
			boolean del=false;
			boolean add=true;
			boolean rt =true;
			JobInfo oldInfo=null;
			CronTrigger trigger = (CronTrigger) getTrigger(triggerName,Scheduler.DEFAULT_GROUP);
			
			if(trigger!=null)
			{
				add=false;
				JobDataMap map =trigger.getJobDataMap();
				oldInfo = (JobInfo)map.get("job");
			}
			if (trigger!=null && !StringUtils.equals(trigger.getCronExpression(), jobInfo.getExpression())) {

				del=true;
			}
			if (trigger!=null && oldInfo!=null && !StringUtils.equals(oldInfo.getExeUrl(), jobInfo.getExeUrl())) {

				del=true;
			}
			if(del)
			{
				boolean delRt =deleteJob(name);
				if(!delRt)
				{
					logger.error("QuartzService updateOrAddJob invoke deleteJob  "+name+" fail!");
					return false;
				}
				add=true;
			}
			if(add)
			{
				rt =addJob(jobInfo,jobClass,name);
			}
			logger.error("QuartzService updateOrAddJob trigger "+triggerName+" ,cromExpression="+jobInfo.getExpression()+",do del="+del+",result="+rt);
			return rt;
		} catch (Exception e) {
			logger.error("QuartzService updateOrAddJob error, triggerName="+triggerName+",cronExpression="+jobInfo.getExpression() + e);
			return false;
		}
	}
	
	/**
	 * 做废
	 * 更新定时任务的触发表达式
	 * @param triggerName 触发器名字
	 * @param cronExpression 触发表达式
	 * @return 成功则返回true，否则返回false
	 */
	public boolean updateCronExpression(String triggerName,String cronExpression) {
		try {
			CronTrigger trigger = (CronTrigger) getTrigger(triggerName,Scheduler.DEFAULT_GROUP);
			if (trigger == null) {
				return false;
			}
			if (StringUtils.equals(trigger.getCronExpression(), cronExpression)) {
				logger.error("QuartzService updateCronExpression cronExpression is same with the running Schedule , no need to update.");
				return true;
			}
			//trigger.setCronExpression(cronExpression);
			//quartzScheduler.rescheduleJob(new TriggerKey(triggerName,Scheduler.DEFAULT_GROUP),trigger);
			logger.error("QuartzService update trigger "+triggerName+" ,cromExpression="+cronExpression+" successfully!");
			return true;
		} catch (Exception e) {
			logger.error("Fail to reschedule. " + e);
			return false;
		}
	}

	/**
	 * 获取触发器
	 * @param triggerName 触发器名字
	 * @param groupName 触发器组名字
	 * @return 对应Trigger
	 */
	private Trigger getTrigger(String triggerName, String groupName) {
		Trigger trigger = null;
		if (StringUtils.isBlank(groupName)) {
			logger.error("QuartzService getTrigger Schedule Job Group is empty!");
			return null;
		}
		if (StringUtils.isBlank(triggerName)) {
			logger.error("QuartzService getTrigger Schedule trigger Name is empty!");
			return null;
		}
		try {
			trigger = quartzScheduler.getTrigger(new TriggerKey(triggerName,groupName));
		} catch (SchedulerException e) {
			logger.error("QuartzService getTrigger Fail to get the trigger (triggerName: " + triggerName
					+ ", groupName : " + groupName + ")");
			return null;
		}
		if (trigger == null) {
			logger.error("QuartzService getTrigger can not found the trigger of triggerName: "
					+ triggerName + ", groupName : " + groupName);
		}
		return trigger;
	}

	/**
	 * 定时任务时间表达式转换 crontab--->quartz
	 * 星期天的转换 cron 0----quartz 7
	 * 增加秒数 cron 没秒 quartz 有秒
	 * @param express
	 * @return
	 */
	public static String transExpress(String express)
	{
		int d = express.lastIndexOf(" ");
		String lastStr = express.substring(d+1);
		lastStr = lastStr.replace("0", "7");//星期天的转换
		lastStr = lastStr.replace("*", "?");//星期转成?号
		String rt = "0 "+express.substring(0,d+1)+lastStr;
		return rt;
	}
}
