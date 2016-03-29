package com.manageplat.service.sync.ssh;

import com.jcraft.jsch.Session;

/**
 * SSH服务接口，包括SSH正常登录、退出以及获取其会话
 *
 */
public interface ISSHService {
	/**
	 * 获取SSH的会话
	 */
	public Session getSession();
	
	/**
	 * 登录SSH
	 */
	public void login();
	
	/**
	 * 退出SSH会话
	 */
	public void logout();
	
	/**
	 * 获取登录SSH设置信息
	 */
	public <T extends ISSHConfig> T getServerConfig();
	
	/**
	 * 设置SSH登录信息
	 */
	public <T extends ISSHConfig> void setServerConfig(T serverConfig);
}
