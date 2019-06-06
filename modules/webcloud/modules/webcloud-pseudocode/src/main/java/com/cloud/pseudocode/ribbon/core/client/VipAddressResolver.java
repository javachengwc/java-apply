package com.cloud.pseudocode.ribbon.core.client;

import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;

public interface VipAddressResolver {

    public String resolve(String vipAddress, IClientConfig niwsClientConfig);
}
