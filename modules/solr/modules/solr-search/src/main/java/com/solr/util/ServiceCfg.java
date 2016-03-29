package com.solr.util;

import com.solr.model.Activity;
import com.solr.model.Price;
import com.solr.model.UrlParameter;
import com.solr.model.Config;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCfg {

    private  static Logger log = LoggerFactory.getLogger(ServiceCfg.class);

	private static Map<String,Config> configs = new HashMap<String, Config>();

    private final static String serviceCfgPath="service.cfg.xml";

    public static void init()
    {
        getXmlNodeValue(serviceCfgPath);
    }


	@SuppressWarnings("unchecked")
	public static void getXmlNodeValue(String path) {
		Document readDoc = null;
		SAXReader saxReader = new SAXReader();
		try {
			//从当前线程的classLoader中读取配置文件
			InputStream fileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);//new FileInputStream(path);
			readDoc = saxReader.read(fileStream);
			readDoc.setXMLEncoding("utf-8");
			Element rootElement = readDoc.getRootElement();
			List<Element> list = rootElement.elements();
			if(list.size()>0){
				configs.clear();
			}
			for (Element entry : list) {
				if (entry.attributes().size() <= 0) {
					continue;
				}
				Attribute attr = (Attribute) entry.attributes().get(0);
				String serverName = attr.getValue();
				if (StringUtils.isBlank(serverName)) {
					continue;
				}
				Config config = new Config(serverName);
				configs.put(serverName, config);
				
				config.getServiceParameter().put("solrRequestUrl",entry.attributeValue("solrRequestUrl"));
				config.getServiceParameter().put("pageSize",entry.attributeValue("pageSize"));
				List<Element> sublist = entry.elements();
				if (sublist.size() == 0) {
					continue;
				}
				for (Element element : sublist) {
					// 获取url下面的field字段
					if ("url".equals(element.getName())) {
						List<Element> filedList = element.elements();
						for (Element filed : filedList) {
							List<Attribute> fieldAttributes = filed.attributes();
							UrlParameter paramter = new UrlParameter();
							for (Attribute attributes : fieldAttributes) {
								// 利用BeanUtils,设置bean的值
								BeanUtils.setProperty(paramter, attributes.getName(), attributes.getValue());
							}
							config.getUrlParameter().add(paramter);
						}
					} else if ("facet".equals(element.getName())) {
						List<Element> filedList = element.elements();
						for (Element filed : filedList) {
							List<Attribute> fieldAttributes = filed.attributes();
							config.getFacetField().add(fieldAttributes.get(0).getValue());

						}
                      }else if("default-sort".equals(element.getName())){
                    	  config.getDefaultSort().put("sort",element.attribute("sort").getValue());
                    	  config.getDefaultSort().put("order",element.attribute("order").getValue());
                      
                      }else if("prices".equals(element.getName())){
                    	List<Element> filedList = element.elements();
  						for (Element filed : filedList) {
  							List<Attribute> fieldAttributes = filed.attributes();
  							Price price = new Price();
  							for (Attribute attributes : fieldAttributes) {
  								// 利用BeanUtils,设置bean的值
  								BeanUtils.setProperty(price, attributes.getName(), attributes.getValue());
  							}
  							config.getPriceList().add(price);
  						}
                      }else if("activitys".equals(element.getName())){
                    	List<Element> filedList = element.elements();
  						for (Element filed : filedList) {
  							Activity activity=new Activity();
  							List<Attribute> fieldAttributes = filed.attributes();
  							for (Attribute attributes : fieldAttributes) {
  								// 利用BeanUtils,设置bean的值
  								BeanUtils.setProperty(activity, attributes.getName(), attributes.getValue());
  							}
  							config.getActivitys().add(activity);
  						}
                      }else if("sort_field".equals(element.getName())){
                      	List<Element> filedList = element.elements();
    						for (Element filed : filedList) {
    							List<Attribute> fieldAttributes = filed.attributes();
    							for (Attribute attributes : fieldAttributes) {
    								// 利用BeanUtils,设置bean的值
    								config.getSortList().add(attributes.getValue());
    							}
    						}
                        }
                   }
			}
			readDoc.clearContent();// 清空文档内容
		} catch (Exception e) {
			log.error(log.getName() + "\n" + e.getMessage() + "\nxml源数据加载异常!");
		}
	}

	public static List<UrlParameter> getUrlParameterByName(String serverName) {
		if(null!=configs.get(serverName).getUrlParameter()){
			return configs.get(serverName).getUrlParameter();
		}
		return null;
	}

	public static List<String> getFacetFieldByName(String serverName) {
		if(null!=configs.get(serverName).getFacetField()){
			return configs.get(serverName).getFacetField();
		}
		return null;
	}

	public static Map<String, String> getDefaultSortByName(String serverName) {
		if(null!=configs.get(serverName).getDefaultSort()){
			return configs.get(serverName).getDefaultSort();
		}
		return null;
	}

	public static List<Price> getPriceListByName(String serverName) {
		if(null!=configs.get(serverName).getPriceList()){
			return configs.get(serverName).getPriceList();
		}
		return null;
	}

	public static List<Activity> getActivitysByName(String serverName) {
		if(null!=configs.get(serverName).getActivitys()){
			return configs.get(serverName).getActivitys();
		}
		return null;
	}
	
	public static Activity getActivityByNameAndIndex(String serverName,int index) {
		if(null!=configs.get(serverName)&&null!=configs.get(serverName).getActivitys()){
			return configs.get(serverName).getActivitys().get(index);
		}
		return null;
	}
	
	public static int getActivityIndexByName(String serverName,String field_name) {
		if(null!=configs.get(serverName)&&null!=configs.get(serverName).getActivitys()){
			int i = 0;
			for(Activity activity:configs.get(serverName).getActivitys()){
				if(activity.getName().equals(field_name)){
					return (i+1);
				}
				i++;
			}
		}
		return 0;
	}

	public static Map<String, String> getServiceParameterByName(String serverName) {
		if(null!=configs.get(serverName).getServiceParameter()){
			return configs.get(serverName).getServiceParameter();
		}
		return null;
	}
	
	public static final List<String> getSortFieldsByName(String serverName){
		if(null!=configs.get(serverName).getSortList()){
			return configs.get(serverName).getSortList();
		}
		return null;
	}
}
