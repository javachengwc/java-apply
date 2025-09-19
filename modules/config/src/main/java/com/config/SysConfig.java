package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class SysConfig {

    private static Properties properties = new Properties();
	
	public static final Logger logger = LoggerFactory.getLogger(SysConfig.class);

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
