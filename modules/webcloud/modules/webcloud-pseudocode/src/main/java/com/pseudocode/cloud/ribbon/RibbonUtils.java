package com.pseudocode.cloud.ribbon;

import com.netflix.config.ConfigurationManager;
import com.pseudocode.cloud.commons.client.ServiceInstance;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RibbonUtils {

    public static final String VALUE_NOT_SET = "__not__set__";
    public static final String DEFAULT_NAMESPACE = "ribbon";

    private static final Map<String, String> unsecureSchemeMapping;
    static
    {
        unsecureSchemeMapping = new HashMap<>();
        unsecureSchemeMapping.put("http", "https");
        unsecureSchemeMapping.put("ws", "wss");
    }

    public static void initializeRibbonDefaults(String serviceId) {
        //设置vipAddresses为serviceId
        setRibbonProperty(serviceId, CommonClientConfigKey.DeploymentContextBasedVipAddresses.key(), serviceId);
        //设置区域感知 true
        setRibbonProperty(serviceId, CommonClientConfigKey.EnableZoneAffinity.key(), "true");
    }

    public static void setRibbonProperty(String serviceId, String suffix, String value) {
        String key = getRibbonKey(serviceId, suffix);
        DynamicStringProperty property = getProperty(key);
        if (property.get().equals(VALUE_NOT_SET)) {
           ConfigurationManager.getConfigInstance().setProperty(key, value);
        }
    }

    public static String getRibbonKey(String serviceId, String suffix) {
        return serviceId + "." + DEFAULT_NAMESPACE + "." + suffix;
    }

    public static DynamicStringProperty getProperty(String key) {
        return DynamicPropertyFactory.getInstance().getStringProperty(key, VALUE_NOT_SET);
    }

    public static boolean isSecure(IClientConfig config, ServerIntrospector serverIntrospector, Server server) {
        if (config != null) {
            Boolean isSecure = config.get(CommonClientConfigKey.IsSecure);
            if (isSecure != null) {
                return isSecure;
            }
        }

        return serverIntrospector.isSecure(server);
    }

    public static URI updateToHttpsIfNeeded(URI uri, IClientConfig config, ServerIntrospector serverIntrospector,
                                            Server server) {
        return updateToSecureConnectionIfNeeded(uri, config, serverIntrospector, server);
    }

    static URI updateToSecureConnectionIfNeeded(URI uri, ServiceInstance ribbonServer) {
        String scheme = uri.getScheme();

        if (StringUtils.isEmpty(scheme)) {
            scheme = "http";
        }

        if (!StringUtils.isEmpty(uri.toString())
                && unsecureSchemeMapping.containsKey(scheme)
                && ribbonServer.isSecure()) {
            return upgradeConnection(uri, unsecureSchemeMapping.get(scheme));
        }
        return uri;
    }

    public static URI updateToSecureConnectionIfNeeded(URI uri, IClientConfig config,
                                                       ServerIntrospector serverIntrospector, Server server) {
        String scheme = uri.getScheme();

        if (StringUtils.isEmpty(scheme)) {
            scheme = "http";
        }

        if (!StringUtils.isEmpty(uri.toString())
                && unsecureSchemeMapping.containsKey(scheme)
                && isSecure(config, serverIntrospector, server)) {
            return upgradeConnection(uri, unsecureSchemeMapping.get(scheme));
        }
        return uri;
    }

    private static URI upgradeConnection(URI uri, String scheme) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).scheme(scheme);
        if (uri.getRawQuery() != null) {
            uriComponentsBuilder.replaceQuery(uri.getRawQuery().replace("+", "%20"));
        }
        return uriComponentsBuilder.build(true).toUri();
    }
}
