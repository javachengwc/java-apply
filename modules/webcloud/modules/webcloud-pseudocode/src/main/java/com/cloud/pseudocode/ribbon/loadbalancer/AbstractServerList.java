package com.cloud.pseudocode.ribbon.loadbalancer;

import com.cloud.pseudocode.ribbon.core.client.ClientException;
import com.cloud.pseudocode.ribbon.core.client.IClientConfigAware;
import com.cloud.pseudocode.ribbon.core.client.config.CommonClientConfigKey;
import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.client.ClientFactory;

public abstract class AbstractServerList<T extends Server> implements ServerList<T>, IClientConfigAware {

    public AbstractServerListFilter<T> getFilterImpl(IClientConfig niwsClientConfig) throws ClientException {
        try {
            String niwsServerListFilterClassName = niwsClientConfig
                    .getProperty(
                            CommonClientConfigKey.NIWSServerListFilterClassName,
                            ZoneAffinityServerListFilter.class.getName())
                    .toString();

            AbstractServerListFilter<T> abstractNIWSServerListFilter =
                    (AbstractServerListFilter<T>) ClientFactory.instantiateInstanceWithClientConfig(niwsServerListFilterClassName, niwsClientConfig);
            return abstractNIWSServerListFilter;
        } catch (Throwable e) {
            throw new ClientException(
                    ClientException.ErrorType.CONFIGURATION,
                    "Unable to get an instance of CommonClientConfigKey.NIWSServerListFilterClassName. Configured class:"
                            + niwsClientConfig
                            .getProperty(CommonClientConfigKey.NIWSServerListFilterClassName), e);
        }
    }
}

