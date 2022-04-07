package com.manageplat.service.job;

import com.manageplat.model.job.JobInfo;
import com.manageplat.service.job.executor.MonitJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 任务监控器
 */
public class JobMonitor {
	
	private static JobMonitor inst = new JobMonitor();
	
	private static Logger logger = LoggerFactory.getLogger(JobMonitor.class);
	
	private Map<Integer,JobInfo> monitJobMap = new ConcurrentHashMap<Integer,JobInfo>();
    
	public static JobMonitor getInstance()
	{
		return inst;
	}
	
	private JobMonitor()
	{
		
	}
	 
	/**
	  * 添加监控任务
	  * @return
	  */
	 public boolean addMonitJob(JobInfo info)
	 {
		 boolean rt= new MonitJobExecutor(info).execute();
    	 if(rt)
    	 {
    		 monitJobMap.put(info.getId(), info);
    	 }
 		 logger.error("JobMonitor addMonitJob job="+info+",rt="+rt);
 		 return rt;
	 }
	 
	 /**删除监控任务b**/
	 public boolean delMonitJob(int jobId)
	 {   
		 boolean rtn=true;
		 JobInfo info = monitJobMap.get(jobId);
		 if(info!=null)
		 {
			 rtn= new MonitJobExecutor(info).del();
			 if(rtn)
			 {
				 monitJobMap.remove(jobId);
			 }
			 logger.error("JobMonitor delMonitJob job="+info+",rt="+rtn);
		 }
		 
		 return rtn;
	 }

	public Map<Integer, JobInfo> getMonitJobMap() {
		return monitJobMap;
	}
	
}
