package com.z7z8.model.monit;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ProcessItem {
	
	public Integer id;
	
	public Integer processId;
	
	public String Name;
	
    public String Title;
    
    public String FileName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}
	
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

}
