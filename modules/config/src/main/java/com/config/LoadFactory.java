package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 *  加载配置文件工厂
 */
public class LoadFactory {

    private static final Logger logger = LoggerFactory.getLogger(LoadFactory.class);

    /**文件系统加载**/
    //配置文件和jar包在同一个目录下可加载
    public static Properties loadFromFile(String name) throws Exception
    {
        //name=config.properties
        InputStream in = new FileInputStream(name);
        return loadFromInputSteam(in);
    }

    //从jar包外部目录加载配置文件
    public static Properties loadFromOutJar(String name) throws Exception
    {
        //name=config.properties 和jar包在同一个目录下
        String path =System.getProperty("user.dir") + "/"+name;
        InputStream in = new FileInputStream(path);
        return loadFromInputSteam(in);
    }

    /**
     * 类方式加载,默认从与此类同目录下的配置文件加载，
     * 当前LoadFactory类在classes/com/config目录下，那配置文件也应该在此目录下
     * 如以/开头的是指从类根目录开始加载
     *
     */
    public static Properties loadFromClasses(String name) throws Exception
    {
        InputStream in =LoadFactory.class.getResourceAsStream(name);
        return loadFromInputSteam(in);
    }


    /**
     * 类加载器方式加载
     * 此方式加载最稳定，兼容tomcat,jboss容器
     */
    public static Properties loadFromClassLoader(String name) throws Exception
    {
        //InputStream in =LoadFactory.class.getClassLoader().getResourceAsStream(name);
        //return loadFromInputSteam(in);

        Properties properties = new Properties();
        InputStreamReader in=null;
        try {
            in =new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), Charset.forName("UTF-8"));
            properties.load(in);

        } catch (Exception e) {
            logger.error("loadFromClassLoader get properties config file : " + name + " error!" + e.getMessage(), e);
        }finally {
            if(in!=null)
            {
                try{
                    in.close();
                }catch(Exception e2)
                {
                    logger.error("loadFromClassLoader close inputSteam error," , e2);
                }
            }
        }
        return properties;
    }


    public static Properties loadFromInputSteam(InputStream in) throws Exception
    {
        Properties p = new Properties();
        p.load(in);
        try {
            if (in != null)
            {
                in.close();
            }
        }catch (Exception e)
        {
            logger.error("loadFromFile close inputStream in error,",e);
        }
        return p;
    }
}
