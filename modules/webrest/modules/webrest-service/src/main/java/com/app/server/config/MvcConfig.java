package com.app.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此配置让jersey的rest服务也能支持mvc http接口服务
 */
@Configuration
@ConditionalOnProperty(value = "springmvc.enabled", matchIfMissing = true)
public class MvcConfig {

    private static Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    private static String SPEC_PATH="/*";

    @Bean
    public ServletRegistrationBean servletRegistration(DispatcherServlet servlet, ApplicationContext applicationContext) {
        ServletRegistrationBean registration = new ServletRegistrationBean(servlet);
        Map<String,RequestMappingHandlerMapping> handlerMap = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
        List<String> paths=new ArrayList<String>();//所有的mvc接口访问路径
        int mapSize =handlerMap==null?0:handlerMap.size();
        logger.info("MvcConfig servletRegistration handlerMap size="+mapSize);
        for(String key:handlerMap.keySet()) {
            RequestMappingHandlerMapping mapping= handlerMap.get(key);
            mapping.setAlwaysUseFullPath(true);
            Map<RequestMappingInfo,HandlerMethod> methodsMap= mapping.getHandlerMethods();
            for(RequestMappingInfo info:methodsMap.keySet()) {
                HandlerMethod handlerMethod =methodsMap.get(info);
                String method =handlerMethod.getMethod().getName();
                PatternsRequestCondition patternsCondition=info.getPatternsCondition();
                Set<String> patterns =info.getPatternsCondition().getPatterns();
                logger.info("MvcConfig handlerMap methodsMap handler={},method={},patternCdn={}",key,method,patternsCondition.toString());
                paths.addAll(patterns);
            }
        }

        Pattern pattern = Pattern.compile("\\{.*\\}");
        for(String path : paths){
            logger.info("MvcConfig 遍历path-->{}",path);
            String tranPath=path;
            Matcher match = pattern.matcher(tranPath);
            if(match.find()){
                tranPath = match.replaceFirst("*");
                tranPath = tranPath.substring(0, tranPath.indexOf("*") + 1);
            }
            if(SPEC_PATH.equals(tranPath)){
                logger.warn("MvcConfig not regist path[{}] to mvc,because it's indeed [/*] ,jersey will be crash if exist mapping [/*]",path);
            }else{
                registration.addUrlMappings(tranPath);
                if(tranPath.endsWith("*")){
                    String otherPath =tranPath.substring(0, tranPath.length() - 1);
                    registration.addUrlMappings(otherPath);
                }
            }
        }
        return registration;
    }
}
