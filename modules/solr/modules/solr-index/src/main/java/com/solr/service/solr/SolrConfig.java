package com.solr.service.solr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import com.util.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrConfig {
	
	private static Properties solrProperties = new Properties();
	
	public static final Logger logger = LoggerFactory.getLogger(SolrConfig.class);

	static {
		try {
			logger.info("SolrConfig static init start.");
			//InputStream in = SolrConfig.class.getResourceAsStream("/solr.properties");
			//solrProperties.load(in);
			//in.close();
            solrProperties = PropertiesLoader.load("solr.properties");
		} catch (Exception e) {
			logger.error("fail to find config file /solr.properties", e);
		}
	}

	/**
	 * /**
	 * jboss下有问题
	 * @param configFile
	 */
	public static void parse(File configFile) {
		
		try {
			InputStream in = new FileInputStream(configFile);
			solrProperties.load(in);
			in.close();
		} catch (Exception e) {
			logger.error("fail to find config file"+configFile.getPath() +",name="+configFile.getName(), e);
		}
	}
	
	public static void parse(String filePath){
		
		File file = new File(filePath);
		
		logger.debug("loading configuration file...");
		if(!file.exists()){
			Enumeration<URL> resources = null;
			try {
				resources = Thread.currentThread().getContextClassLoader().getResources(filePath);
			} catch (IOException e) {
				logger.error("fail to find config file", e);
			}
			if(resources != null && resources.hasMoreElements()){
				try {
					file = new File(resources.nextElement().toURI());
				} catch (URISyntaxException e) {
					logger.error("fail to find config file", e);
				}
			}
			
			if(!file.exists()){
				try {
					URL url = SolrConfig.class.getResource(filePath);
					if(url != null){
						file = new File(url.toURI());
					}
				} catch (URISyntaxException e) {
					logger.error("fail to find config file", e);
				}
			}
		}
		if(!file.exists()){
			throw new IllegalArgumentException("config file doesn't exists");
		}else{
			logger.info("SolrConfig loaded configuration file:"+ file.getAbsolutePath());
			parse(file);
		}
	}
	
	
	public static String getValue(String key) {
		return solrProperties.get(key).toString();
	}

	public static String getServers() {
		return getValue("solr.servers");
	}
	public static String getMaxActive()
	{
		return getValue("solr.maxActive");
	}
	public static String getTimeout()
	{
		return getValue("solr.timeout");
	}
    public static String getConnectTimeout()
    {
    	return getValue("solr.connectTimeout");
    }
	
}
