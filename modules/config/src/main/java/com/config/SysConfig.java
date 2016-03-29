package com.config;

import java.util.Properties;

import org.apache.log4j.Logger;

public class SysConfig {

    private static Properties properties = new Properties();
	
	public static final Logger logger = Logger.getLogger(SysConfig.class);

	static {
		try {
			logger.info("SysConfig static init start.");
			properties = LoadFactory.loadFromClassLoader("sysConfig.properties");
		} catch (Exception e) {
			logger.error("fail to find config file sysConfig.properties", e);
		}
	}
	
	public static String getValue(String key) {
		
		return properties.getProperty(key);
	}

}
