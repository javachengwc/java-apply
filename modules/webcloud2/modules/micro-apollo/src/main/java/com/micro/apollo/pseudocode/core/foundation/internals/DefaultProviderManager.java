package com.micro.apollo.pseudocode.core.foundation.internals;

import com.micro.apollo.pseudocode.core.foundation.internals.provider.DefaultApplicationProvider;
import com.micro.apollo.pseudocode.core.foundation.internals.provider.DefaultNetworkProvider;
import com.micro.apollo.pseudocode.core.foundation.internals.provider.DefaultServerProvider;
import com.micro.apollo.pseudocode.core.foundation.spi.ProviderManager;
import com.micro.apollo.pseudocode.core.foundation.spi.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultProviderManager implements ProviderManager {

    private static final Logger logger = LoggerFactory.getLogger(com.ctrip.framework.foundation.internals.DefaultProviderManager.class);

    private Map<Class<? extends Provider>, Provider> m_providers = new LinkedHashMap<>();

    public DefaultProviderManager() {
        //应用
        Provider applicationProvider = new DefaultApplicationProvider();
        applicationProvider.initialize();
        register(applicationProvider);

        //网络
        Provider networkProvider = new DefaultNetworkProvider();
        networkProvider.initialize();
        register(networkProvider);

        //服务
        Provider serverProvider = new DefaultServerProvider();
        serverProvider.initialize();
        register(serverProvider);
    }

    public synchronized void register(Provider provider) {
        m_providers.put(provider.getType(), provider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Provider> T provider(Class<T> clazz) {
        Provider provider = m_providers.get(clazz);

        if (provider != null) {
            return (T) provider;
        } else {
            logger.error("No provider [{}] found in DefaultProviderManager, please make sure it is registered in DefaultProviderManager ", clazz.getName());
            return (T) NullProviderManager.provider;
        }
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        for (Provider provider : m_providers.values()) {
            String value = provider.getProperty(name, null);

            if (value != null) {
                return value;
            }
        }
        return defaultValue;
    }
}
