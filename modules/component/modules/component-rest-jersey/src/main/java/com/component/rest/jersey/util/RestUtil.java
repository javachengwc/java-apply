package com.component.rest.jersey.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class RestUtil {

    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);

    private static final String API_CONFIG_FILE  = "META-INF/api.properties";
    private static final String APPLICATION_NAME = "application.name";
    private static Map<Class<?>, Properties> propMap = new ConcurrentHashMap<Class<?>, Properties>();

    public static String getApplicationName(Class<?> resourceClass) {
        if (resourceClass == null) {
            return null;
        }
        Properties properties = propMap.get(resourceClass);
        if (properties != null) {
            return properties.getProperty(APPLICATION_NAME);
        }

        String resourceClassName = resourceClass.getName();
        synchronized (resourceClass) {
            properties = propMap.get(resourceClass);
            if (properties != null) {
                return properties.getProperty(APPLICATION_NAME);
            }

            URL url = null;
            try {
                String relativePath = resourceClass.getCanonicalName().replaceAll("\\.", "/")+ ".class";
                String resourceClassPath = resourceClass.getResource("/" + relativePath).getPath();
                resourceClassPath = simplePath(resourceClassPath, relativePath);
                logger.info("RestUtil resourceClassName={}, resourceClassPath={}",resourceClassName, resourceClassPath);
                Enumeration<URL> urls = resourceClass.getClassLoader().getResources(API_CONFIG_FILE);
                URL defaultUrl = null;
                while (urls.hasMoreElements()) {
                    URL curUrl = urls.nextElement();
                    if ("file".equals(curUrl.getProtocol())) {
                        defaultUrl = curUrl;
                    }
                    if (resourceClassPath.equals(simplePath(curUrl.getPath(), API_CONFIG_FILE))) {
                        url = curUrl;
                        break;
                    }
                }
                if(url==null) {
                    url=defaultUrl;
                }
            } catch (Exception e) {
                logger.error("RestUtil getApplicationName error,resourceClassName={}",resourceClassName, e);
            }

            if (url == null) {
                logger.info("RestUtil getApplicationName can not find url,resourceClassName={}",resourceClassName);
                return "";
            }

            properties = new Properties();
            InputStream inputStream = null;
            try {
                inputStream = (InputStream) url.getContent();
                properties.load(inputStream);
                propMap.put(resourceClass, properties);
            } catch (IOException e) {
                logger.error("RestUtil getApplicationName load inputStream error,resourceClassName={}",resourceClassName, e);
            }
            if (inputStream == null) {
                throw new RuntimeException("RestUtil getApplicationName not find api.properties," +
                        "resourceClassName=" + resourceClassName);
            }
            String appName =properties.getProperty(APPLICATION_NAME);
            return appName;
        }
    }

    private static String simplePath(String path, String replace) {

        if (!StringUtils.isBlank(replace) && path.endsWith(replace)) {
            path = path.substring(0, path.length() - replace.length());
        }
        if (path.lastIndexOf("!/") != -1) {
            path = path.substring(0, path.lastIndexOf("!/"));
        }
        return path;
    }
}
