package com.mountain.config.spring.schema;

import com.mountain.config.ApplicationBean;
import com.mountain.config.ReferenceBean;
import com.mountain.config.RegistryBean;
import com.mountain.config.ServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义spring-xml配置元素解析器
 */
public class DjyBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static Logger logger = LoggerFactory.getLogger(DjyBeanDefinitionParser.class);

    private Class<?> beanClass;

    public static List<String> depends = new ArrayList();

    public DjyBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    protected Class<?> getBeanClass() {
        return beanClass;
    }

    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        logger.info("DjyBeanDefinitionParser doParse start.........");

        bean.setLazyInit(false);
        String id = element.getAttribute("id");

        if (ApplicationBean.class.equals(beanClass)) {
            bean.addPropertyValue("id", id);
            bean.addPropertyValue("owner", element.getAttribute("owner"));
            depends.add(id);

            logger.info("DjyBeanDefinitionParser doParse application id -->" + id );
        } else if (RegistryBean.class.equals(beanClass)) {
            bean.addPropertyValue("id", id);
            bean.addPropertyValue("address", element.getAttribute("address"));
            bean.addPropertyValue("timeout", element.getAttribute("timeout"));
            bean.addPropertyValue("protocol", element.getAttribute("protocol"));
            for(String dependBeanName : depends){
                bean.addDependsOn(dependBeanName);
            }
            depends.add(id);
            logger.info("DjyBeanDefinitionParser doParse registry id -->" + id );
        }  else if (ServiceBean.class.equals(beanClass)) {
            bean.addPropertyValue("id", id);
            bean.addPropertyValue("ref", element.getAttribute("ref"));
            bean.addPropertyValue("api", element.getAttribute("api"));
            bean.addPropertyValue("port", element.getAttribute("port"));
            bean.addPropertyValue("version", element.getAttribute("version"));
            bean.addPropertyValue("owner", element.getAttribute("owner"));
            bean.addPropertyValue("registry", element.getAttribute("registry"));
            bean.addPropertyValue("timeout", element.getAttribute("timeout"));
            bean.addPropertyValue("note", element.getAttribute("note"));
            for(String dependBeanName : depends){
                bean.addDependsOn(dependBeanName);
            }
            logger.info("DjyBeanDefinitionParser doParse service id -->" + id );
        } else if (ReferenceBean.class.equals(beanClass)) {
            bean.addPropertyValue("id", id);
            bean.addPropertyValue("api", element.getAttribute("api"));
            bean.addPropertyValue("sid", element.getAttribute("sid"));
            bean.addPropertyValue("version", element.getAttribute("version"));
            bean.addPropertyValue("timeout", element.getAttribute("timeout"));
            bean.addPropertyValue("registry", element.getAttribute("registry"));
            bean.addPropertyValue("owner", element.getAttribute("owner"));
            for(String dependBeanName : depends){
                bean.addDependsOn(dependBeanName);
            }
            logger.info("DjyBeanDefinitionParser doParse reference id -->" + id );
        }
    }
}
