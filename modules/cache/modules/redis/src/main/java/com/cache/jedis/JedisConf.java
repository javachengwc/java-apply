package com.cache.jedis;

import java.util.Properties;

import com.util.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JedisConf {

    private static Logger logger = LoggerFactory.getLogger(JedisConf.class);

	private static Properties prop = new Properties();
	
	public static String PATH = "redis.properties";

    public static String REDIS_HOST = "redis.ip";

    public static String REDIS_PORT = "redis.port";

    static
    {
        init();
    }

	public static void init(){
		try {
			prop=PropertiesLoader.load(PATH);
		} catch (Exception e) {
			logger.error("JedisConf init error,", e);
		}
	}

	public static String getHost() {

        return getValue(REDIS_HOST);
	}

	public static int getPort() {

        String vlu =getValue(REDIS_PORT);
        return Integer.parseInt(vlu);
	}

    public static String getValue(String key)
    {
        return prop.getProperty(key);
    }
}
