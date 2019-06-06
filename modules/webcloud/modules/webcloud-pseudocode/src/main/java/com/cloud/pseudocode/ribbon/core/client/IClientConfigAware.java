package com.cloud.pseudocode.ribbon.core.client;

import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;

public interface IClientConfigAware {

    public abstract void initWithNiwsConfig(IClientConfig clientConfig);

}
