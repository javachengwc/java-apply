package com.app.jersey;

import com.app.annotation.RestService;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.Map;

@Component
public class JerseyConfig extends ResourceConfig implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //设定jersey配置
    public JerseyConfig() {

        register(MultiPartFeature.class);
        //注册json功能
        register(JacksonFeature.class);
        //注册日志
        //register(LoggingFilter.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext=applicationContext;
        Map<String, Object> restServieMap = applicationContext.getBeansWithAnnotation(RestService.class);
        int restServiceCnt = restServieMap==null?0:restServieMap.size();
        logger.info("JerseyConfig restService count:"+restServiceCnt);
        if(restServiceCnt<=0 )
        {
            return;
        }
        for (Object restObj : restServieMap.values()) {
            Class<?> restClass = restObj.getClass();
            Class<?>[] interfaces = restClass.getInterfaces();
            Class<?> restface = getRestInterfaceWithPath(interfaces);

            if (restface != null) {
                //加载resource
                register(restObj);
                Path path = restface.getDeclaredAnnotation(Path.class);
                String pathValue =path.value();
                logger.info("JerseyConfig register restService ["+restClass.getSimpleName()+"],path="+pathValue);
            }else
            {
                logger.info("JerseyConfig find restService,but not implements interface,restService="+restClass.getSimpleName());
            }
        }
    }

    private Class<?> getRestInterfaceWithPath(Class<?>[] interfaces) {
        if (interfaces==null || interfaces.length <= 0) {
            return null;
        }
        for (Class<?> clazz : interfaces) {
            if(clazz==null)
            {
                continue;
            }
            if (clazz.getAnnotation(Path.class) != null) {
                return clazz;
            }
        }
        return null;
    }
}
