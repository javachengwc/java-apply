package com.pseudocode.netflix.ribbon.core.client;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;

public interface IClientConfigAware {

    public abstract void initWithNiwsConfig(IClientConfig clientConfig);

}
