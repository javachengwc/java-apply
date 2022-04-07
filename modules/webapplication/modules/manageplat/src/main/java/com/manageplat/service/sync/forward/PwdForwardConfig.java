package com.manageplat.service.sync.forward;

import com.manageplat.service.sync.ssh.PwdSSHConfig;

/**
 * Forward 明文密码登录信息设置类
 *
 */
public class PwdForwardConfig extends PwdSSHConfig {

	private int lport;

    private String lhost;

    private int rport;

    private String rhost;

    /**
	 * 获取影射的本地端口
	 */
	public int getLport() {
		return lport;
	}

    /**
	 * 设置影射的本地端口
	 */
	public void setLport(int lport) {
		this.lport = lport;
	}

    /**
	 * 获取影射的本地Host
	 */
	public String getLhost() {
		return lhost;
	}

    /**
	 * 设置影射的本地Host
	 */
	public void setLhost(String lhost) {
		this.lhost = lhost;
	}

    /**
	 * 获取影射的远程机器端口
	 */
	public int getRport() {
		return rport;
	}

    /**
	 * 设置影射的远程机器端口
	 */
	public void setRport(int rport) {
		this.rport = rport;
	}

    /**
	 * 获取影射的远程机器Host
	 */
	public String getRhost() {
		return rhost;
	}

    /**
	 * 设置影射的远程机器Host
	 */
	public void setRhost(String rhost) {
		this.rhost = rhost;
	}
}
