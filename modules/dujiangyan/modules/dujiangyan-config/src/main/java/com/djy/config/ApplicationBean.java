package com.djy.config;

import com.djy.config.spring.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * application节点
 */
public class ApplicationBean implements InitializingBean ,ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(ApplicationBean.class);

    //应用名，供其他模块使用
    public static String applicationName;

    //应用负责人，供其他模块使用
    public static String applicationOwner;

    //应用id(名称),只是一个标识
    private String id;

    //应用负责人
    private String owner = "";

    private transient ApplicationContext applicationContext;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void init() {
        applicationName = this.id;
        applicationOwner = this.owner;
        logger.info("ApplicationBean init application [" + applicationName + "] 加载设置成功, 这是唯一的应用名,他很重要, 请不要和其他应用的名字起的一样, 应用负责人 ["+applicationOwner+"] !");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (applicationContext != null) {
            SpringContextUtils.setApplicationContext(applicationContext);
        }
    }

}
