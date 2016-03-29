package com.manageplat.model.job;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 自动任务监控记录
 * job_monit
 */
public class JobMonit {
	
	private Integer id ;
	
	private Integer jobId;
	
	//监控时间
	private int recordTime;
	
	private Integer result;//0---正常，1---未正常执行 2---正常执行了但未执行成功
	
	//操作备注
	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public int getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(int recordTime) {
		this.recordTime = recordTime;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
	
}
