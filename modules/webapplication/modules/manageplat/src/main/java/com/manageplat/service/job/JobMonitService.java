package com.manageplat.service.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.manageplat.dao.job.JobMonitDao;
import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.job.JobMonit;
import com.manageplat.model.vo.job.JobExecuteQueryVo;
import com.manageplat.model.vo.job.JobMonitQueryVo;
import com.util.base.NumberUtil;
import com.util.date.SysDateTime;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自动任务监控
 *
 */
@Service
public class JobMonitService {

	private static Logger logger = LoggerFactory.getLogger(JobMonitService.class);
	
	public final int SAMPLING_TINE=30*60;//采集时间30分钟内
	
	public final int AllOW_STAY_TIME=2*60;//允许的交互误差时间

	private static final ExecutorService executors = Executors.newFixedThreadPool(10);
	
    public List<Future<?>> monitFutureList = Collections.synchronizedList(new ArrayList<Future<?>>());
	
	private static int doPerCount=2000;
	
	private static String GATHERKEYS [] ={"total","doFailCount"};

	//crontab 星期全写
	private static String WEEKFULLNAMES [] = {"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
	
	//CRONTAB 星期简写
	private static String WEEKSMPNAMES [] = {"sun","mon","tue","wed","thu","fri","sat"};
	
	public static int JOB_DO_PERCENT=90;//自动任务执行的百分比闸值，少于这个百分比视为未正常执行

    @Autowired
    private JobMonitDao jobMonitDao;

	@Autowired
	private JobService jobService;

    @Autowired
    private JobExecuteService jobExecuteService;

	/**
	 * 查询监控详情列表
	 */
	public List<JobMonit> queryPage(JobMonitQueryVo queryVo)
	{
        return jobMonitDao.queryPage(queryVo);
	}

	/**
	 * 监控检测任务状况
	 */
	public void monitJob(Integer jobExecuteId)
	{
		int now=  SysDateTime.getNow();
		int start =now -SAMPLING_TINE;
		int queryEnd =now;
		
        List<JobInfo> list = jobService.queryAllJobInfo();
		if(list!=null && list.size()>0)
        {
            exeJobMonit(list,start,queryEnd);
        }

		if(jobExecuteId!=null)
		{   
			waitTaskComplete();
			Map<String,String> rt = combinTaskResult();
			int end = SysDateTime.getNow();
			JobExecute jobExecute = new JobExecute();
			jobExecute.setId(jobExecuteId);
			jobExecute.setState(1);//成功
			StringBuffer buf = new StringBuffer();
			buf.append("监控开始:").append(start).append(",");
			buf.append("结束:").append(end).append(",");
			buf.append("采集范围:").append(SAMPLING_TINE).append(",");
			buf.append("总任务数:").append(""+rt.get("total")).append(",");
			buf.append("执行失败次数:").append(""+rt.get("doFailCount")).append(",");
			buf.append("存在未执行任务:").append(""+rt.get("unDojob"));
			buf.append("失败任务:").append(""+rt.get("failJob"));
			
			String note = buf.toString();
			note = note.substring(0, (note.length()>400)?400:note.length());
			jobExecute.setNote(note);
			
			jobService.recordActOver(jobExecute);
		}
	}
	
	/**
     * 取消批量执行任务
     */
    public void stopMonitTask()
	{
		if(monitFutureList!=null && monitFutureList.size()>0)
		{
			for(Future<?> f:monitFutureList)
			{
				//立马取消，不管是否执行完
				if(!f.isDone())
				{
					logger.error("monitFutureList furture begin stop ");
				    f.cancel(true);
				}
			}
		}
	}
    
    /**
     * 清理环境
     */
    public void celarEnv()
    {
    	clearTask();
    }
    
    public void clearTask()
    {
    	if(monitFutureList!=null && monitFutureList.size()>0)
		{
    		monitFutureList.clear();
		}
    }
    
    public void waitTaskComplete()
	{
		if(monitFutureList!=null && monitFutureList.size()>0)
		{
			for(Future<?> f:monitFutureList)
			{
				logger.error("monitFutureList furture doing ");
				while(!f.isDone())
				{
					//等获取任务信息结束
				}
			}
		}
	}
    
    @SuppressWarnings("unchecked")
	public Map<String,String> combinTaskResult()
    {
    	Map<String,String> result=new HashMap<String,String>();
    	if(monitFutureList!=null && monitFutureList.size()>0)
		{
			for(Future<?> f:monitFutureList)
			{
				try{
				   Object obj= f.get();
				   if(obj==null)
				   {
					   continue;
				   }
				   Map<String,String> info = (Map<String,String>)obj;
				   combinInfo(result,info);
				}catch(Exception e)
				{
					logger.error("JobMonitService combinTaskResult error,",e);
				}
			}
		}
    	return result;
    }
	
    public void combinInfo(Map<String,String> result,Map<String,String> info)
    {
    	
    	List<String> digitKeyList =  Arrays.asList(GATHERKEYS);
    	for(String key:info.keySet())
    	{
    		String old = result.get(key);
    		String add = info.get(key);
    		if(StringUtils.isBlank(old))
    		{
    			result.put(key, add);
    		}else if(digitKeyList.contains(key))
    		{
    			int oldInt =0,addInt=0;
    			if(NumberUtil.isNumeric(old))
    			{
    				oldInt = Integer.parseInt(old);
    			}
    			if(NumberUtil.isNumeric(add))
    			{
    				addInt = Integer.parseInt(add);
    			}
    			result.put(key, ""+(oldInt+addInt));
    		}
    		else
    		{
    			result.put(key, old+","+add);
    		}
    	}
    }
    
    /**
	 * 执行 更新关注信息
	 */
    public void exeJobMonit(List<JobInfo> jobs,int start,int end){

    	Future<?> f = executors.submit(new JobMonitThread(jobs,start,end));
    	monitFutureList.add(f);

	}
	
    private class JobMonitThread implements Callable<Map<String,String>> {
    	
    	private List<JobInfo> list;
    	
    	private int start;
    	
    	private int end;
    	
		public JobMonitThread(List<JobInfo> list,int start,int end)
		{
			this.list=list;
			this.start=start;
			this.end=end;
		}

		@Override
		public Map<String,String> call() throws Exception {
			
			Map<String,String> info =null;
			try{
				info=monitJobPer(list,start,end);
				logger.error("JobMonitService JobMonitThread exe end.");
			}catch(Exception e)
			{
				logger.error("JobMonitService JobMonitThread exe error,",e);
			}
			return info;
		}
	}
    
    public boolean checkFinish(Integer exeStatus)
    {
    	if(exeStatus!=null && exeStatus==1)
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * 监控数据分析具体实现
     * @param list
     * @return
     */
    public Map<String,String> monitJobPer(List<JobInfo> list,int start,int end)
	{
    	if(list==null || list.size()<=0)
    	{
    		return null;
    	}
    	//"total","undoCount","doFailCount","failJob"
    	int total = 0;
    	//int undoCount=0;
    	int doFailCount=0;
    	StringBuffer failJobBuf = new StringBuffer("");
    	StringBuffer unDoJobBuf = new StringBuffer("");
    	
    	for(JobInfo job:list)
    	{    
    		
    		//job是否可驱动
    		boolean isCanRun=JobManager.getInstance().isCanRunJob(job);
    		
    		if(!isCanRun)
    		{
    			continue;
    		}
    		total++;
    		
    		int jobFailCount=0;
    		
    		//*2是因为可能有2个web应用，同一时间点执行会产生2条记录，只有1条是正常的 其他都是被排斥掉未执行的
            JobExecuteQueryVo queryVo =new JobExecuteQueryVo();
            queryVo.setJobId(job.getId());
            queryVo.setStartTimeBegin(start);
            queryVo.setStartTimeEnd(end);
            queryVo.setStart(0);
            queryVo.setRows(2*SAMPLING_TINE/60);
    		List<JobExecute> exeList = jobExecuteService.queryPage(queryVo);

    		//需要过滤掉被其他线程执行的
    		int maxExeTime =start;
    		
    		if(exeList!=null && exeList.size()>0)
    		{
    			Iterator<JobExecute> it = exeList.iterator();
    			while(it.hasNext())
    			{
    				JobExecute exeInfo = it.next();
    				if(2==exeInfo.getState())
    				{
                        //未执行
    					it.remove();
    				}else
    				{
    					if(exeInfo.getStartTime()>maxExeTime)
    					{
    						maxExeTime = exeInfo.getStartTime();
    					}
    				}
    			}
    		}
    		
    		int doCount=0;
    		if(exeList!=null)
    		{
    			doCount =exeList.size();
    		}
    		//检测是否正常执行
    		maxExeTime = maxExeTime+AllOW_STAY_TIME;
    		boolean hasCanDo=false;
    		int endBf=end-AllOW_STAY_TIME;
    		if(maxExeTime <endBf)
    		{
    			hasCanDo=checkJobHasCanDo(job,maxExeTime,endBf);
    		}
    		
    		if(hasCanDo)
    		{
    			//记录监控异常日志，更新最近自动任务监控异常标记
    			recordMonitInfo(job,end,1,"存在未正常执行的情况a");
    			unDoJobBuf.append(job.getId()).append(",");
    			continue;
    		}
    		
    		StringBuffer jobFailExeBuf = new StringBuffer("");
    		//检测是否执行成功
    		if(doCount>0)
    		{
    			for(JobExecute e:exeList)
    			{
    				Integer exeStatus =e.getState();
    				if(exeStatus!=null && 2==exeStatus)
					{
                        //2--未执行
						continue;//任务已被另一进程执行或修改
					}
    				boolean isSuccess=true;
    				if(job.getIsCallBack()!=null && job.getIsCallBack()==1)
    				{
    					//正在执行
	    				if(e.getEndTime()==0 )
	    				{
	    					if(job.getPlanCostTime()!=null)
	    					{
	    						//超时
	    						if(e.getStartTime()+job.getPlanCostTime()>end)
	    						{
	    							//执行失败
	    							isSuccess=false;
	    						}
	    					}
	    					
	    				}else
	    				{
	    					
	    					if(!checkFinish(exeStatus))
	    					{
	    						//执行失败
	    						isSuccess=false;
	    					}
	    					
	    				}
    				}else
    				{
                        //定时到点执行--2
    					if(2!=exeStatus  && !checkFinish(exeStatus))
    					{
    						isSuccess=false;
    					}
    				}
    				
    				if(!isSuccess)
    				{
    					//执行失败
						jobFailCount++;
						jobFailExeBuf.append(e.getId()).append(",");
    				}
    				
    			}
    		}
    		
    		//记录执行未完成
    		if(jobFailCount>0)
    		{
    			doFailCount+=jobFailCount;
    			failJobBuf.append(job.getId()).append(",");
    			String jobFailExeInfo = jobFailExeBuf.toString();
    			String note = "执行失败数:"+jobFailCount+",执行失败记录id:"+jobFailExeInfo;
    			note=note.substring(0,note.length()>200?200:note.length());
    			recordMonitInfo(job,end,2,note);
    		}
    		
    	}
    	
    	String failJob = failJobBuf.toString();
    	String unDojob = unDoJobBuf.toString();
    	Map<String,String> result = new HashMap<String,String>();
    	result.put("total", ""+total);
    	result.put("doFailCount", ""+doFailCount);
    	result.put("failJob", failJob);
    	result.put("unDojob", unDojob);
    	return result;
	}
    
    /**
     * 检查此时间段是否还有可执行任务
     * @param job
     * @param start
     * @param end
     * @return
     */
    public boolean checkJobHasCanDo(JobInfo job,int start,int end)
    {
    	if(job.getParentId()!=null && job.getParentId()>0)
		{
			return false;
		}
    	String expr = job.getExpression();
    	return curTimeCanDo(expr,start,end);
    	
    }
    
    /**
     * corntab星期中的表达式是否包含英文表达式
     */
    public boolean isEnglishExp(String exp)
    {
    	String lowStr = exp.toLowerCase();
    	for(String pw :WEEKSMPNAMES)
    	{
    		if(lowStr.indexOf(pw)>=0)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 将corntab星期中英文表达式转换成数字表达式
     */
    public String transEngToNumExp(String exp)
    {
    	String lowStr = exp.toLowerCase();
    	
    	for(int i=0;i<WEEKFULLNAMES.length;i++)
    	{
    		lowStr = lowStr.replace(WEEKFULLNAMES[i], ""+i);
    	}
        
    	for(int i=0;i<WEEKSMPNAMES.length;i++)
    	{
    		lowStr = lowStr.replace(WEEKSMPNAMES[i], ""+i);
    	}
    	return lowStr;
    }
    
    public int isCurTime(String exp,int beginMin,int endMin,int index)
    {
    	//System.out.println(exp+" "+beginMin+" "+endMin+" "+index);
    	//exp可能的表达式:* 0,10 0 0-10 */10  //星期最后来考虑
    	if("*".equals(exp) || "?".equals(exp))
    	{
    		if(endMin>=beginMin)
    		{
    			return (endMin-beginMin+1);
    		}else
    		{
    			return (getDistMaxValue(index)-beginMin)+endMin;
    		}
    	}
    	//星期，表达式如果是用英文，需要转换一下
    	if(index==4 && isEnglishExp(exp) )
    	{
    	   exp = transEngToNumExp(exp);	
    	}
    	if(NumberUtil.isNumeric(exp))
    	{
    		int expMin = Integer.parseInt(exp);
    		boolean rt= isCurTimePoint(expMin,beginMin,endMin,index);
    		if(rt)
    		{
    			return 1;
    		}
    		return 0;
    	}
    	
    	if(exp.indexOf(",")>=0)
    	{
    		String ps [] = exp.split(",");
    		int count=0;
    		for(String p:ps)
    		{
    			if(NumberUtil.isNumeric(p))
    			{
    				int pMin = Integer.parseInt(p);
    				boolean rt = isCurTimePoint(pMin,beginMin,endMin,index);
    				if(rt)
    				{
    					count++;
    				}
    			}
    		}
    		return count;
    	}
    	
    	if(exp.indexOf("-")>0)
    	{
    		int step=1;
    		if(exp.indexOf("/")>0)
    		{
    			String ps [] = exp.split("\\/");
    			if(ps.length==2 && NumberUtil.isNumeric(ps[1]))
    			{
    				step = Integer.parseInt(ps[1]);
    			}
    			exp = exp.substring(0,exp.indexOf("/"));
    		}
    		String ts [] = exp.split("-");
    		if(ts.length==2 && NumberUtils.isNumber(ts[0]) && NumberUtils.isNumber(ts[1]))
    		{
    			int start= Integer.parseInt(ts[0]);
    			int end = Integer.parseInt(ts[1]);
    			int count=0;
    			for(int i=start;i<=end;i+=step)
    			{
    				boolean rt =isCurTimePoint(i ,beginMin,endMin,index);
    				if(rt)
    				{
    				    count++;
    				}
    			}
    			return count;
    		}
    		return 0;
    	}
    	
    	if(exp.indexOf("/")>0)
    	{
    		String ps [] = exp.split("\\/");
			if(ps.length==2 && NumberUtil.isNumeric(ps[1]))
			{
                 int begin=0;
                 if(NumberUtils.isNumber(ps[0]))
                 {
                	 begin = Integer.parseInt(ps[0]);
                 }
                 int step =Integer.parseInt(ps[1]);
                 int end = getDistMaxValue(index);
                 
                 int count=0;
                 for(int i=begin;i<=end;i+=step)
     			 {
     				boolean rt =isCurTimePoint(i ,beginMin,endMin,index);
     				if(rt)
     				{
     				    count++;
     				}
     			 }
                 return count;
			}
			return 0;
    	}
    	
    	return 1;
    }
    
    /**
     * index 0 -分  1-时  2-日 3-月 4-星期
     */
    public int getDistMaxValue(int index)
    {
    	if(index==0)
    	{
    		return 60;
    	}
    	if(index==1)
    	{
    		return 24;
    	}
    	if(index==2)
    	{
    		return 30;
    	}
    	if(index==3)
    	{
    		return 12;
    	}
    	if(index==4)
    	{
    		return 7;
    	}
    	return 0;
    }
    
    public Integer [] getDistBeginEndValue(int i,Calendar beginCa,Calendar endCa)
    {
    	int beginT=0,endT=0;
    	if(i==0)
		{
			beginT= beginCa.get(Calendar.MINUTE);
	    	endT = endCa.get(Calendar.MINUTE);
	    	long interval =endCa.getTimeInMillis()-beginCa.getTimeInMillis();
	    	if(interval/1000/60 >=getDistMaxValue(i))
	    	{
	    		beginT=0;
	    		endT=getDistMaxValue(i);
	    	}
		}
		if(i==1)
		{
			beginT= beginCa.get(Calendar.HOUR_OF_DAY);
	    	endT = endCa.get(Calendar.HOUR_OF_DAY);
		}
		if(i==2)
		{
			beginT= beginCa.get(Calendar.DAY_OF_MONTH);
	    	endT = endCa.get(Calendar.DAY_OF_MONTH);
		}
		if(i==3)
		{
			beginT= beginCa.get(Calendar.MONTH)+1;
	    	endT = endCa.get(Calendar.MONTH)+1;
		}
		if(i==4)
		{
			beginT= beginCa.get(Calendar.DAY_OF_WEEK)-1;
	    	endT = endCa.get(Calendar.DAY_OF_WEEK)-1;
		}
		return new Integer[]{beginT,endT};
    }
    
    /**
     * 时间点判断
     */
    public boolean isCurTimePoint(int point ,int start,int end,int index)
    {
    	if(start>end)
    	{
    		if(0<=point && end>=point)
	    	{
	    		return true;
	    	}
    		if(start<=point && getDistMaxValue(index)>=point)
	    	{
	    		return true;
	    	}
    	}	
    	else
    	{
	    	if(start<=point && end>=point)
	    	{
	    		return true;
	    	}
    	}
    	return false;
    }
    
    //当前时间段执行次数
    public boolean curTimeCanDo(String expr,int start,int end)
    {
    	if(StringUtils.isBlank(expr))
    	{
    		return false;
    	}
    	Calendar beginCa= Calendar.getInstance();
    	long beginMiss =start*1000l;
    	long endMiss = end*1000l;
    	beginCa.setTimeInMillis(beginMiss);
    	int beginSec= beginCa.get(Calendar.SECOND);
    	if(beginSec>0)
    	{
    	    beginCa.add(Calendar.MINUTE, 1);
    	    beginCa.set(Calendar.SECOND, 0);
    	}
    	Calendar endCa= Calendar.getInstance();
    	endCa.setTimeInMillis(endMiss);
    	
    	String exps [] = expr.split(" ");
    	
    	if(exps.length<5)
    	{
    		return false;
    	}
    	String after = QuartzService.transExpress(expr);
    	try{
    		CronExpression cexp = new CronExpression(after);
	    	Date nextTime = cexp.getNextValidTimeAfter(beginCa.getTime());
	    	if(endCa.getTimeInMillis()>nextTime.getTime())
	    	{
	    		return true; 
	    	}
	    	
    	}catch(Exception e)
    	{
    		logger.error("JobMonitService curTimeCanDo CronExpression error,",e);
    		return false;
    	}
    	return false;
    }
    
    /**
     * 插入任务监控记录
     * @param jobMonit
     * @return
     */
    public boolean  insertJobMonit(JobMonit jobMonit) {
    	
       //待执行
        return true;
    }
    
    /**
     * 记录监控异常日志，更新最近自动任务监控异常标记
     * @param job
     * @param endTime
     * @param result
     */
    public void recordMonitInfo(JobInfo job,int endTime,int result,String note)
    {
    	JobMonit jobMonit = new JobMonit();
		jobMonit.setJobId(job.getId());
		jobMonit.setRecordTime(endTime);
		jobMonit.setResult(result);
		jobMonit.setNote(note);
		insertJobMonit(jobMonit);
		
		job.setLastedMonitTime(endTime);
		job.setMonitResult(result);
		
		jobService.updateJob(job);
    }
    
    public static void main(String args[] )
    {
    	JobMonitService service = new JobMonitService();
    	int start=1418194200;//2014-12-10 14:50:00
    	          
    	int end=start+30*60;
    	Calendar ca = Calendar.getInstance();
    	long a = start*1000l;
    	ca.setTimeInMillis(a);
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println(format.format(ca.getTime()));
    	
    	String expr="10 14 * * *";
    	
    	boolean canDo =service.curTimeCanDo(expr,start,end);
    	
    	System.out.println("canDo="+canDo);
    }
}
