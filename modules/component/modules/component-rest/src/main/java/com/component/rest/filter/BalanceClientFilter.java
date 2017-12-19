package com.component.rest.filter;

import com.component.rest.balance.ClientLoadBalancer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile("microservice")
public class BalanceClientFilter implements ClientRequestFilter, ApplicationContextAware {

    private static Logger logger  = LoggerFactory.getLogger(BalanceClientFilter.class);

    public static String  MS_PREFIX  = "MS_";
    private static String HEADER_OVERRIDESERVERNAME_PREFIX    = "MS_SERVERNAME_";
    private static String HEADER_OVERRIDESERVERVERSION_PREFIX = "MS_SERVERVERSION_";
    private static String OVERRIDE_SERVERNAME_PREFIX = "component.overrideServerName.";
    private static String OVERRIDE_SERVERVERSION_PREFIX = "component.overrideServerVersion.";
    public static String  REST_CONSISTENT_KEY="REST_CONSISTENT";
    public static String  APP_NAME_KEY="APP_NAME";
    private static String  METADATA_KEY_APIGATEWAY_GROUP = "eureka.instance.metadataMap.groupName";

    public static ThreadLocal<Map<String, String>> parentRequestContextHolder = new ThreadLocal<Map<String, String>>();
    public static ThreadLocal<Map<String, String>> childRequestContextHolder  = new ThreadLocal<Map<String, String>>();
    public static ThreadLocal<String> restConsistent = new ThreadLocal<String>();

    private static Environment environment = null;

    @Autowired
    private ClientLoadBalancer clientLoadBalancer;

    @Value("${spring.application.name:noname-service}")
    private String appName;

    public void setApplicationContext(ApplicationContext applicationContext) {
        environment = applicationContext.getEnvironment();
    }

    public void filter(ClientRequestContext requestContext) throws IOException {
        transferRequestContext(requestContext);
        restConsistent.set(requestContext.getHeaderString(REST_CONSISTENT_KEY));
        requestContext.getHeaders().add(APP_NAME_KEY, appName);
        if (clientLoadBalancer != null) {
            clientLoadBalancer.execute(requestContext);
        }
        childRequestContextHolder.remove();
        restConsistent.remove();
    }

    public static String getApiGatewayGroup() {
        String result = "";
        if (environment != null) {
            result =  environment.getProperty(METADATA_KEY_APIGATEWAY_GROUP);
        }
        if (StringUtils.isBlank(result)) {
            result = "";
        }
        return result;
    }

    private static void appendChildRequest(String key, String value) {
        if (childRequestContextHolder.get() == null) {
            HashMap<String,String> map = new HashMap<String,String>();
            childRequestContextHolder.set(map);
        }
        childRequestContextHolder.get().put(key, value);
    }

    public static void appendParentRequest(String key, String value) {
        if (parentRequestContextHolder.get() == null) {
            HashMap<String,String> map = new HashMap<String,String>();
            parentRequestContextHolder.set(map);
        }
        parentRequestContextHolder.get().put(key, value);
    }

    public static String getVersion(String serviceName) {
        String version = "";
        if (StringUtils.isBlank(serviceName)) {
            return version;
        }

        Map<String, String> childRequestContext = childRequestContextHolder.get();
        if (childRequestContext != null ) {
            for (String keyHeader : childRequestContext.keySet()) {
                if (keyHeader.equalsIgnoreCase(HEADER_OVERRIDESERVERVERSION_PREFIX + serviceName)) {
                    return childRequestContext.get(keyHeader);
                }
            }
        }

        Map<String, String> parentRequestContext = parentRequestContextHolder.get();
        if (parentRequestContext != null) {
            for (String keyHeader : parentRequestContext.keySet()) {
                if (keyHeader.equalsIgnoreCase(MS_PREFIX + serviceName)) {
                    return parentRequestContext.get(keyHeader);
                }
            }
        }

        return version;
    }

    public static String getServiceName(String serviceName) {
        if (StringUtils.isBlank(serviceName) ) {
            return serviceName;
        }
        Map<String, String> childRequestContext = childRequestContextHolder.get();
        if (childRequestContext == null) {
            return serviceName;
        }
        for (String keyHeader : childRequestContext.keySet()) {
            if (keyHeader.equalsIgnoreCase(HEADER_OVERRIDESERVERNAME_PREFIX + serviceName)) {
                String headerValue = childRequestContext.get(keyHeader);
                if (StringUtils.isBlank(headerValue)) {
                    return serviceName;
                } else {
                    return headerValue;
                }
            }
        }
        return serviceName;
    }

    //把父请求中MS_开头的header复制给子请求
    private void transferRequestContext(ClientRequestContext requestContext) {
        Map<String, String> contextHolder = parentRequestContextHolder.get();
        if (null == contextHolder || contextHolder.size()<=0) {
            return;
        }
        for (String headerKey : contextHolder.keySet()) {
            if (!headerKey.startsWith(MS_PREFIX)) {
                continue;
            }
            String headerValue = contextHolder.get(headerKey);
            requestContext.getHeaders().add(headerKey, headerValue);
            if(logger.isDebugEnabled()) {
                logger.debug("BalancedClientFilter transferRequestContext request header," +
                        "headerKey={},headerValue={}", headerKey, headerValue);
            }
        }
    }

    public static void appendHeaderValues(String interfaceName, String hostName) {
        if (environment == null || StringUtils.isBlank(interfaceName) || StringUtils.isBlank(hostName)) {
            return;
        }

        String serverNameKey =  OVERRIDE_SERVERNAME_PREFIX + interfaceName + "." + hostName;
        String versionNameKey = OVERRIDE_SERVERVERSION_PREFIX + interfaceName + "." + hostName;
        String overrideServerName = environment.getProperty(serverNameKey);
        String overrideServerVersion = environment.getProperty(versionNameKey);

        if (!StringUtils.isBlank(overrideServerName)) {
            appendChildRequest(HEADER_OVERRIDESERVERNAME_PREFIX + hostName, overrideServerName);
        }

        if (!StringUtils.isBlank(overrideServerVersion)) {
            appendChildRequest(HEADER_OVERRIDESERVERVERSION_PREFIX + hostName, overrideServerVersion);
        }
    }

    public static void copyTo(Map<String, String> holder) {
        if (holder == null || parentRequestContextHolder.get() == null ) {
            return;
        }
        holder.putAll(parentRequestContextHolder.get());
    }

    public static void setRestConsistent(String restConsistentStr) {
        restConsistent.set(restConsistentStr);
    }

    public static void removeRestConsistent() {
        restConsistent.remove();
    }
}