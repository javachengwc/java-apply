package com.task.util;

import com.util.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConfigUtil {
    private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    private static Properties globalProperties = new Properties();

    static {
        try {
            logger.info("SysConfig static init start.");
            globalProperties = PropertiesLoader.load("sysConfig.properties");
        } catch (Exception e) {
            logger.error("fail to find config file sysConfig.properties", e);
        }
    }

    public static String getValue(String key) {
        return globalProperties.getProperty(key);
    }
	
	
	

}
