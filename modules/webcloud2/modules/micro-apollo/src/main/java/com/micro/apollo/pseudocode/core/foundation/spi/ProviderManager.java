package com.micro.apollo.pseudocode.core.foundation.spi;

import com.micro.apollo.pseudocode.core.foundation.spi.provider.Provider;

public interface ProviderManager {

    public String getProperty(String name, String defaultValue);

    public <T extends Provider> T provider(Class<T> clazz);
}
