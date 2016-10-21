package com.mountain.registry;

import com.mountain.model.SpecUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册工厂
 */
public class RegistryFactory {

    private static Logger logger = LoggerFactory.getLogger(RegistryFactory.class);

    //注册中心集合
    private static  ConcurrentHashMap<String, Registry> registryMap = new ConcurrentHashMap<String, Registry>();

    public Registry getRegistry(SpecUrl url) {
        String key = url.toServiceStr();
        synchronized (key.intern()) {
            try {
                Registry registry = registryMap.get(key);
                if (registry != null) {
                    return registry;
                }
                registry = new ZookeeperRegistry(url);
                registryMap.put(key, registry);
                return registry;
            } catch (Exception e) {
                logger.error("RegistryFactory getRegistry error,url key="+key,e);
                throw new IllegalStateException("RegistryFactory can not create registry,url=" + url);
            }
        }
    }
}
