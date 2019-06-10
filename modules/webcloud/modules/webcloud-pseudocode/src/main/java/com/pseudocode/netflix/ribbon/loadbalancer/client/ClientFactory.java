package com.pseudocode.netflix.ribbon.loadbalancer.client;

import com.pseudocode.netflix.ribbon.core.client.ClientException;
import com.pseudocode.netflix.ribbon.core.client.IClient;
import com.pseudocode.netflix.ribbon.core.client.IClientConfigAware;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.DefaultClientConfigImpl;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.netflix.servo.monitor.Monitors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {

    private static Map<String, IClient<?,?>> simpleClientMap = new ConcurrentHashMap<String, IClient<?,?>>();
    private static Map<String, ILoadBalancer> namedLBMap = new ConcurrentHashMap<String, ILoadBalancer>();
    private static ConcurrentHashMap<String, IClientConfig> namedConfig = new ConcurrentHashMap<String, IClientConfig>();

    private static Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    public static synchronized IClient<?, ?> registerClientFromProperties(String restClientName, IClientConfig clientConfig) throws ClientException {
        IClient<?, ?> client = null;
        ILoadBalancer loadBalancer = null;
        if (simpleClientMap.get(restClientName) != null) {
            throw new ClientException(
                    ClientException.ErrorType.GENERAL,
                    "A Rest Client with this name is already registered. Please use a different name");
        }
        try {
            String clientClassName = (String) clientConfig.getProperty(CommonClientConfigKey.ClientClassName);
            client = (IClient<?, ?>) instantiateInstanceWithClientConfig(clientClassName, clientConfig);
            boolean initializeNFLoadBalancer = Boolean.parseBoolean(clientConfig.getProperty(
                    CommonClientConfigKey.InitializeNFLoadBalancer, DefaultClientConfigImpl.DEFAULT_ENABLE_LOADBALANCER).toString());
            if (initializeNFLoadBalancer) {
                loadBalancer  = registerNamedLoadBalancerFromclientConfig(restClientName, clientConfig);
            }
            if (client instanceof AbstractLoadBalancerAwareClient) {
                ((AbstractLoadBalancerAwareClient) client).setLoadBalancer(loadBalancer);
            }
        } catch (Throwable e) {
            String message = "Unable to InitializeAndAssociateNFLoadBalancer set for RestClient:"
                    + restClientName;
            logger.warn(message, e);
            throw new ClientException(ClientException.ErrorType.CONFIGURATION,
                    message, e);
        }
        simpleClientMap.put(restClientName, client);

        Monitors.registerObject("Client_" + restClientName, client);

        logger.info("Client Registered:" + client.toString());
        return client;
    }

    public static synchronized IClient getNamedClient(String name) {
        return getNamedClient(name, DefaultClientConfigImpl.class);
    }

    public static synchronized IClient getNamedClient(String name, Class<? extends IClientConfig> configClass) {
        if (simpleClientMap.get(name) != null) {
            return simpleClientMap.get(name);
        }
        try {
            return createNamedClient(name, configClass);
        } catch (ClientException e) {
            throw new RuntimeException("Unable to create client", e);
        }
    }

    public static synchronized IClient createNamedClient(String name, Class<? extends IClientConfig> configClass) throws ClientException {
        IClientConfig config = getNamedConfig(name, configClass);
        return registerClientFromProperties(name, config);
    }

    public static synchronized ILoadBalancer getNamedLoadBalancer(String name) {
        return getNamedLoadBalancer(name, DefaultClientConfigImpl.class);
    }

    public static synchronized ILoadBalancer getNamedLoadBalancer(String name, Class<? extends IClientConfig> configClass) {
        ILoadBalancer lb = namedLBMap.get(name);
        if (lb != null) {
            return lb;
        } else {
            try {
                lb = registerNamedLoadBalancerFromProperties(name, configClass);
            } catch (ClientException e) {
                throw new RuntimeException("Unable to create load balancer", e);
            }
            return lb;
        }
    }

    public static ILoadBalancer registerNamedLoadBalancerFromclientConfig(String name, IClientConfig clientConfig) throws ClientException {
        if (namedLBMap.get(name) != null) {
            throw new ClientException("LoadBalancer for name " + name + " already exists");
        }
        ILoadBalancer lb = null;
        try {
            String loadBalancerClassName = (String) clientConfig.getProperty(CommonClientConfigKey.NFLoadBalancerClassName);
            lb = (ILoadBalancer) ClientFactory.instantiateInstanceWithClientConfig(loadBalancerClassName, clientConfig);
            namedLBMap.put(name, lb);
            logger.info("Client: {} instantiated a LoadBalancer: {}", name, lb);
            return lb;
        } catch (Throwable e) {
            throw new ClientException("Unable to instantiate/associate LoadBalancer with Client:" + name, e);
        }
    }

    public static synchronized ILoadBalancer registerNamedLoadBalancerFromProperties(String name, Class<? extends IClientConfig> configClass) throws ClientException {
        if (namedLBMap.get(name) != null) {
            throw new ClientException("LoadBalancer for name " + name + " already exists");
        }
        IClientConfig clientConfig = getNamedConfig(name, configClass);
        return registerNamedLoadBalancerFromclientConfig(name, clientConfig);
    }

    @SuppressWarnings("unchecked")
    public static Object instantiateInstanceWithClientConfig(String className, IClientConfig clientConfig)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class clazz = Class.forName(className);
        if (IClientConfigAware.class.isAssignableFrom(clazz)) {
            IClientConfigAware obj = (IClientConfigAware) clazz.newInstance();
            obj.initWithNiwsConfig(clientConfig);
            return obj;
        } else {
            try {
                if (clazz.getConstructor(IClientConfig.class) != null) {
                    return clazz.getConstructor(IClientConfig.class).newInstance(clientConfig);
                }
            } catch (NoSuchMethodException ignored) {
                // OK for a class to not take an IClientConfig
            } catch (SecurityException | IllegalArgumentException | InvocationTargetException e) {
                logger.warn("Error getting/invoking IClientConfig constructor of {}", className, e);
            }
        }
        logger.warn("Class " + className + " neither implements IClientConfigAware nor provides a constructor with IClientConfig as the parameter. Only default constructor will be used.");
        return clazz.newInstance();
    }

    public static IClientConfig getNamedConfig(String name) {
        return 	getNamedConfig(name, DefaultClientConfigImpl.class);
    }

    public static IClientConfig getNamedConfig(String name, Class<? extends IClientConfig> clientConfigClass) {
        IClientConfig config = namedConfig.get(name);
        if (config != null) {
            return config;
        } else {
            try {
                config = (IClientConfig) clientConfigClass.newInstance();
                config.loadProperties(name);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Unable to create named client config '{}' instance for config class {}", name,
                        clientConfigClass, e);
                return null;
            }
            config.loadProperties(name);
            IClientConfig old = namedConfig.putIfAbsent(name, config);
            if (old != null) {
                config = old;
            }
            return config;
        }
    }
}
