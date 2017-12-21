package com.cloud.zkapp2.config;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfiguration {

    private static Logger logger= LoggerFactory.getLogger(RibbonConfiguration.class);

    //@Bean
    //@ConditionalOnMissingBean
    public IClientConfig ribbonConfig() {
        IClientConfig clientConfig=  IClientConfig.Builder.newBuilder().build();
        clientConfig.set(CommonClientConfigKey.ServerListRefreshInterval,1000);
        return  clientConfig;
    }

    //@Bean
    //@ConditionalOnMissingBean
    public ServerListUpdater serverListUpdater(IClientConfig clientConfig) {
        long refreshIntercalMs =getRefreshIntervalMs(clientConfig);
        logger.info("----------------------------------RibbonConfiguration serverListUpdater refreshIntercalMs={}",refreshIntercalMs);
        return new PollingServerListUpdater(clientConfig);
    }

    public long getRefreshIntervalMs(IClientConfig clientConfig) {
        return clientConfig.get(CommonClientConfigKey.ServerListRefreshInterval, 30000);
    }
}
