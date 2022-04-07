package com.manageplat.service.sync.ssh;

/**
 * 使用明文密码登录的SSH登录设置类
 *
 */
public class PwdSSHConfig implements ISSHConfig {

	private String host;

    private String password;

    private String user;

    private int port = 22;

	/**
	 * 获取SSH登录的目标机器
	 */
	public String getHost() {
		return host;
	}
	/**
	 * 获取SSH登录的目标机器
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * 获取SSH登录时的用户密码/密钥
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置SSH登录时的用户密码/密钥
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 获取SSH登录的目标机器端口
	 */
	public int getPort() {
		return this.port;
	}
	/**
	 * 设置SSH登录的目标机器端口
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * 获取SSH登录时的用户
	 */
	public String getUser() {
		return user;
	}
	/**
	 * 设置SSH登录时的用户
	 */
	public void setUser(String user) {
		this.user = user;
	}
}
