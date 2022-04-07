package com.manageplat.service.sync.shell;

import com.jcraft.jsch.Channel;
import com.manageplat.service.sync.ssh.ISSHService;


/**
 * Shell服务基础接口
 *
 */
public interface IShellService extends ISSHService {

	/**
	 * 获取shell会话对象
	 */
	public Channel getShell();
	
	/**
	 * 通过shell执行命令操作
	 */
	public void execute(String command);
}
