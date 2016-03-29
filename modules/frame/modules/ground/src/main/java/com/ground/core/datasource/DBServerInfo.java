package com.ground.core.datasource;


/**
 * 数据库info
 */
public class DBServerInfo
{
	private int dbIndex;
	
	private String url;
	
	private String username;
	
	private String passwd;
	
	private int maxActive;
	
	private int minIdle;
	
	private int maxIdle;
	
	public DBServerInfo(){
		
	}
	
	public DBServerInfo(int dbIndex,String url,String username,String passwd,int maxActive,int minIdle,int maxIdle){
		this.dbIndex = dbIndex;
		this.url = url;
		this.username = username;
		this.passwd = passwd;
		this.maxActive = maxActive;
		this.minIdle = minIdle;
		this.maxIdle = maxIdle;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	
}
