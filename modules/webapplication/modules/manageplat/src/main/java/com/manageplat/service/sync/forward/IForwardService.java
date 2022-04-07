package com.manageplat.service.sync.forward;

import com.manageplat.service.sync.ssh.ISSHService;

/**
 * 影射服务基础接口
 *
 */
public interface IForwardService extends ISSHService {

	/**
	 * 将远程服务器影射到本地
	 */
	public void createPortForwardLocal();
	
	/**
	 * 将本地影射到远程服务器
	 */
	public void createPortForwardRemote();
}
