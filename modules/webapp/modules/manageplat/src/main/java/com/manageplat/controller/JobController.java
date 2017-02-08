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
import com.util.lang.ReflectionUtil;
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
        if(StringUtils.isBlank(job.getJobName())){
            return genResponse("1","任务名为空");
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
         if( StringUtils.isBlank(job.getJobName())){
              return genResponse("1","任务名为空");
         }
         JobInfo oJob=jobService.queryJobByIdOrName(job.getId(),job.getJobName());
         if(oJob==null){
               return new BaseResponse(1,"任务不存在");
         }
         job.setId(oJob.getId());
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
	   if( StringUtils.isBlank(job.getJobName()) && (job.getId()==null|| job.getId()<=0) ){
    	   
            return genResponse("1","任务ID,名称都为空");
       }
       if(job.getJobStatus()==null)
       {
    	    return genResponse("1","任务状态为空");
       }
       JobInfo oldJob = jobService.queryJobByIdOrName(job.getId(),job.getJobName());
       if(oldJob==null)
       {
           return genResponse("1","任务不存在");
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
        if( StringUtils.isBlank(jobInfo.getJobName()) && (jobInfo.getId()==null|| jobInfo.getId()<=0) ){
            return genResponse("1","任务ID,名称都为空");
        }
        JobInfo oJob=jobService.queryJobByIdOrName(jobInfo.getId(),jobInfo.getJobName());
        if(oJob==null){
            return createJob(jobInfo,request);
        }else
        {
            jobInfo.setId(oJob.getId());
            return jobService.startJob(jobInfo.getId());
        }
    }
    
    /**
     * 结束任务
     */
    @RequestMapping("endJob")
    @ResponseBody
    public Object endJob(JobInfo jobInfo,HttpServletRequest request){
    	if( (jobInfo.getId()==null || jobInfo.getId()<=0) && StringUtils.isBlank(jobInfo.getJobName())  ){
            return genResponse("1","任务ID,名称都为空");
        }
        JobInfo oJob=jobService.queryJobByIdOrName(jobInfo.getId(),jobInfo.getJobName());
        if(oJob==null){
            return new BaseResponse(1,"任务不存在");
        }
        jobInfo.setId(oJob.getId());
        return jobService.endJob(jobInfo.getId());
    }
    
    /**
     * 记录任务到点执行动作结束信息
     */
    @RequestMapping("recordJobExeOver")
    @ResponseBody
    public BaseResponse recordJobExeOver(JobExecute jobExecute){
    	logger.info("JobController recordJobExeOver start param:"+jobExecute);
    	if( jobExecute.getId()<=0 ){
            return new BaseResponse(1,"任务执行记录ID为空");
        }
        if(jobExecute.getState()==null){
            return new BaseResponse(1,"任务执行结果为空");
        }
        BaseResponse rsp= jobService.recordActOver(jobExecute);
        logger.info("JobController recordJobExeOver end rsp:"+rsp.getError()+" "+rsp.getMsg());
        return rsp;
    }

    @RequestMapping("uptJobExeNote")
    @ResponseBody
    public Object uptJobExeNote(JobExecute jobExecute,HttpServletRequest request){
        if( jobExecute.getId()<=0 ){
            return new BaseResponse(1,"任务执行记录ID为空");
        }
        if(StringUtils.isBlank(jobExecute.getNote())){
            return new BaseResponse(1,"备注为空");
        }
        return jobService.uptJobExecute(jobExecute);
    }

    private void warpJobInfo(JobInfo job,HttpServletRequest request) {
        if(StringUtils.isBlank(job.getCreater())){
            job.setCreater("unknow");
        }
        if(job.getCreateTime()==null){
            job.setCreateTime(SysDateTime.getNow());
        }
        job.setJobStatus(1);//1--正常
        if(job.getParentId()==null)
        {
            job.setParentId(0);
        }
        if(StringUtils.isBlank(job.getRunStatus()))
        {
            job.setRunStatus("ready");
        }
        job.setIp(RequestUtil.getIpFromRequest(request));
    }

    //http类型的job组装参数
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
            return genResponse("1", e.getClass().getName()+","+e.getMessage());
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
     * 监控任务
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

    private Map<String,Object> genResponse(String code, String message, Map<String, Object> result) {
        if(result==null)
        {
            result =new HashMap<String,Object>();
        }
        result.put("error", code);
        result.put("msg", message);
        return result;
    }

    private Map<String,Object> genResponse(String code, String message) {
        return genResponse(code,message,null);
    }
}
