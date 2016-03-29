package com.newrelic.hander;

import com.newrelic.entity.AspectBean;
import com.newrelic.util.XmlParse;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * 结果处理器
 */
public class ResultHander {

    public static final Logger logger = LoggerFactory.getLogger(ResultHander.class);

    private String resultFlag;

    private String outPath;

    private Map<String,List<AspectBean>> aspectMap;

    public ResultHander(String resultFlag,String outPath,Map<String,List<AspectBean>> aspectMap)
    {
        this.resultFlag= resultFlag;
        this.outPath=outPath;
        this.aspectMap=aspectMap;
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

    public Map<String, List<AspectBean>> getAspectMap() {
        return aspectMap;
    }

    public void setAspectMap(Map<String, List<AspectBean>> aspectMap) {
        this.aspectMap = aspectMap;
    }

    public Document getTemplate()
    {
         return XmlParse.getDoc(Thread.currentThread().getContextClassLoader().getResourceAsStream("extension-template.xml"));
    }

    public String getRealOutPath()
    {
        if(!StringUtils.isBlank(outPath))
        {
            return outPath;
        }
        String resourcePath = Thread.currentThread().getContextClassLoader().getResource("extension-template.xml").getPath();
        resourcePath=resourcePath.substring(0,resourcePath.lastIndexOf("/"));
        return resourcePath+"/out";
    }

    //是否需要输出
    public boolean ableOut(String type)
    {
        if(StringUtils.isBlank(resultFlag) || "all".equalsIgnoreCase(resultFlag))
        {
            return true;
        }
        String []  types = resultFlag.split(",");
        for(String per:types)
        {
            if(type.equalsIgnoreCase(per))
            {
                return true;
            }
        }
        return false;
    }

    //返回输出文件名
    public String getOutName(String type)
    {
        return type+"-service.xml";
    }

    public void hander()
    {
         if(aspectMap==null || aspectMap.size()<=0)
         {
             return;
         }
         //Document doc = getTemplate();
         //if(doc==null)
         //{
         //    logger.error("********** ResultHander hander template doc is null");
         //    return;
         //}
         for(String type:aspectMap.keySet())
         {
             System.out.println("----------------type:"+type);
             if(!ableOut(type))
             {
                continue;
             }
             List<AspectBean> list = aspectMap.get(type);
             if(list==null || list.size()<=0)
             {
                 continue;
             }
             Document outDoc =DocumentHelper.createDocument();
             //Element root = (Element) doc.getRootElement().clone();
             //outDoc.add(root);
             Element root = outDoc.addElement("extension");
             String spaceUrl="https://newrelic.com/docs/java/xsd/v1.0";
             Namespace namespace =new Namespace("",spaceUrl);
             root.add(namespace);
             //"","https://newrelic.com/docs/java/xsd/v1.0"
             root.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
             root.addAttribute("xsi:schemaLocation","newrelic-extension extension.xsd ");
             root.addAttribute("version","1.0");
             root.addAttribute("enabled","true");

             root.addAttribute("name",type+"-service");

             Element inst = root.addElement("instrumentation",spaceUrl);
             for(AspectBean aspectBean:list)
             {
                  if(aspectBean.getMethods()==null || aspectBean.getMethods().size()<=0)
                  {
                      continue;
                  }
                  Element pointcut= inst.addElement("pointcut");
                  pointcut.addAttribute("transactionStartPoint","true");
                  Element clazzEle =pointcut.addElement("className");
                  clazzEle.addText(aspectBean.getClassImpl());
                  for(String method:aspectBean.getMethods().keySet())
                  {
                      Element methodEle =pointcut.addElement("method");
                      Element nameEle = methodEle.addElement("name");
                      nameEle.addText(method);
                      Element paramsEle = methodEle.addElement("parameters");
                      List<String> types = aspectBean.getMethods().get(method);
                      if(types!=null &&types.size()>0)
                      {
                          for(String per:types)
                          {
                              Element typeEle = paramsEle.addElement("type");
                              typeEle.addText(per);
                          }
                      }
                  }
             }
             outXml(outDoc,getRealOutPath(),getOutName(type));
         }

    }

    //输出
    public void outXml(Document doc,String path,String fileName)
    {
       File dir =null;
       try {
           dir = new File(path);
           if (!dir.exists()) {
               dir.mkdir();
           }

       }catch(Exception e)
       {
           logger.error("ResultHander outXml error,",e);
           return ;
       }
       if(dir==null || !dir.isDirectory())
       {
           logger.error("ResultHander outXml out dir is null or dir is not Directory");
           return;
       }
       try {
           OutputFormat format = OutputFormat.createPrettyPrint();//缩减型格式
           format.setEncoding("UTF-8");//设置文件内部文字的编码
           format.setExpandEmptyElements(true);
           format.setTrimText(false);
           format.setIndent(true);      // 设置是否缩进
           format.setIndent("   ");     // 以空格方式实现缩进
           format.setNewlines(true);    // 设置是否换行

           File out = new File(dir, fileName);
           System.out.println("---------- ResultHander out path:"+out.getPath());
           String encoding = "UTF-8";//设置文件的编码！！和format不是一回事
           OutputStreamWriter outstream = new OutputStreamWriter(new FileOutputStream(out), encoding);
           XMLWriter writer = new XMLWriter(outstream,format);

           writer.write(doc);
           writer.close();
       }catch(Exception e)
       {
           logger.error("ResultHander outXml write out error",e);
       }
    }
}
