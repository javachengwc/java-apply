package com.newrelic.hander;

import com.newrelic.entity.Bean;
import com.newrelic.entity.DubboService;
import com.newrelic.entity.LmService;
import com.newrelic.util.XmlParse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * xml信息处理器
 */
public class XmlHander {

    public static final Logger logger = LoggerFactory.getLogger(XmlHander.class);

    private String xmlPath;

    public XmlHander(String xmlPath)
    {
       this.xmlPath=xmlPath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    /**
     * 解析路径
     * @return
     */
    public String[] parsePath()
    {
        //带有模糊路径的情况待扩展
        String [] paths = xmlPath.split(",");
        List<String> pathList = new ArrayList<String>();
        for(String  path: paths)
        {
            if(path.endsWith(".xml"))
            {
                pathList.add(path);
            }else
            if(path.endsWith("*"))
            {
                path = path.replace("*","");
                List<String> findList = findPath(path,true);
                if(findList!=null && findList.size()>0) {
                    pathList.addAll(findList);
                }

            }else
            {
                List<String> findList = findPath(path,false);
                if(findList!=null && findList.size()>0) {
                    pathList.addAll(findList);
                }
            }
        }
        return  pathList.toArray(new String [pathList.size()]);
    }

    public List<String> findPath(String path,boolean recursive)
    {
        File dir = new File(path);
        if(!dir.exists() )
        {
            return null;
        }
        List<String> rt = new ArrayList<String>();
        if(!dir.isDirectory() && path.endsWith(".xml"))
        {
            rt.add(path);
            return rt;
        }
        if(!dir.isDirectory())
        {
            return null;
        }
        File [] files = dir.listFiles();
        if(files!=null && files.length>0  )
        {
            for(File per:files)
            {
                if(per.getPath().endsWith(".xml"))
                {
                    rt.add(per.getPath());
                }else
                if(per.isDirectory() && recursive)
                {
                    _findPath(per.getPath(),recursive,rt);
                }
            }
        }
        return rt;
    }

    private void _findPath(String path,boolean recursive,List<String> result)
    {
        File dir = new File(path);
        if(!dir.exists() )
        {
            return;
        }
        if(!dir.isDirectory() && path.endsWith(".xml"))
        {
           result.add(path);
            return;
        }
        if(!dir.isDirectory())
        {
            return;
        }
        File [] files = dir.listFiles();
        if(files!=null && files.length>0  )
        {
            for(File per:files)
            {
                if(per.getPath().endsWith(".xml"))
                {
                    result.add(per.getPath());
                }else
                if(per.isDirectory() && recursive)
                {
                    _findPath(per.getPath(),recursive,result);
                }
            }
        }
    }

    /**
     * 返回lm,dubbo的接口名和实现类名
     * @return
     */
    public Map<String,Map<String,String>> hander()
    {
        String [] paths = parsePath();
        System.out.println("paths:"+paths);
        for(String path:paths)
        {
            System.out.println("path:"+path);
        }
        Map<String,Object> map = XmlParse.parseAll(paths);
        if(map==null || map.size()<=0)
        {
            return null;
        }
        Set<LmService> lms= (map.get("lm")==null)?null:(Set<LmService>)(map.get("lm"));
        Set<DubboService> dubbos = (map.get("dubbo")==null)?null:(Set<DubboService>)(map.get("dubbo"));
        Set<Bean> beans =(map.get("bean")==null)?null:(Set<Bean>)(map.get("bean"));

        Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();

        Map<String,String> lmMap = parseLm(lms, beans);
        result.put("lm",lmMap);

        Map<String,String> dubboMap = parseDubbo(dubbos,beans);
        result.put("dubbo",dubboMap);
        return result;
    }

    public Map<String,String> parseLm(Set<LmService> lms,Set<Bean> beans)
    {
        Map<String,String> lmMap = new HashMap<String,String>();
        if(lms!=null  && beans!=null )
        {
            for(LmService lm:lms)
            {
                String face = lm.getApi()+"$Iface";
                String ref = lm.getRef();
                String clazz="";
                for(Bean bean:beans)
                {
                    if(ref.equals(bean.getId()))
                    {
                        clazz = bean.getClazz();
                        break;
                    }
                }
                if(StringUtils.isBlank(clazz))
                {
                    logger.error("@@@@@@@@@@ XmlHander hander parseLm:"+lm+",not find matched bean");
                    continue;
                }
                lmMap.put(face,clazz);
            }
        }
        return lmMap;
    }

    public Map<String,String> parseDubbo(Set<DubboService> dubbos,Set<Bean> beans)
    {
        Map<String,String> dubboMap = new HashMap<String,String>();
        if(dubbos!=null  && beans!=null )
        {
            for(DubboService dubbo:dubbos)
            {
                String face =dubbo.getFace();
                String ref = dubbo.getRef();
                String clazz="";
                for(Bean bean:beans)
                {
                    if(ref.equals(bean.getId()))
                    {
                        clazz = bean.getClazz();
                        break;
                    }
                }
                if(StringUtils.isBlank(clazz))
                {
                    logger.error("@@@@@@@@@@ XmlHander hander parseDubbo:"+dubbo+",not find matched bean");
                    continue;
                }
                dubboMap.put(face,clazz);
            }
        }
        return dubboMap;
    }

}
