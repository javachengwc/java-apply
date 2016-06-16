package com.manageplat.service.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.manageplat.dao.job.JobExecuteDao;
import com.manageplat.dao.job.JobInfoDao;
import com.manageplat.model.job.JobDrive;
import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.job.JobQueryVo;
import com.manageplat.model.vo.web.BaseResponse;
import com.manageplat.model.vo.web.JobResponse;
import com.manageplat.util.MemcachedUtil;
import com.util.date.SysDateTime;
import com.util.slice.QuerySliceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {
	
	private static Logger logger = Logger.getLogger(JobService.class);

    @Autowired
    private JobInfoDao jobInfoDao;

    @Autowired
    private JobExecuteDao jobExecuteDao;
	
	/**
	 * 创建任务
	 */
    public JobResponse saveJob(JobInfo job) {
        //判断任务名是否重读
        JobInfo joba =getJob(job.getJobName());
        int jobId= (joba==null)?0:joba.getId();
        if(jobId>0){
            return new JobResponse(1,"已经存在同名任务");
        }
        if(!StringUtils.isBlank(job.getParentJobName()) &&  (job.getParentId()==null || job.getParentId()<=0))
        {
            JobInfo parentJob =getJob(job.getParentJobName());
        	int parentJobId =  (parentJob==null)?0:parentJob.getId();
        	if(parentJobId<=0)
        	{
        		return new JobResponse(1,"不存在"+job.getParentJobName()+"的任务");
        	}else
        	{
        		job.setParentId(parentJobId);
        	}
        }
        
        JobResponse rsp = insertJob(job);
        if(0==rsp.getError())
        {
        	job.setId(rsp.getKey());
        	JobResponse startRsp = startJob(job);
        	rsp.setDrive_result(startRsp.getError());
        	rsp.setDrive_info(startRsp.getMsg());
        	rsp.setDriver(startRsp.getDriver());
        }
        return rsp;
    }

    /**
     * 插入任务记录
     */
    public JobResponse insertJob(JobInfo job) {
        int rt =jobInfoDao.insert(job);
        boolean isSuccess = (rt==1)?true:false;
        Integer genId=job.getId();
        if (isSuccess) {
            return new JobResponse(0, "保存成功",genId );
        } else {
            return new JobResponse(1, "保存失败");
        }
    }
    
    /**
     * 修改任务
     */
    public JobResponse changeJob(JobInfo job)
    {
    	if(!StringUtils.isBlank(job.getParentJobName()) &&  (job.getParentId()==null || job.getParentId()<=0))
        {
            JobInfo parentJob =getJob(job.getParentJobName());
        	int parentJobId =  (parentJob==null)?0:parentJob.getId();
        	if(parentJobId<=0)
        	{
        		return new JobResponse(1,"不存在"+job.getParentJobName()+"的任务");
        	}else
        	{
        		job.setParentId(parentJobId);
        	}
        }
    	boolean rt = updateJob(job);
    	if(rt)
    	{
    		return dealJobDrive(job);
    	}
    	else
    	{
    		return new JobResponse(1,"处理失败");
    	}
    }
    
    /**
     * 任务修改后的相关处理
     */
    public JobResponse dealJobDrive(JobInfo job)
    {
    	JobResponse rsp = new JobResponse(0,"处理成功");
		
		JobInfo newestJob =jobInfoDao.getJobById(job.getId());
		
		//本没驱动,且不可驱动
		if(!JobManager.getInstance().hasDrivedJob(newestJob) && !JobManager.getInstance().isCanDriveJob(newestJob))
		{
			return rsp;
		}
        //已驱动,但驱动相关信息没改变
		if(JobManager.getInstance().hasDrivedJob(newestJob) && !JobManager.getInstance().hasDriveRelaChange(newestJob))
		{
			return rsp;
		}
		
		BaseResponse lock= doLuckLock(newestJob);
		int i=0;
    	while( (lock.getError()==null || 0!=lock.getError().intValue()) && i<3)
    	{
    		try{
    		    Thread.sleep(5*1000l);
    		    newestJob = jobInfoDao.getJobById(newestJob.getId());
    		}catch(Exception e)
    		{
    			
    		}
    		lock= doLuckLock(newestJob);
    		i++;
    	}
        
		//需重启任务
    	if(JobManager.getInstance().hasDrivedJob(newestJob) )
		{
			JobResponse jobRsp =endJob(newestJob);
			rsp.setDrive_info(jobRsp.getMsg());
		}
		if(JobManager.getInstance().isCanDriveJob(newestJob))
		{
    		JobResponse startRsp = startJob(newestJob);
        	rsp.setDrive_result(startRsp.getError());
        	String pre ="";
        	if(!StringUtils.isBlank(rsp.getDrive_info()))
        	{
        		pre = rsp.getDrive_info()+",";
        	}
        	rsp.setDrive_info(pre+startRsp.getMsg());
        	rsp.setDriver(startRsp.getDriver());	
		}
		return rsp;
    }
    
    /**
     * 更新任务记录
     */
    public boolean updateJob(JobInfo job)
    {
        jobInfoDao.updateSelectiveById(job);
		return true;
    }
    
    /**
     * 改变任务状态
     */
    public JobResponse changeJobStatus(JobInfo job)
    {
    	boolean rt = updateJob(job);
    	if(rt)
    	{
    		return dealJobDrive(job);
    	}
    	else
    	{
    		return new JobResponse(1,"修改任务状态失败");
    	}
    }

    /**
     * 通过名称获取任务
     */
    public JobInfo getJob(String jobName) {
        return jobInfoDao.getJobByName(jobName);
    }

    /**
     * 记录任务到点执行动作开始信息
     */
    public BaseResponse recordActBegin(JobExecute jobExecute)
    {
    	 if(jobExecute.getJobId()<=0){
            JobInfo job =getJob(jobExecute.getJobName());
            int jobId = (job==null)?0:job.getId();
            jobExecute.setJobId(jobId);
            if(jobId<=0)
            {
            	return new BaseResponse(1,"不存在名称为:"+jobExecute.getJobName()+"的任务");
            }
    	 }
    	 if(StringUtils.isBlank(jobExecute.getIp()))
    	 {
    		 jobExecute.setIp(JobManager.getInstance().getName());
    	 }
        JobResponse rsp  =saveJobExecute(jobExecute);
        return rsp;
    }
    
    /**
     * 记录任务到点执行动作结束信息
     */
    public BaseResponse recordActOver(JobExecute jobExecute)
    {
    	BaseResponse rsp= uptJobExecute(jobExecute);
    	if(jobExecute.getResult()!=null && 0==jobExecute.getResult())
    	{
    		//启动子任务
    		JobInfo job =queryJobByExecuteId(jobExecute.getId());
    		if(job!=null)
    		{
    			 List<JobInfo> subList =querySubJob(job.getId());
    			 logger.error("JobService recordActOver begin do sub job,parent jobId="+
    			              job.getId()+", sub job size="+ ((subList==null)?"0":subList.size()) );
    			 Map<String,Map<String,String>> map =doSubJob(subList);
    			 JobResponse rp = new JobResponse(rsp.getError(),rsp.getMsg());
    			 rp.setSubJobInfo(map);
    			 
    			 return rp;
    		}
    		else
    		{
    			logger.error("JobService recordActOver "+jobExecute.getId()+", job info is null");
    		}
    	}
        return rsp;
    }
    
    /**
     * 开始任务
     */
    public JobResponse startJob(int jobId)
    {
    	if(jobId<=0)
    	{
    		return new JobResponse(1,"任务不存在");
    	}
    	JobInfo info = jobInfoDao.getJobById(jobId);
    	return startJob(info);
    }
    
    
    public JobResponse startJob(JobInfo jobInfo)
    {
    	if(jobInfo==null)
    	{
    		return new JobResponse(1,"任务不存在");
    	}
    	
		//如果是可驱动任务，驱动任务
        JobResponse driveRsp = syncDriveJob(jobInfo);
        
        if(JobManager.getInstance().isCanDriveJob(jobInfo))
		{
        	
        	//日志
	        JobDrive driveInfo= generateStartInfo(jobInfo);
	        driveInfo.setResult(driveRsp.getError());
	        String os=System.getProperty("os.name","Linux");
	        String ov=System.getProperty("os.version","");
	        if(!StringUtils.isBlank(os))
	        {
	        	os= (os.length()>10)?os.substring(0,10):os;
	        }
	        driveInfo.setNote(os+"|"+ov+"|"+driveRsp.getMsg());
	        saveJobDrive(driveInfo);
	        
	        driveRsp.setDriver(driveInfo.getDriver());
		}
        if(driveRsp.getError()!=null && 0==driveRsp.getError())
        {
        	jobInfo.setRunTime(SysDateTime.getNow());
        	jobInfo.setRuner("runing");
        	jobInfoDao.updateSelectiveById(jobInfo);
        }
        return driveRsp;
    }
    
    public JobDrive generateStartInfo(JobInfo jobInfo)
	{
		JobDrive driveInfo = new JobDrive();
		driveInfo.setJobId(jobInfo.getId());
		driveInfo.setOptFlag(0);
		driveInfo.setRecordTime(SysDateTime.getNow());
		driveInfo.setDriver(JobManager.getInstance().getName());
		
		return driveInfo;
	}
	
	public JobDrive generateEndInfo(JobInfo jobInfo)
	{
		JobDrive driveInfo = new JobDrive();
		driveInfo.setJobId(jobInfo.getId());
		driveInfo.setOptFlag(1);
		driveInfo.setRecordTime(SysDateTime.getNow());
		driveInfo.setDriver(JobManager.getInstance().getName());
		return driveInfo;
	}
    
    /**
     * 保存执行时间点的行动作信息
     */
    public JobResponse saveJobExecute(JobExecute jobExecute) {
        int rt = jobExecuteDao.insert(jobExecute);
        int genkey=jobExecute.getId();
        if (rt==1) {
            return new JobResponse(0, "保存成功",genkey);
        } else {
            return new JobResponse(1, "保存失败");
        }
    }

    /**
     * 取消任务
     */
    public JobResponse endJob(int jobId)
    {
    	if(jobId<=0)
    	{
    		return new JobResponse(1,"任务不存在");
    	}
    	JobInfo info =jobInfoDao.getJobById(jobId);
    	return endJob(info);
    }
    
    /**
     * 取消任务
     */
    public JobResponse endJob(JobInfo jobInfo)
    {
    	if(jobInfo ==null || jobInfo.getId()<=0)
    	{
    		return new JobResponse(1,"任务不存在");
    	}
    	JobResponse jobRsp =null;
    	
    	if(JobManager.getInstance().hasDrivedJob(jobInfo))
    	{
    		boolean rt = JobManager.getInstance().delJob(jobInfo.getId());
    		JobDrive jobDriveInfo =generateEndInfo(jobInfo);
    		String note="";
        	if(rt)
        	{
        		jobDriveInfo.setResult(0);
        		note ="驱动的任务结束成功";
        		jobDriveInfo.setNote(note);
        		jobRsp= new JobResponse(0,note);
        	}else
        	{
        		jobDriveInfo.setResult(1);
        		note ="驱动的任务结束失败";
        		jobDriveInfo.setNote(note);
        		jobRsp= new JobResponse(1,note);
        	}
        	String os=System.getProperty("os.name","Linux");
	        String ov=System.getProperty("os.version","");
	        if(!StringUtils.isBlank(os))
	        {
	        	os= (os.length()>10)?os.substring(0,10):os;
	        }
 	        jobDriveInfo.setNote(os+"|"+ov+"|"+jobDriveInfo.getNote());
        	saveJobDrive(jobDriveInfo);
    	}else
    	{
    		jobRsp= new JobResponse(1,"此任务未驱动!");
    	}
    	if(jobRsp.getError()!=null && 0==jobRsp.getError())
        {
        	jobInfo.setRunTime(0);
        	jobInfo.setRuner("over");
            jobInfoDao.updateSelectiveById(jobInfo);
        	doLuckLock(jobInfo);
        }
    	jobRsp.setDriver(JobManager.getInstance().getName());
    	return jobRsp;
    }
    
    /**
     * 记录任务到点执行动作结束信息
     */
    public BaseResponse uptJobExecute(JobExecute jobExecute) {
    	
    	if(jobExecute.getId()<=0 )
    	{
    		 return new BaseResponse(1, "保存失败,无更新条件");
    	}
        jobExecuteDao.updateSelectiveById(jobExecute);
        return new BaseResponse(0, "保存成功");

    }

    /**
	 * 查询所有任务信息
	 */
    public List<JobInfo> queryAllJobInfo() {

        return QuerySliceUtil.query(new QuerySliceUtil.QueryPage<JobInfo>() {
            @Override
            public List<JobInfo> queryPage(int start, int perSize) {
                JobQueryVo queryVo =new JobQueryVo();
                queryVo.setStart(start);
                queryVo.setRows(perSize);
                return queryJobPage(queryVo);
            }
        },1000);
	}

    /**
     * 分页查询任务信息
     */
    public List<JobInfo> queryJobPage(JobQueryVo queryVo)
    {
        return jobInfoDao.queryPage(queryVo);
    }

	/**
	 * 真正的驱动任务
	 */
	public JobResponse syncDriveJob(JobInfo info)
	{
		if(info==null)
		{
			logger.error("JobService syncDriveJob info is null");
			return new JobResponse(1,"任务为空，无法驱动!");
		}
		if(JobManager.getInstance().hasDrivedJob(info,0))
		{
			return new JobResponse(1, "任务已驱动过!");
		}
		if(!JobManager.getInstance().isCanDriveJob(info))
		{
			return new JobResponse(1, "任务不可驱动!");
		}
		boolean rt = JobManager.getInstance().addJob(info);
		if(rt)
		{
			return new JobResponse(0, "驱动任务成功!");
		}
		else
		{
		    return new JobResponse(1, "驱动任务失败!");
		}
	}
	
	public BaseResponse doLuckLock(JobInfo info)
	{
		String randomStr =UUID.randomUUID().toString();
		String status = ((randomStr.length()>20)? randomStr.substring(0,20):randomStr);
		String oldStatusString = info.getRunStatus();
		int change = 0;
		try{
		     change=uptJobRunStatus(info,status);
		}catch(Exception e)
		{
			logger.error("JobService doLuckLock error,info="+info,e);
			return new BaseResponse(2,"更新执行状态异常,"+e.getClass().getName()+","+e.getMessage());
		}
		if(change<=0)
		{ 
			info.setRunStatus(oldStatusString);
			return new BaseResponse(1);
		}
		return new BaseResponse(0);
	}

	/**
	 * 修改运行状态
	 */
	public int uptJobRunStatus(JobInfo info,String status)  throws Exception{
		String oldStatusString = info.getRunStatus();
		info.setRunStatus(status);
        return jobInfoDao.uptLockRunStatus(status, info.getId(),oldStatusString);
    }
	
	/**
	 * 根据executeId查任务id
	 */
	public JobInfo queryJobByExecuteId(int executeId)
	{
		return  jobExecuteDao.queryJobByExecuteId(executeId);
	}
	
	/**
	 * 启动所有可驱动任务
	 */
	public void startAllDriveJobs()
	{
		
		List<JobInfo> list = queryAllJobInfo();
		if(list!=null && list.size()>0)
        {
        	
        	for(JobInfo jobInfo : list)
    		{
        		if(!JobManager.getInstance().isCanDriveJob(jobInfo))
        		{
        			continue;
        		}
    	        startJob(jobInfo);
    		}
        }
	}

	/**
	 * 获取驱动的任务列表
	 */
	public List<JobInfo> getDriveedJobs()
	{
		List<JobInfo> list = queryAllJobInfo();
		if(list==null || list.size()<=0)
		{
			return null;
		}
		Iterator<JobInfo> it = list.iterator();
		while(it.hasNext())
		{
		    JobInfo info = it.next();
		    if(StringUtils.isBlank(info.getRuner()) || !"runing".equals(info.getRuner()))
		    {
		    	it.remove();
		    }
		}
		return list;
	}
	
	/**
	 * 停止所有可驱动任务
	 */
	public void stopAllDriveJobs()
	{
		List<JobInfo> list = queryAllJobInfo();
        if(list!=null && list.size()>0)
        {
        	for(JobInfo jobInfo : list)
    		{
        		endJob(jobInfo);
    		}
        }
	}
	
    /**
	 * 保存任务驱动信息
	 * @param jobDrive
	 * @return
	 */
	public JobResponse saveJobDrive(JobDrive jobDrive)
	{
        //待实现
		boolean isSuccess = true;
        int genKey=0;
        if (isSuccess) {
            return new JobResponse(0, "保存成功", genKey);
        } else {
            return new JobResponse(1,"description");
        }
	}
	
	/**
	 * 查询子任务列表
	 */
	public List<JobInfo> querySubJob(int id)
	{
		List<JobInfo> list = new ArrayList<JobInfo>();
        //待实现
		return list;
	}
	
	/**
	 * 执行子任务
	 */
	public Map<String,Map<String,String>> doSubJob(List<JobInfo> list)
	{
		if(list==null || list.size()<=0)
		{
			return null;
		}
		Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
		
		for(JobInfo info:list)
		{
			boolean canRun = JobManager.getInstance().isCanRunJob(info);
			if(canRun)
			{
				try
				{
					logger.error("JobService doSubJob start job id="+info.getId());
					
				    new HttpJob(info).execute(null);
				    
				    Map<String,String> subMap = new HashMap<String,String>();
					subMap.put("id",""+info.getId());
					subMap.put("error","0");
					subMap.put("msg", "子任务执行中");
					map.put(info.getJobName(), subMap);
				    
				}catch(Exception e)
				{
					logger.error("JobService doSubJob has exception,job id="+info.getId(),e);
					
					Map<String,String> subMap = new HashMap<String,String>();
					subMap.put("id",""+info.getId());
					subMap.put("error","1");
					subMap.put("msg", "子任务执行异常,"+e.getClass().getName()+","+e.getMessage());
					map.put(info.getJobName(), subMap);
				}
				
			}else
			{
				logger.error("JobService doSubJob is unable do,job id="+info.getId());
				
				Map<String,String> subMap = new HashMap<String,String>();
				subMap.put("id",""+info.getId());
				subMap.put("error","1");
				subMap.put("msg", "子任务不可执行,status="+info.getJobStatus()+",type="+info.getType());
				map.put(info.getJobName(), subMap);
				
			}
		}
		return map;
	}
	
	/**
	 * 直接调用任务目标url
	 */
	public JobResponse directExeTarget(Integer id)
	{
		if(id==null || id<=0)
		{
    		return new JobResponse(1,"任务不存在");
    	}
    	JobInfo info = jobInfoDao.getJobById(id);
    	if(info==null)
    	{
    		return new JobResponse(1,"任务不存在");
    	}
    	if(!StringUtils.isBlank(info.getExeUrl()))
    	{
	    	try
			{
			    new HttpJob(info).execute(null);
			    return new JobResponse(0,"任务目标接口执行中");
			    
			}catch(Exception e)
			{
				logger.error("JobService directExeTarget has exception,",e);
				return new JobResponse(1,"任务目标接口调用异常,"+e.getClass().getName()+","+e.getMessage());
			}
    	}else
    	{
    		return new JobResponse(1,"任务目标url为空");
    	}
	}

    public JobInfo queryJobByIdOrName(Integer id,String jobName)
    {
        JobInfo job = null;
        if(id!=null)
        {
            job=jobInfoDao.getJobById(id);
        }
        if(job==null && !StringUtils.isBlank(jobName))
        {
            job= jobInfoDao.getJobByName(jobName);
        }
        return job;
    }
	
	public Map<Integer,Boolean> hasDrivedJob(Integer id,String jobName)
	{
        JobInfo job =queryJobByIdOrName(id,jobName);
		if(job==null)
		{
			return null;
		}
		boolean a= JobManager.getInstance().hasDrivedJob(job,0);
		boolean b= JobManager.getInstance().hasDrivedJob(job,1);
		boolean c= JobManager.getInstance().hasDrivedJob(job);
		Map<Integer,Boolean> rt = new HashMap<Integer,Boolean>();
		rt.put(0,a);
		rt.put(1,b);
		rt.put(2,c);
		return rt;
	}
	
	/**
	 * 获取任务执行次数
	 */
	public Integer getJobExecuteCount(int jobId,Integer queryStartTime,Integer queryEndTime )
	{
		Integer rt=null;
    	//待实现
		return rt;
	}

	/**
     * 分页查询任务执行记录
     */
    public List<JobExecute> queryJobExecutePage(int jobId,Integer queryStartTime,Integer queryStartOverTime,
    		Integer queryCompareEndTime,int start,int size) {

    	List<JobExecute> list = new ArrayList<JobExecute>();
		//待实现
		return list;
  	}
    
    public boolean updateJobMonitInfo(JobInfo job)
    {
    	//待实现
		return false;
    }

    public void traceJob(int id)
    {
       String key = "trace_job";
  	   String value = ""+id;
  	   String oldValue = MemcachedUtil.get(key);
  		if (!StringUtils.isBlank(oldValue)) {
  			value =value +","+ oldValue;
  		}
  		MemcachedUtil.put(key, value, 1*24*60*60);
    }

    public String getTraceJob()
    {
    	String key = "trace_job";
    	String value = MemcachedUtil.get(key);
    	return value;
    }

 }
