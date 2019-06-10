package com.pseudocode.netflix.ribbon.core.client;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;

public interface VipAddressResolver {

    public String resolve(String vipAddress, IClientConfig niwsClientConfig);
}
