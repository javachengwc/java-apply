package com.manageplat.service.job.filter;

import com.manageplat.model.job.JobInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DefaultJobFilter implements IJobFilter{
	
	private List<String> allowStatus;
	
	private List<String> allowTypes;
	
	private List<String> denyTypes;
	
	public DefaultJobFilter(List<String> allowStatus)
	{
		this.allowStatus = allowStatus;
	}
	
	public DefaultJobFilter(List<String> allowStatus, List<String> allowTypes)
	{
		this(allowStatus);
		this.allowTypes= allowTypes;
	}
	
	public DefaultJobFilter(List<String> allowStatus, List<String> allowTypes, List<String> denyTypes)
	{
		this(allowStatus,allowTypes);
		this.denyTypes=denyTypes;
	}

	public List<String> getAllowStatus() {
		return allowStatus;
	}

	public void setAllowStatus(List<String> allowStatus) {
		this.allowStatus = allowStatus;
	}

	public List<String> getAllowTypes() {
		return allowTypes;
	}

	public void setAllowTypes(List<String> allowTypes) {
		this.allowTypes = allowTypes;
	}

	public List<String> getDenyTypes() {
		return denyTypes;
	}

	public void setDenyTypes(List<String> denyTypes) {
		this.denyTypes = denyTypes;
	}
    
	public List<JobInfo> filter(List<JobInfo> list)
	{

		List<JobInfo> result = new ArrayList<JobInfo>();
		if(list==null || list.size()<=0)
		{
			return result;
		}
		for(JobInfo info: list)
		{
			info = filter(info);
			if(info!=null)
			{
				result.add(info);
			}
		}
		return  result;
			
	}
	
	public JobInfo filter(JobInfo info)
	{
		
		 //定时任务表达式过滤
		 if(StringUtils.isBlank(info.getExpression()))
		 {
			 return null;
		 }
		 //状态过滤
		 if(allowStatus!=null && allowStatus.size()>=0)
		 {
		   	 if(info.getJobStatus()==null || !allowStatus.contains(info.getJobStatus()))
		   	 {
		   		 return null;
		   	 }
		 }
	   	 //类型过滤
		 if(allowTypes!=null && allowTypes.size()>=0)
		 {
		   	 if(StringUtils.isBlank(info.getType()) || !allowTypes.contains(info.getType()))
		   	 {
		   		 return null;
		   	 }
		 }
		 if(denyTypes!=null && denyTypes.size()>=0)
		 {
		   	 if(!StringUtils.isBlank(info.getType()) && denyTypes.contains(info.getType()))
		   	 {
		   		 return null;
		   	 }
		 }
		 return info;
	}
	
}
