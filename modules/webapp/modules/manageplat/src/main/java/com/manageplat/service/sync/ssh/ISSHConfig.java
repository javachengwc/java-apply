package com.manageplat.service.sync.ssh;

/**
 * SSH登录信息的设置接口
 *
 */
public interface ISSHConfig {
	/**
	 * 获取SSH登录时的用户
	 */
	public String getUser();
	/**
	 * 获取SSH登录时的用户密码/密钥
	 */
	public String getPassword();
	/**
	 * 获取SSH登录的目标机器
	 */
	public String getHost();
	/**
	 * 获取SSH登录的目标机器端口
	 */
	public int getPort();
}
