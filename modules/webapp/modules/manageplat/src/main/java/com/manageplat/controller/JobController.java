package com.manageplat.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.job.JobMonit;
import com.manageplat.model.vo.job.JobMonitQueryVo;
import com.manageplat.model.vo.job.JobQueryVo;
import com.manageplat.model.vo.web.BaseResponse;
import com.manageplat.model.vo.web.JobResponse;
import com.manageplat.service.job.JobManager;
import com.manageplat.service.job.JobMonitService;
import com.manageplat.service.job.JobMonitor;
import com.manageplat.service.job.JobService;
import com.util.ReflectionUtil;
import com.util.date.SysDateTime;
import com.util.web.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("job")
public class JobController {
    
	private static Logger logger =  LoggerFactory.getLogger(JobController.class);
	
    @Autowired
    private JobService jobService;
    
    @Autowired
    private JobMonitService jobMonitService;

    @RequestMapping("queryJobPage")
    @ResponseBody
    public Object queryJobPage(JobQueryVo queryVo)
    {
        queryVo.genPage();
        queryVo.genDate();

        List<JobInfo> list = jobService.queryJobPage(queryVo);
        int count = jobService.count(queryVo);

        Map<String,Object> result = new HashMap<String,Object>(2);
        result.put("error", 0);
        result.put("list",list);
        result.put("count",count);
        return result;
    }

    @RequestMapping("queryJobMonitPage")
    @ResponseBody
    public Object queryJobMonitPage(JobMonitQueryVo queryVo)
    {
        queryVo.genPage();

        List<JobMonit> list = jobMonitService.queryPage(queryVo);

        Map<String,Object> result = new HashMap<String,Object>(2);
        result.put("error", 0);
        result.put("data",list);
        return result;
    }

    /**
     * 任务创建的接口
     * 如果是http型任务 其他参数 加到exeUrl里面，同时把includeParam的key值加到exeUrl
     */
	@RequestMapping("createJob")
    @ResponseBody
    public Object createJob(JobInfo job,HttpServletRequest request){
        Map<String,Object> result=new HashMap<String,Object>();
        if(StringUtils.isEmpty(job.getJobName())){
            return makeErrorResponse("1","任务名不能为空",result);
        }
       if(!StringUtils.isBlank(job.getType()) && "http".equalsIgnoreCase(job.getType()) && !StringUtils.isBlank(job.getExeUrl()))
       {
    	   warpJobUrlParam(job,request);
       }

        warpJobInfo(job, request);
        return jobService.saveJob(job);
    }
	
	/**
	 * 修改任务
	 */
	@RequestMapping("changeJob")
    @ResponseBody
    public Object changeJob(JobInfo job,HttpServletRequest request){
		
       Map<String,Object> result=new HashMap<String,Object>();
       if( StringUtils.isBlank(job.getJobName())){
    	   
            return makeErrorResponse("1","任务名称不能为空",result);
       }
       if(job.getId()==null|| job.getId()<=0){
           JobInfo joba =jobService.getJobByName(job.getJobName());
           int jobId= (joba==null)?0:joba.getId();
           if(jobId<=0){
               return new BaseResponse(1,"不存在名称为:"+job.getJobName()+"的任务");
           }
           job.setId(jobId);
       }
       if(!StringUtils.isBlank(job.getType()) && "http".equalsIgnoreCase(job.getType()) && !StringUtils.isBlank(job.getExeUrl()))
       {
    	   warpJobUrlParam(job,request);
       }
       return jobService.changeJob(job);
    }
	
	/**
	 * 更改任务状态，使任务生效或失效
	 */
	@RequestMapping("changeJobStatus")
    @ResponseBody
    public Object changeJobStatus(JobInfo job,HttpServletRequest request){
	   Map<String,Object> result=new HashMap<String,Object>();
       if( StringUtils.isBlank(job.getJobName()) && (job.getId()==null|| job.getId()<=0) ){
    	   
            return makeErrorResponse("1","任务ID,任务名称不能都为空",result);
       }
       if(job.getJobStatus()==null)
       {
    	   return makeErrorResponse("1","任务状态不能为空",result);
       }
       JobInfo oldJob = jobService.queryJobByIdOrName(job.getId(),job.getJobName());
       if(oldJob==null)
       {
           return new BaseResponse(1,"不存在名称为:"+job.getJobName()+"的任务");
       }
       job.setId(oldJob.getId());
       return jobService.changeJobStatus(job);
    }
    
    /**
     * 启动任务
     */
    @RequestMapping("startJob")
    @ResponseBody
    public Object startJob(JobInfo jobInfo,HttpServletRequest request){
        Map<String,Object> result=new HashMap<String,Object>();
        if( StringUtils.isBlank(jobInfo.getJobName()) && (jobInfo.getId()==null|| jobInfo.getId()<=0) ){
            return makeErrorResponse("1","任务ID,任务名称不能都为空",result);
        }
        if(jobInfo.getId()==null||jobInfo.getId()<=0){
            JobInfo joba =jobService.getJobByName(jobInfo.getJobName());
            int jobId= (joba==null)?0:joba.getId();
            if(jobId<=0){
                return createJob(jobInfo,request);
            }else
            {
               jobInfo.setId(jobId);
            }
        }
        return jobService.startJob(jobInfo.getId());
    }
    
    /**
     * 结束任务
     * @param request
     */
    @RequestMapping("endJob")
    @ResponseBody
    public Object endJob(JobInfo jobInfo,HttpServletRequest request){
    	 Map<String,Object> result=new HashMap<String,Object>();
        if( (jobInfo.getId()==null || jobInfo.getId()<=0) && StringUtils.isBlank(jobInfo.getJobName())  ){
            return makeErrorResponse("1","任务ID,任务名称不能都为空",result);
        }
        if(jobInfo.getId()==null || jobInfo.getId()<=0){
            JobInfo joba =jobService.getJobByName(jobInfo.getJobName());
            int jobId= (joba==null)?0:joba.getId();
            if(jobId<=0){
                return new BaseResponse(1,"不存在名称为:"+jobInfo.getJobName()+"的任务");
            }
            jobInfo.setId(jobId);
        }
        logger.info("JobController endJob jobInfo="+jobInfo);
        return jobService.endJob(jobInfo.getId());
    }
    
    /**
     * 记录任务到点执行动作结束信息
     */
    @RequestMapping("recordJobActOver")
    @ResponseBody
    public BaseResponse recordJobActOver(JobExecute jobExecute){

    	logger.error("JobController recordJobActOver start param:"+jobExecute);

    	if( jobExecute.getId()<=0 ){
            return new BaseResponse(1,"任务记录ID不能为空");
        }
        if(jobExecute.getState()==null){
            return new BaseResponse(1,"此次执行结果不能为空");
        }
        
        BaseResponse rsp= jobService.recordActOver(jobExecute);
        
        logger.error("JobController recordActOver end rsp:"+rsp.getError()+" "+rsp.getMsg());
        
        return rsp;
    }

    @RequestMapping("uptJobExeNote")
    @ResponseBody
    public Object uptJobExeNote(JobExecute jobExecute,HttpServletRequest request){
        Map<String,Object> result=new HashMap<String,Object>();
        if(jobExecute.getId()<=0){
            return makeErrorResponse("1","任务记录ID不能为空",result);
        }
        if(StringUtils.isEmpty(jobExecute.getNote())){
            return makeErrorResponse("1","备注不能为空",result);
        }
        //warpJobExecute(jobExecute,request);
        return jobService.uptJobExecute(jobExecute);
    }

    private void warpJobInfo(JobInfo job,HttpServletRequest request) {
        if(StringUtils.isBlank(job.getCreater())){
            job.setCreater("me");
        }
        if(job.getCreateTime()==null){
            job.setCreateTime(SysDateTime.getNow());
        }
        job.setJobStatus(1);//1--正常
        job.setIp(RequestUtil.getIpFromRequest(request));
    }

    /**
     * 给http类型的job组装参数
     */
    @SuppressWarnings("unchecked")
	private void warpJobUrlParam(JobInfo job,HttpServletRequest request) {
    	
    	String url = job.getExeUrl();
 	   
        Enumeration<String> ems =request.getParameterNames();
        List<String> list = ReflectionUtil.getFieldNames(JobInfo.class);
        StringBuffer buf= new StringBuffer("");
        while(ems.hasMoreElements())
        {
     	   String key = ems.nextElement();
     	   if(StringUtils.isBlank(key) || "includeParam".equals(key))
     	   {
     		   continue;
     	   }
     	   if(list.contains(key))
     	   {
     		   continue;
     	   }
     	   if(key.startsWith("add_url_param_"))
    	   {
     		   String realKey=key.replace("add_url_param_", "");
     		   if(!StringUtils.isBlank(realKey))
     		   {
    		       buf.append("&").append(realKey).append("=").append(request.getParameter(key));
     		   }
    		   
    	   }else
    	   {
     	       buf.append("&").append(key).append("=").append(request.getParameter(key));
    	   }
        }
        String includeParam = request.getParameter("includeParam");
        if(!StringUtils.isBlank(includeParam))
        {
     	   String pamKeys [] = includeParam.split(",");
     	   for(String pk:pamKeys)
     	   {
     		   if(!StringUtils.isBlank(pk) && !StringUtils.isBlank(request.getParameter(pk)))
     		   {
     			   buf.append("&").append(pk).append("=").append(request.getParameter(pk));
     		   }
     	   }
        }
        String pa =buf.toString();
        if(!StringUtils.isBlank(pa))
        {
           if(url.indexOf("?")>0)
           {
        	   if(url.endsWith("&"))
        	   {
        		   url=url.substring(0,url.length()-1);
        	   }
        	   
           }else
           {
        	   pa ="?"+pa.substring(1);
           }
           url = url + pa;
        }
        job.setExeUrl(url);
    }

    private Object makeErrorResponse(String code, String message, Map<String, Object> result) {
        result.put("error", code);
        result.put("msg", message);
        return result;
    }
    
    @RequestMapping("queryJob")
    @ResponseBody
    public Object queryJob(Integer id,String jobName){

        JobInfo job = jobService.queryJobByIdOrName(id,jobName);
        Map<String,Object> result = new HashMap<String,Object>();
    	result.put("error", 0);
        result.put("data", job);
        return result;
    }

    @RequestMapping("queryAllJob")
    @ResponseBody
    public Object queryAllJob(){
    	try{
    		List<JobInfo> list= jobService.queryAllJobInfo();
            Map<String,Object> result = new HashMap<String,Object>();
        	result.put("error", 0);
            result.put("data", list);
            return result;
    	}catch(Exception e)
    	{
    		Map<String,Object> result = new HashMap<String,Object>();
        	result.put("error", 1);
            result.put("msg", e.getClass().getName()+","+e.getMessage());
            return result;
    	}
    }
    
    @RequestMapping("getPerDrivedJobs")
    @ResponseBody
    public Object getPerDrivedJobs(){
    	
    	Map<Integer,JobInfo> map = JobManager.getInstance().getPerDrivedJobs();
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("error", 0);
        result.put("data", map);
        return result;
    }
    
    @RequestMapping("getDrivedJobs")
    @ResponseBody
    public Object getDrivedJobs(){
    	
    	List<JobInfo> list= jobService.getDriveedJobs();
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("error", 0);
        result.put("data", list);
        return result;
    }
    
    @RequestMapping("hasDrivedJob")
    @ResponseBody
    public Object hasDrivedJob(Integer id,String jobName){
    	
    	Map<Integer,Boolean> has = jobService.hasDrivedJob(id,jobName);
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("error", 0);
        result.put("data", has);
        return result;
    }
    
    @RequestMapping("driveAllJob")
    @ResponseBody
    public BaseResponse driveAllJob(){
    	
    	jobService.startAllDriveJobs();
    	return new BaseResponse(0 , "success");
    }
    
    @RequestMapping("stopAllJob")
    @ResponseBody
    public Object stopAllJob(){
    	
    	jobService.stopAllDriveJobs();
    	return new BaseResponse(0 , "success");
    }
    
    @RequestMapping("directExeJob")
    @ResponseBody
    public Object directExeJob(Integer id){
    	JobResponse rsp=jobService.directExeTarget(id);
    	return rsp;
    }

    /**
     * 监控自动任务定时调用入口
     */
    @RequestMapping("monitJob")
    @ResponseBody
    public Object monitJob(Integer jobExecuteId)
    {
    	jobMonitService.monitJob(jobExecuteId);
    	return new BaseResponse(0 , "success");
    }
   
   @RequestMapping("getMonitJobsCount")
   @ResponseBody
   public Object getMonitJobsCount() 
   {
		Map<String,Object> result = new HashMap<String,Object>(2);
	   	int monitCount = JobMonitor.getInstance().getMonitJobMap().size();
	   	result.put("error", 0);
	   	result.put("count",monitCount);
	   	return result;
   }
   
   @RequestMapping("traceJob")
   @ResponseBody
   public Object traceJob(Integer id)
   {
	   jobService.traceJob(id);
	   return new BaseResponse(0 , "success");
   }
}
