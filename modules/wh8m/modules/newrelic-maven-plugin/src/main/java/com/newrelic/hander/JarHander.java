package com.newrelic.hander;

import com.newrelic.entity.AspectBean;
import com.newrelic.util.FileClassLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Jar类处理器
 */
public class JarHander {

    public static final Logger logger = LoggerFactory.getLogger(JarHander.class);

    private String jarPath;

    private Map<String,Map<String,String>> clazzInfo;

    public JarHander(String jarPath,Map<String,Map<String,String>> clazzInfo)
    {
        this.jarPath=jarPath;
        this.clazzInfo=clazzInfo;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public Map<String, Map<String, String>> getClazzInfo() {
        return clazzInfo;
    }

    public void setClazzInfo(Map<String, Map<String, String>> clazzInfo) {
        this.clazzInfo = clazzInfo;
    }

    public Map<String,List<AspectBean>> hander()
    {
        boolean flag=true;
        if(flag)
        {
            //return null;
        }
        try {
            ClassLoader orglClassLoader =Thread.currentThread().getContextClassLoader();
            ClassLoader appClassLoader=orglClassLoader;
            boolean change=false;
            if(!StringUtils.isBlank(jarPath))
            {
                change=true;
                ClassLoader classLoader = new FileClassLoader(jarPath);
                appClassLoader=classLoader;

            }
            Thread.currentThread().setContextClassLoader(appClassLoader);
            Map<String,List<AspectBean>> rt = new HashMap<String,List<AspectBean>>();

            for(String key:clazzInfo.keySet())
            {

                Map<String,String> innerMap = clazzInfo.get(key);
                if(innerMap!=null && innerMap.size()>0)
                {
                    List<AspectBean> list = new ArrayList<AspectBean>();
                    for(String face:innerMap.keySet()) {
                        System.out.println("-----------interface:"+face);
                        Class clazz = appClassLoader.loadClass(face);

                        AspectBean aspectBean =new AspectBean();
                        aspectBean.setFace(face);
                        aspectBean.setClassImpl(innerMap.get(face));

                        Method [] methods = clazz.getDeclaredMethods();
                        Map<String,List<String>> methodMap = new HashMap<String,List<String>>();
                        for(Method m:methods)
                        {
                            String name = m.getName();
                            System.out.println("----------method:"+name);
                            Class<?> [] params= m.getParameterTypes();
                            List<String> types = new LinkedList<String>();
                            if(params!=null && params.length>0)
                            {
                                for(Class<?> clz:params)
                                {
                                    System.out.println("-------param:"+clz.getName());
                                    types.add(clz.getName());
                                }
                            }
                            methodMap.put(name,types);

                        }
                        aspectBean.setMethods(methodMap);
                        list.add(aspectBean);
                    }
                    if(list.size()>0)
                    {
                        rt.put(key,list);
                    }
                }
            }

            if(change)
            {
                Thread.currentThread().setContextClassLoader(orglClassLoader);
            }

            return rt;

        }catch(Exception e)
        {
            logger.error("JarHander hander error,",e);
        }
        return null;
    }
}
