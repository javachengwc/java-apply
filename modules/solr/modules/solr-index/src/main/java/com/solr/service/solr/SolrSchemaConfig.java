package com.solr.service.solr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class SolrSchemaConfig {

	public static final Logger logger = Logger.getLogger(SolrSchemaConfig.class);
	
	public static final String GENERAL_FIELD="general_field";
	
	public static final String PRE_FIELD="pre_field";
	
	public static final String SUF_FIELD="suf_field";
	
	//field集合
	private static List<String> fields =new ArrayList<String>();
	
	//带前缀dynamicField集
	private static List<String> preFields =new ArrayList<String>();
	
	//带后缀dynamicField集
	private static List<String> sufFields =new ArrayList<String>();
	
	public static Map<String,List<String>> allFields;
	
	private static ReadWriteLock rwl = new ReentrantReadWriteLock();

	//前缀匹配
	public static Pattern prePtn = Pattern.compile("^[A-Za-z0-9_-~#]+_\\*$");
	
	//后缀匹配
	public static Pattern sufPtn = Pattern.compile("^\\*_[A-Za-z0-9.@_-~#]+");
	
	public static Map<String,List<String>> getAllFields() {
		
		rwl.readLock().lock();
		Map<String,List<String>> rt = allFields;
		rwl.readLock().unlock();
		return rt;
	}
	
	//载入初始化schema信息
	static {
		init();
	}
	
	public static void init()
	{
		rwl.writeLock().lock();
		
		fields.clear();
		preFields.clear();
		sufFields.clear();
		allFields=null;
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("solr-schema.xml");
			parse(in);
		} catch (Exception e) {
			logger.error("fail to find config file", e);
		}finally
		{
		    rwl.writeLock().unlock();
		}
	}
	
	public static void parse(InputStream in )
	{
		logger.info("SolrSchemaConfig init start.");
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(in);
		}  catch (Exception e) {
			throw new RuntimeException("config file parse error", e);
		}
		if(document != null){
			Element rootElement = document.getRootElement();
			Iterator<?> ie = rootElement.elementIterator();
			while(ie.hasNext()){
				Element element = (Element) ie.next();
				
				logger.info("parsing..." + element.getName());
				
				if("fields".equals(element.getName())){
					parseFields(element);
					break;
				}
			}
		}
		logger.info("SolrSchemaConfig init end.");
	}
	
	/**
	 * jboss下有问题
	 * @param configFile
	 */
	public static void parse(File configFile) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new InputSource(new FileInputStream(configFile)));
		}  catch (Exception e) {
			throw new RuntimeException("config file parse error", e);
		}
		if(document != null){
			Element rootElement = document.getRootElement();
			Iterator<?> ie = rootElement.elementIterator();
			while(ie.hasNext()){
				Element element = (Element) ie.next();
				
				logger.info("parsing..." + element.getName());
				
				if("fields".equals(element.getName())){
					parseFields(element);
					break;
				}
			}
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
					URL url = SolrSchemaConfig.class.getResource(filePath);
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
			logger.info("SolrSchemaConfig loaded configuration file:"+ file.getAbsolutePath());
			parse(file);
		}
	}
	
	private static void parseFields(Element fieldsElement) {
		Iterator<?> elementIterator = fieldsElement.elementIterator();
		while(elementIterator.hasNext()){
			Element element = (Element)elementIterator.next();
			if("field".equals(element.getName()))
			{
				//filed
			    String name = element.attributeValue("name");
			    fields.add(name);
			    
			}else if("dynamicField".equals(element.getName()))
			{
				//dynamicField
				String name = element.attributeValue("name");
				Matcher m = prePtn.matcher(name);
				if(m.find())
				{
					preFields.add(m.group().replace("*", ""));
				}else
				{
					m=sufPtn.matcher(name);
					if(m.find())
					{
						sufFields.add(m.group().replace("*", ""));
					}
				}
				
			}
		}
		allFields = new HashMap<String,List<String>>(3);
		allFields.put(GENERAL_FIELD, fields);
		allFields.put(PRE_FIELD, preFields);
		allFields.put(SUF_FIELD, sufFields);
		
		printFieldsInfo();
	}

	public static void printFieldsInfo()
	{
		logger.info("SolrSchemaConfig fields: "+ fields);
		logger.info("SolrSchemaConfig preFields: "+ preFields);
		logger.info("SolrSchemaConfig sufFields: "+ sufFields);
		
	}
	
	public static void main(String args [])
	{
		System.out.println("SolrSchemaConfig start.");
		SolrSchemaConfig.init();
		//SolrSchemaConfig.init("E:\\workspace\\mllsolr\\mllsolr-web\\target\\mllsolr\\WEB-INF\\classes\\solr-schema.xml");
		System.out.println("SolrSchemaConfig end.");
		
	}
	
}
