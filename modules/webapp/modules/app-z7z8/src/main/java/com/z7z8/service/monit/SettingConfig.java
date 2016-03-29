package com.z7z8.service.monit;

import java.util.Properties;

import com.util.PropertiesLoader;
import com.z7z8.model.monit.SettingInfo;
import org.apache.log4j.Logger;


public class SettingConfig {

	private static Properties setting = new Properties();
	
	public static final Logger logger = Logger.getLogger(SettingConfig.class);

	static {
		try {
			logger.info("SettingConfig static init start.");
            setting = PropertiesLoader.load("monitor.properties");
			logger.info("SettingConfig static init end.");
		} catch (Exception e) {
			logger.error("SettingConfig fail to find config file /monitor.properties", e);
		}
	}

	public static String getValue(String key) {
		
		return setting.getProperty(key);
	}

	
	public static String getUploadInterval()
	{
		return getValue("monitor.uploadInterval");
	}
	
	public static String getExitCode()
	{
		return getValue("monitor.exitCode");
	}
	
	public static String getAttachDir()
	{
		return getValue("monitor.attachDir");
	}
	/**
	 * 获取保存最新版本号的文件全路径名
	 * @return
	 */
	public static String getLastedVersionNoFile()
	{
		return getValue("monitor.lastedVersionNoFile");
	}
	/**
	 * 获取版本文件的路径
	 * @return
	 */
	public static String getVersionPath()
	{
		return getValue("monitor.versionPath");
	}
	
	public static SettingInfo getSettingInfo()
	{
		SettingInfo info = new SettingInfo();
		info.setUploadInterval(Integer.parseInt(getUploadInterval()));
		info.setExitCode(getExitCode());
		
		return info;
	}
}
