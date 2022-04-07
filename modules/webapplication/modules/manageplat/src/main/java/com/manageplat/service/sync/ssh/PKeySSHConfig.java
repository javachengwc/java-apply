package com.manageplat.service.sync.ssh;

/**
 * 使用private/public key登录的SSH登录设置类
 *
 */
public class PKeySSHConfig extends PwdSSHConfig {

    private String keyFile;

	/**
	 * 获取private key文件路径
	 */
	public String getKeyFile() {
		return keyFile;
	}

	/**
	 * 设置private key文件
	 */
	public void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
	}
	
}
