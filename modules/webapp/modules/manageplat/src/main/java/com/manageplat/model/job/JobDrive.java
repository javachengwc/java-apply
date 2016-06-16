package com.manageplat.model.job;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 记录任务驱动信息 (任务的启动，结束)
 */
public class JobDrive {
	
	private Integer id;

	private Integer jobId;
	
	//记录时间
	private Integer recordTime;
	
	//操作标记
	private Integer optFlag;//0  ---任务驱动开始 1---任务结束
	
	//操作结果
	private Integer result;//0  --成功, 1--失败

	//操作备注
	private String note;
	
	//操作人
	private String driver;

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

	public Integer getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Integer recordTime) {
		this.recordTime = recordTime;
	}

	public Integer getOptFlag() {
		return optFlag;
	}

	public void setOptFlag(Integer optFlag) {
		this.optFlag = optFlag;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
    
}
