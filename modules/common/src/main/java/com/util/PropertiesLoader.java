package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * properties配置文件加载器
 */
public class PropertiesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

    public static Properties load(String pathname) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(pathname), Charset.forName("UTF-8")));
        } catch (Exception e) {
            LOGGER.error("get properties config file : " + pathname + " error!" + e.getMessage(), e);
        }
        return properties;
    }
}
