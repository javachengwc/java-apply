package com.flower.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class FlowerConfig {
	protected static final Logger logger = Logger.getLogger(FlowerConfig.class);


	public static FlowerConfig parse(File configFile) {
		FlowerConfig config = new FlowerConfig();
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new InputSource(new FileInputStream(configFile)));
		} catch (FileNotFoundException e) {
			// impossible
		} catch (DocumentException e) {
			throw new ConfigurationException("config file parse error", e);
		}
		if(document != null){
			Element rootElement = document.getRootElement();
			Iterator<?> ie = rootElement.elementIterator();
			while(ie.hasNext()){
				Element element = (Element) ie.next();
				logger.debug("parsing..." + element.getName());
				if("action-package".equals(element.getName())){
					config.actionPackages = parseActionPackages(element);
				}else if("interceptors".equals(element.getName())){
					config.interceptorConfigs = parseInterceptors(element);
				}else if("namespaces".equals(element.getName())){
					config.namespaces = parseNamespaces(element);
				}
			}
		}
		return config;
	}
	
	public static FlowerConfig parse(String filePath){
		FlowerConfig config = null;
		File file = new File(filePath);
		logger.debug("loading flower configuration file...");
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
					URL url = FlowerConfig.class.getResource(filePath);
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
			config = parse(file);
		}
		return config;
	}
	
	private static List<Namespace> parseNamespaces(Element namespaceElement) {
		Iterator<?> elementIterator = namespaceElement.elementIterator();
		List<Namespace> namespaces = new ArrayList<Namespace>();
		while(elementIterator.hasNext()){
			Element element = (Element)elementIterator.next();
			String name = element.attributeValue("name");
			String packages = element.attributeValue("packages");
			Namespace ns = new Namespace(name, packages);
			namespaces.add(ns);
		}
		return namespaces;
	}

	private static List<InterceptorConfig> parseInterceptors(Element interceptorPackagesElem) {
		Iterator<?> packageInterator = interceptorPackagesElem.elementIterator();
		List<InterceptorConfig> interceptorConfig = new ArrayList<InterceptorConfig>();
		while(packageInterator.hasNext()){
			Element element = (Element)packageInterator.next();
			String className = element.attributeValue("class");
			String pattern = element.attributeValue("auto-match");
			String excludes = element.attributeValue("excludes");
			String id = element.attributeValue("id");
			InterceptorConfig config;
			config = new InterceptorConfig(pattern, className, excludes, id);
			interceptorConfig.add(config);
		}
		return interceptorConfig;
	}

	private static List<String> parseActionPackages(Element actionPackagesElem){
		Iterator<?> packageInterator = actionPackagesElem.elementIterator();
		List<String> packages = new ArrayList<String>();
		while(packageInterator.hasNext()){
			Element element = (Element)packageInterator.next();
			packages.add(element.getTextTrim());
		}
		return packages;
	}
	
	private List<String> actionPackages;
	private List<InterceptorConfig> interceptorConfigs;
	private List<Namespace> namespaces;
	
	public List<String> getActionPacages(){
		return actionPackages;
	}
	
	public List<InterceptorConfig> getInterceptorConfigs(){
		return interceptorConfigs;
	}
	
	public List<Namespace> getNamespaces(){
		return namespaces;
	}

}
