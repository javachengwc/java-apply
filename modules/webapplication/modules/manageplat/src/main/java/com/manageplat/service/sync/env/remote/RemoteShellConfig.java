package com.manageplat.service.sync.env.remote;

import com.manageplat.service.sync.PwdDBShellConfig;

public class RemoteShellConfig extends PwdDBShellConfig {
	private String telnetHost;
	private int telnetPort;

	/**
	 * 获取telnet的目标机器
	 */
	public String getTelnetHost() {
		return telnetHost;
	}

    /**
	 * 设置telnet的目标机器
	 */
	public void setTelnetHost(String telnetHost) {
		this.telnetHost = telnetHost;
	}

    /**
	 * 获取telnet的端口
	 */
	public int getTelnetPort() {
		return telnetPort;
	}

    /**
	 * 设置telnet的端口
	 */
	public void setTelnetPort(int telnetPort) {
		this.telnetPort = telnetPort;
	}
}
