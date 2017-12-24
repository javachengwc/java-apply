package com.component.rest.springmvc;

import com.component.rest.springmvc.config.RestConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ComponentScan
@EnableScheduling
public class ApplicationStarter {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStarter.class);

    private static volatile AnnotationConfigEmbeddedWebApplicationContext instance;

    public static void run(Class<?> assemblyClass, String[] args) {

        Object[] sourcesClass =new Class[]{ApplicationStarter.class, assemblyClass};

        if (instance == null) {
            synchronized (ApplicationStarter.class) {
                if (instance == null) {
                    logger.info("ApplicationStarter start init instance");
                    SpringApplicationBuilder builder = new SpringApplicationBuilder(sourcesClass).sources(RestConfig.class);
                    builder.application().addListeners(new EnvironmentPreparedListener());
                    instance = (AnnotationConfigEmbeddedWebApplicationContext)builder.run(args);
                    instance.registerShutdownHook();
                } else {
                    logger.warn("ApplicationStarter instance exists ");
                }
            }
        } else {
            logger.warn("ApplicationStarter instance exists");
        }
    }

    public static class EnvironmentPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

        public static Map<String, String> commonProp = new ConcurrentHashMap<String, String>();

        @Override
        public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
            generateProperties();
            if(!commonProp.isEmpty()){
                event.getEnvironment().getPropertySources().addFirst(new EnumerablePropertySource<String>("commonProp"){
                    public Object getProperty(String name) {
                        return commonProp.get(name);
                    }

                    public String[] getPropertyNames() {
                        return commonProp.keySet().toArray(new String[commonProp.size()]);
                    }
                });
            }
        }

        private void generateProperties(){
            try {
                List<String> apiNames = new ArrayList<String>();
                Enumeration<URL> apiProperties = ClassUtils.getDefaultClassLoader().getResources("META-INF/api.properties");
                while(apiProperties.hasMoreElements()){
                    Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(apiProperties.nextElement()));
                    apiNames.add(properties.get("application.name").toString());
                }
                commonProp.put("eureka.instance.metadataMap.static.dependencies", StringUtils.join(apiNames, ","));
            } catch (IOException e) {
                logger.warn("ApplicationStarter generateProperties error,",e);
            }
        }
    }
}

