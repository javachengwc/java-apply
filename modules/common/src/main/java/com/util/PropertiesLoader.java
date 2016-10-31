package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * properties配置文件加载器
 */
public class PropertiesLoader {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    public static Properties load(String pathname) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(pathname), Charset.forName("UTF-8")));
        } catch (Exception e) {
            logger.error("PropertiesLoader load properties error,patchname= " + pathname, e);
        }
        return properties;
    }

    public static Properties loadFile(String absPath)
    {
        File file = new File(absPath);
        return loadFile(file);
    }

    public static Properties loadFile(File file )
    {
        return loadFile(file,null);
    }

    public static Properties loadFile(File file,Properties savedProperties )
    {
        InputStream in = null;
        if(savedProperties==null) {
            savedProperties = new Properties();
        }
        try {
            if (file!=null && file.exists()) {
                in = new FileInputStream(file);
                savedProperties.load(in);
                return savedProperties;
            }else
            {
                logger.info("PropertiesLoader loadFile file is not exist");
            }
        } catch (Throwable e) {
            logger.error("PropertiesLoader loadFile error", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn("PropertiesLoader loadFile close inputstream error", e);
                }
            }
        }
        return savedProperties;
    }
}
