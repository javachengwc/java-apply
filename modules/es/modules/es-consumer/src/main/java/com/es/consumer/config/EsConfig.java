package com.es.consumer.config;

import lombok.Data;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value="es.enabled", matchIfMissing = false)
@Data
public class EsConfig {

    private static Logger logger= LoggerFactory.getLogger(EsConfig.class);

    @Value("${es.cluster.name}")
    private String esClusterName;

    @Value("${es.ping.timeout}")

    private String esPingTimeout;

    @Value("${es.nodes.sampler.interval}")
    private String esNodesSamplerInterval;

    @Value("${es.host}")
    private String esHost;

    @Value("${es.port}")
    private int esPort;

    @Value("${es.host2}")
    private String esHost2;

    @Value("${es.port2}")
    private int esPort2;

    @Value("${es.host3}")
    private String esHost3;

    @Value("${es.port3}")
    private int esPort3;

    @Bean(name="esClient")
    public Client esClient(){
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("client.transport.sniff",true)         //开启嗅探
                .put("cluster.name", esClusterName)
                .put("client.transport.ping_timeout", esPingTimeout)
                .put("client.transport.nodes_sampler_interval", esNodesSamplerInterval)
                .build();

        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(esHost, esPort))
                .addTransportAddress(new InetSocketTransportAddress(esHost2, esPort2))
                .addTransportAddress(new InetSocketTransportAddress(esHost3, esPort3));

        logger.info("EsConfig esClient created success.........");
        return client;
    }
}
