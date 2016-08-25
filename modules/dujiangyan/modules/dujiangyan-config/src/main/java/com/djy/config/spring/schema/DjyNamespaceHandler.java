package com.djy.config.spring.schema;

import com.djy.config.ApplicationBean;
import com.djy.config.ReferenceBean;
import com.djy.config.RegistryBean;
import com.djy.config.ServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义spring-xml配置元素解析处理
 */
public class DjyNamespaceHandler extends NamespaceHandlerSupport {

    private static Logger logger = LoggerFactory.getLogger(DjyNamespaceHandler.class);

    private static Map<String,Class> elementNameMap = new HashMap<String,Class>();

    static
    {
        elementNameMap.put("application",ApplicationBean.class);
        elementNameMap.put("registry",RegistryBean.class);
        elementNameMap.put("service",ServiceBean.class);
        elementNameMap.put("reference",ReferenceBean.class);
    }

    public void init() {

        int eleNameCnt = (elementNameMap==null)?0:elementNameMap.size();
        logger.info("DjyNamespaceHandler init start..............eleNameCnt="+eleNameCnt);
        if(eleNameCnt>0)
        {
            for(String key:elementNameMap.keySet())
            {
                registerBeanDefinitionParser(key, new DjyBeanDefinitionParser(elementNameMap.get(key)));
            }
        }
    }
}
