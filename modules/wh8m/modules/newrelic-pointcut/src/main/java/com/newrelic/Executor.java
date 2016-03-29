package com.newrelic;

import com.newrelic.entity.AspectBean;
import com.newrelic.hander.JarHander;
import com.newrelic.hander.ResultHander;
import com.newrelic.hander.XmlHander;

import java.util.*;

/**
 * 生成newrelic extension xml文件的总执行器
 */
public class Executor {

    public static final String [] FLAGS={"all","lm","dubbo"};

    //xml文件路径
    private String xmlPath;

    //jar包路径
    private String jarPath;

    //结果标记
    private String resultFlag=FLAGS[0];

    //输出的xml文件路径
    private String outPath="";

    public Executor()
    {

    }
    public Executor(String xmlPath,String resultFlag)
    {
        this.xmlPath=xmlPath;
        this.resultFlag=resultFlag;
    }

    public Executor(String xmlPath,String resultFlag,String jarPath,String outPath)
    {
        this(xmlPath,resultFlag);
        this.jarPath=jarPath;
        this.outPath=outPath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    //入口
    public void exe()
    {
        //从xml收集配置信息
        Map<String,Map<String,String>> map = new XmlHander(xmlPath).hander();

//        printXmlHanderRt(map);

        //根据配置信息收集对应的类,方法信息
        Map<String,List<AspectBean>> aspectMap = new JarHander(jarPath,map).hander();

//        if(aspectMap==null || aspectMap.size()<=0)
//        {
//            aspectMap=generalData();
//        }
        //根据类，方法信息生成切入点配置文件
        new ResultHander(resultFlag,outPath,aspectMap).hander();
    }

    public void printXmlHanderRt(Map<String,Map<String,String>> map)
    {
        System.out.println("----------------XmlHander result:\r\n");
        for(String key:map.keySet())
        {
            System.out.println(key+":\r\n");
            Map<String,String> innerMap= map.get(key);
            if(innerMap!=null)
            {
                for(String a:innerMap.keySet())
                    System.out.println(a+":"+innerMap.get(a));
            }
        }
        System.out.println("---------------------------------");
    }

    public  Map<String,List<AspectBean>> generalData()
    {
        Map<String,List<AspectBean>> map = new HashMap<String,List<AspectBean>>();
        List<AspectBean> list = new ArrayList<AspectBean>();
        AspectBean aspectBean = new AspectBean();
        aspectBean.setFace("com.service.finagle$Iface");
        aspectBean.setClassImpl("com.service.finagle.DataServiceImpl");
        Map<String, List<String>> methods = new HashMap<String, List<String>>();
        List<String> types = new LinkedList<String>();
        types.add("int");
        methods.put("method1",types);

        methods.put("method2",types);

        List<String> types3 = new LinkedList<String>();
        types3.add("int");
        types3.add("int");
        methods.put("method3",types3);

        List<String> types4 = new LinkedList<String>();
        types4.add("java.lang.String");
        methods.put("method4",types4);

        List<String> types5 = new LinkedList<String>();
        types5.add("java.lang.String");
        types5.add("int");
        types5.add("int");
        methods.put("mothod5",types5);

        aspectBean.setMethods(methods);
        list.add(aspectBean);
        map.put("lm",list);
        map.put("dubbo",list);
        return map;
    }
}
