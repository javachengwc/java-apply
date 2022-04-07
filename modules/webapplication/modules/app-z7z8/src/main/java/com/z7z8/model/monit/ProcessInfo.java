package com.z7z8.model.monit;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ProcessInfo {

	public Integer id;
	
	public String UserName;
	
	public String MachineName;
	
	public Date Time;
	
	public List<ProcessItem> Items;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getMachineName() {
		return MachineName;
	}
	public void setMachineName(String machineName) {
		MachineName = machineName;
	}
	public Date getTime() {
		return Time;
	}
	public void setTime(Date time) {
		Time = time;
	}
	public List<ProcessItem> getItems() {
		return Items;
	}
	
	public void setItems(List<ProcessItem> items) {
		Items = items;
	}
	
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
	
}
