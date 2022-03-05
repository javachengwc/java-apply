package com.es.consumer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationConfig {

    @Value("${zk.address}")
    private String zkAddress;

    @Value("${zk.session.timeout}")
    private String zkSessionTimeout;

    @Value("${zk.connect.timeout}")
    private String zkConnectTimeout;

    @Value("${zk.sync.time}")
    private String zkSyncTime;

    @Value("${rebalance.backoff}")
    private String rebalanceBackoff;

    @Value("${rebalance.max.retries}")
    private String rebalanceMaxRetries;

}
