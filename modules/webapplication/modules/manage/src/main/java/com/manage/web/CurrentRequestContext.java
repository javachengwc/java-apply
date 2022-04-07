package com.manage.web;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CurrentRequestContext implements Serializable {

	private static final long serialVersionUID = -1255659361069707788L;

	//服务访问 根路径 比如:http://host:port/app/
	private String serverPath;
	
	//访问相关路径 比如:user/user.aciton
	private String relativePath;
	
	//用户IP
	private String remoteAddr;
	
	//用户name
	private String username;
	
	private String refererURL;
	
	private String refererRelativePath;
	
	private boolean allowRequest=false;
    
	public CurrentRequestContext()
	{
		System.out.println("--- --- currentRequestContext create---");
	}
	
	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRefererURL() {
		return refererURL;
	}

	public void setRefererURL(String refererURL) {
		this.refererURL = refererURL;
	}

	public String getRefererRelativePath() {
		return refererRelativePath;
	}

	public void setRefererRelativePath(String refererRelativePath) {
		this.refererRelativePath = refererRelativePath;
	}

	public boolean isAllowRequest() {
		return allowRequest;
	}

	public void setAllowRequest(boolean allowRequest) {
		this.allowRequest = allowRequest;
	}
	
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
}
