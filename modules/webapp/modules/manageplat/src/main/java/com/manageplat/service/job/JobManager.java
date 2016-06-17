package com.manageplat.service.job;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.web.BaseResponse;
import com.manageplat.service.job.executor.JobExecuteFactory;
import com.manageplat.service.job.filter.DefaultJobFilter;
import com.manageplat.service.job.filter.IJobFilter;
import com.manageplat.util.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 任务管理器
 *
 */
public class JobManager{
	
	 private static Logger logger = Logger.getLogger(JobManager.class);
	 
	 private static JobManager inst = new JobManager();
	
	 public static boolean autoStart=true;//是否自动启动所有可驱动的任务
	 
	 public static final int heartTime = 2*60;
	 
	 private List<JobInfo> jobList = new ArrayList<JobInfo>();
	 
	 //已驱动的工作列表	 
	 private Map<Integer,JobInfo> drivedJobMap = new ConcurrentHashMap<Integer,JobInfo>();
	 
	 //过滤列表
	 private List<IJobFilter> filters = new CopyOnWriteArrayList<IJobFilter>();
	 
	 //执行者名字
	 private String name="";
	 
	 private JobService jobService = SpringContextUtils.getBean("jobService", JobService.class);
	 
	 public static JobManager getInstance()
	 {
		return inst;
	 }
		
	 private JobManager()
	 {
		 try
	     {
		     name=InetAddress.getLocalHost().getHostAddress();
		 }
	     catch (UnknownHostException e)
	     {
	    	 
	     }
		 if(StringUtils.isBlank(name))
		 {
		    name =UUID.randomUUID().toString();
		 }
		 name = ((name.length()>15)? name.substring(0,15):name);
		 
		 initFilters();
	 }
	 
	 public List<JobInfo> getJobList() {
		return jobList;
	 }
	 
	 public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JobService getJobService() {
		return jobService;
	}

	public Map<Integer, JobInfo> getDrivedJobMap() {
		return drivedJobMap;
	}

	public void setDrivedJobMap(Map<Integer, JobInfo> drivedJobMap) {
		this.drivedJobMap = drivedJobMap;
	}

	public void initFilters()
	{
		 filters.clear();
		 //过滤jog
	     List<String> allowStatus= new ArrayList<String>();
	     allowStatus.add("正常");
	     List<String> allowTypes = new ArrayList<String>();
	     allowTypes.add("http");
	     
	     IJobFilter filter = new DefaultJobFilter(allowStatus,allowTypes);
	     filters.add(filter);
	}
	
	 /**
	  * 过滤job
	  */
	 public JobInfo filter(JobInfo jobInfo)
	 {
		 if(filters==null || filters.size()<=0)
		 {
			 return jobInfo;
		 }
		 for(IJobFilter filter:filters)
		 {
			 JobInfo info = filter.filter(jobInfo);
			 if(info==null)
			 {
				 return info;
			 }
		 }
		 return jobInfo;
	 }
	 
	 /**
	  * 添加job
	  * @return
	  */
	 public boolean addJob(JobInfo info)
	 {
		 if(jobList.contains(info))
		 {
			 jobList.remove(info);
		 }
		 jobList.add(info);
		 
		 boolean rt= driveJob(info);
		
		 if(rt)
		 {
			 JobMonitor.getInstance().addMonitJob(info);
		 }
		 
		 logger.error("JobManager addJob job id="+info.getId()+",rt="+rt+",thread="+Thread.currentThread().getName());
		 
		 return rt;
	 }
	 
	 /**
	  * 驱动job
	  */
	 public boolean driveJob(JobInfo jobInfo,boolean needLock)
	 {
         JobInfo info=filter(jobInfo);
	     
	     if(info ==null)
	     {
	    	 logger.error("JobManager driveJob job="+jobInfo+",not pass filter");
	    	 return false;
	     }
	     if(needLock)
	     {
	    	BaseResponse lock= jobService.doLuckLock(info);
	    	if(lock.getError()==null || 0!=lock.getError().intValue())
	    	{
	    		logger.error("JobManager driveJob job="+info+",not get qidong lock");
	    		return false;
	    	}
	     }
		 boolean rt= JobExecuteFactory.executeJob(info);
    	 if(rt)
    	 {
    		 drivedJobMap.put(info.getId(), info);
    	 }
 		 logger.error("JobManager driveJob job="+info+",rt="+rt);
 		 return rt;
	 }
	 
	 /**
	  * 驱动job
	  */
	 public boolean driveJob(JobInfo info)
	 {
         return driveJob(info,false);
	 }
	 
	 /**删除job**/
	 public boolean delJob(int jobId)
	 {   
		 boolean rtn=true;
		 JobInfo info = drivedJobMap.get(jobId);
		 if(info!=null)
		 {
			 rtn= JobExecuteFactory.delJob(info);
			 if(rtn)
			 {
				 drivedJobMap.remove(jobId);
				 JobMonitor.getInstance().delMonitJob(jobId);
			 }
			 logger.error("JobManager delJob job="+info+",rt="+rtn);
		 }
		 
		 logger.error("JobManager delJob job id="+jobId+",rt="+rtn+",thread="+Thread.currentThread().getName());
		 
		 return rtn;
	 }
	 
	 /**
	  * 获取所有的驱动的工作
	  */
	 public  Map<Integer,JobInfo> getPerDrivedJobs()
	 {
		 return drivedJobMap;
	 }
	 
	 /**判断任务是否已驱动**/
	 public boolean hasDrivedJob(JobInfo info)
	 {
		 return hasDrivedJob(info,2);
	 }
	 
	 /**
	  * 判断任务是否已驱动
	  * @param info
	  * @param flag  0---不管状态  1---回查数据库 2---不回查数据库
	  * @return
	  */
	 public boolean hasDrivedJob(JobInfo info,int flag)
	 {
		if(info==null || info.getId()==null)
		{
			return false;
		}
		if(flag==0)
		{
			return false;
		}
		if(StringUtils.isBlank(info.getRuner()) || flag==1  )
		{
			JobInfo nInfo = jobService.getJobById(info.getId());
			if(nInfo!=null)
			{
				info.setRuner(nInfo.getRuner());
			}
		}
		if(!StringUtils.isBlank(info.getRuner()) && !"over".equals(info.getRuner()))
		{
			return true;
		}
		return false;
		
	 }
	 
	 public JobInfo getDrivedJob(int jobId)
	 {
		 return drivedJobMap.get(jobId);
	 }
	 
	 /**
	  * 比较job与驱动的job 驱动相关信息是否改变
	  * 由于多应用同步一致的处理，需要同步时候都返回true
	  * @return
	  */
	 public boolean hasDriveRelaChange(JobInfo job)
	 {
		 JobInfo drivedJob = getDrivedJob(job.getId());
		 if(drivedJob==null)
		 {
			 return true;
		 }
		 if(!isCanDriveJob(job))
		 {
			 return true;
		 }
		 if(StringUtils.isBlank(job.getType()) || !job.getType().equalsIgnoreCase(drivedJob.getType()) )
		 {
			 return true;
		 }
		 if(StringUtils.isBlank(job.getExpression()) || !job.getExpression().equalsIgnoreCase(drivedJob.getExpression()) )
		 {
			 return true;
		 }
		 if("http".equalsIgnoreCase(job.getType()))
		 {
			 if(StringUtils.isBlank(job.getExeUrl()) || !job.getExeUrl().equalsIgnoreCase(drivedJob.getExeUrl()) )
			 {
				 return true;
			 }
		 }
	     return false;
	 }
	 
	 /**
	  * 获取所有载入的工作
	  */
	 public List<JobInfo>  getAllJob()
	 {
		 return jobList;
	 }
	 
	 /**
	  * 是否是可驱动的job
	  */
	 public boolean isCanDriveJob(JobInfo jobInfo)
	 {
		JobInfo info=filter(jobInfo);
		if(info==null)
		{
			return false;
		}
		return true;
	 }
	 
	 /**
	  * 是否可执行，只是执行，不需要驱动
	  */
	 public boolean isCanRunJob(JobInfo jobInfo)
	 {
		 String oldExp = jobInfo.getExpression();
		 if(StringUtils.isBlank(oldExp))
		 {
			 jobInfo.setExpression("0 0 0 0 0");
		 }
		 boolean rt = isCanDriveJob(jobInfo);
		 jobInfo.setExpression(oldExp);
		 return rt;
	 }
		
}
