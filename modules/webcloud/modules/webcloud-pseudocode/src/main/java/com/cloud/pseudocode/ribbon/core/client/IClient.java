package com.cloud.pseudocode.ribbon.core.client;

import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;

public interface IClient<S extends ClientRequest, T extends IResponse> {

    T execute(S var1, IClientConfig var2) throws Exception;
}
