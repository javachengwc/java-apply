package com.z7z8.model.monit;

public class SettingInfo {

	//上传间隔时间
	public Integer uploadInterval;
	
	//退出口令
	public String exitCode;

	public Integer getUploadInterval() {
		return uploadInterval;
	}

	public void setUploadInterval(Integer uploadInterval) {
		this.uploadInterval = uploadInterval;
	}

	public String getExitCode() {
		return exitCode;
	}

	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}
}
