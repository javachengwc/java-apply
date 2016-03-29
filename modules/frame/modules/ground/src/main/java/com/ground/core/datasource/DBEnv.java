package com.ground.core.datasource;

import java.sql.Connection;

public interface DBEnv
{

	/**
	 * 获得当前key所hash到的DB url
	 * @param key
	 * @return
	 */
	public String getURL(String key);


	
	/**
	 * 获取数据库连接
	 * 
	 * @param index 数据库索引
	 * @return
	 * @throws Exception
	 */
	public Connection borrowConn(int index) throws Exception;
	
	/**
	 * 返回数据库连接
	 * 
	 * @param index 数据库索引
	 * @param conn 数据库连接
	 * @throws Exception
	 */
	public void returnConn(int index, Connection conn) throws Exception;
	
	/**
	 * 获取数据库连接
	 * 
	 * @param key 均衡值
	 * @return
	 * @throws Exception
	 */
	public Connection borrowConn(String key) throws Exception;
	
	/**
	 * 返回数据库连接
	 * 
	 * @param key 均衡值
	 * @param conn 数据库连接
	 * @throws Exception
	 */
	public void returnConn(String key, Connection conn) throws Exception;
	
	/**
	 * 
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public void loadConfig(String filePath) throws Exception;
	
	
	/**
	 * 初始化连接池
	 * @throws Exception
	 */
	public void initPool() throws Exception;
	/**
	 * 数据库索引大小
	 * 
	 * @return
	 */
	public int indexSize();
	

	public String getDBPoolStatus();
}
