package com.configcenter.util;

import com.util.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 获取应用配置类
 */
public class SysConfig {

    private static Properties properties = new Properties();

    public static Logger logger = LoggerFactory.getLogger(SysConfig.class);

    static {
        try {
            logger.info("SysConfig static init start.");
            properties = PropertiesLoader.load("var.properties");
        } catch (Exception e) {
            logger.error("fail to find config file var.properties", e);
        }
    }

    public static String getValue(String key) {

        return properties.getProperty(key);
    }
}

