package com.newrelic.util;

import com.newrelic.entity.Bean;
import com.newrelic.entity.DubboService;
import com.newrelic.entity.LmService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 解析xml
 */
public class XmlParse {

    public static final Logger logger = LoggerFactory.getLogger(XmlParse.class);

    public static Document getDoc(InputStream in)
    {
        logger.info("XmlParse getDoc start.");
        SAXReader saxReader= new SAXReader();
        Document document =null;
        try{
            document = saxReader.read(in);
        }
        catch(Exception e)
        {
            logger.error("XmlParse getDoc error",e);
            throw new RuntimeException("XmlParse getDoc error",e);
        }
        return document;

    }

    public static Map<String,Object> parse(String path)
    {
        try{
            InputStream in =new FileInputStream( new File(path));
            return parse(in);
        }catch(Exception e)
        {
            logger.error("XmlParse parse error,fail to find file path="+path,e);
        }
        return null;
    }

    public static Map<String,Object> parse(InputStream in)
    {
        logger.info("XmlParse parse inputstream start.");
        SAXReader saxReader= new SAXReader();
        Document document =null;
        try{
            document = saxReader.read(in);
        }
        catch(Exception e)
        {
            logger.error("XmlParse parse inputstream error",e);
            throw new RuntimeException("XmlParse parse inputstream error",e);
        }
        if(document!=null)
        {
            Set<Bean> beanSet = new HashSet<Bean>();
            Set<DubboService> dubboSet = new HashSet<DubboService>();
            Set<LmService> lmSet = new HashSet<LmService>();
            //beans
            Element rootElement= document.getRootElement();
            Iterator<?> ie = rootElement.elementIterator();
            while(ie.hasNext())
            {
                Element element = (Element)ie.next();
                System.out.println("----------"+element.getName());
                if("bean".equals(element.getName()))
                {
                    Bean bean = new Bean();
                    bean.setId(element.attributeValue("id"));
                    bean.setClazz(element.attributeValue("class"));

                    beanSet.add(bean);
                }

                //dubbo:service
                if("service".equals(element.getName()) && "dubbo".equals(element.getNamespacePrefix()) )
                {

                    DubboService dubboBean = new DubboService();
                    dubboBean.setFace(element.attributeValue("interface"));
                    dubboBean.setRef(element.attributeValue("ref"));

                    dubboSet.add(dubboBean);
                }
                //lm:service
                if("service".equals(element.getName()) && "lm".equals(element.getNamespacePrefix()))
                {
                    LmService lmBean = new LmService();
                    lmBean.setId(element.attributeValue("id"));
                    lmBean.setRef(element.attributeValue("ref"));
                    lmBean.setApi(element.attributeValue("api"));

                    lmSet.add(lmBean);
                }
            }
            Map<String,Object> map =new HashMap<String,Object>();
            map.put("bean",beanSet);
            map.put("lm",lmSet);
            map.put("dubbo",dubboSet);

            return map;
        }
        return null;
    }

    public static Map<String,Object> parseAll(String [] paths) {
        if (paths == null || paths.length <= 0) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Bean> beans = new HashSet<Bean>();
        Set<LmService> lms = new HashSet<LmService>();
        Set<DubboService> dubbos = new HashSet<DubboService>();
        for (String path : paths) {
            Map<String, Object> per = XmlParse.parse(path);
            if (per == null) {
                continue;
            }
            for (String key : per.keySet()) {
                Object obj = per.get(key);
                if (obj == null) {
                    continue;
                }
                if ("bean".equals(key)) {
                    Set<Bean> perBeanSet = (Set<Bean>) obj;
                    beans.addAll(perBeanSet);
                }
                if ("lm".equals(key)) {
                    Set<LmService> perLmSet = (Set<LmService>) obj;
                    lms.addAll(perLmSet);
                }
                if ("dubbo".equals(key)) {
                    Set<DubboService> perDubboSet = (Set<DubboService>) obj;
                    dubbos.addAll(perDubboSet);
                }
            }

        }
        map.put("bean",beans);
        map.put("lm",lms);
        map.put("dubbo",dubbos);
        return map;
    }

}
