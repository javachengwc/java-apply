package com.boot.pseudocode.autoconfigure.data.elasticsearch;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lease.Releasable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.NodeClientFactoryBean;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@ConditionalOnClass({Client.class, TransportClientFactoryBean.class, NodeClientFactoryBean.class})
@EnableConfigurationProperties({ElasticsearchProperties.class})
public class ElasticsearchAutoConfiguration implements DisposableBean
{
    private static final Map<String, String> DEFAULTS;
    private static final Log logger;
    private final ElasticsearchProperties properties;
    private Releasable releasable;

    public ElasticsearchAutoConfiguration(ElasticsearchProperties properties)
    {
        this.properties = properties;
    }
    @Bean
    @ConditionalOnMissingBean
    public Client elasticsearchClient() {
        try {
            return createClient();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private Client createClient() throws Exception
    {
        if (StringUtils.hasLength(this.properties.getClusterNodes())) {
            return createTransportClient();
        }
        return createNodeClient();
    }

    private Client createNodeClient() throws Exception {
        Settings.Builder settings = Settings.settingsBuilder();
        for (Map.Entry entry : DEFAULTS.entrySet()) {
            if (!this.properties.getProperties().containsKey(entry.getKey())) {
                settings.put((String)entry.getKey(), (String)entry.getValue());
            }
        }
        settings.put(this.properties.getProperties());

        Node node = new NodeBuilder().settings(settings).clusterName(this.properties.getClusterName()).node();
        this.releasable = node;
        return node.client();
    }

    private Client createTransportClient() throws Exception {
        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        factory.setClusterNodes(this.properties.getClusterNodes());
        factory.setProperties(createProperties());
        factory.afterPropertiesSet();
        TransportClient client = factory.getObject();
        this.releasable = client;
        return client;
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        properties.put("cluster.name", this.properties.getClusterName());
        properties.putAll(this.properties.getProperties());
        return properties;
    }

    public void destroy() throws Exception
    {
        if (this.releasable != null)
            try {
                if (logger.isInfoEnabled())
                    logger.info("Closing Elasticsearch client");
                try
                {
                    this.releasable.close();
                }
                catch (NoSuchMethodError ex)
                {
                    ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(Releasable.class, "release"),this.releasable);
                }
            }
            catch (Exception ex)
            {
                if (logger.isErrorEnabled())
                    logger.error("Error closing Elasticsearch client: ", ex);
            }
    }

    static
    {
        Map defaults = new LinkedHashMap();
        defaults.put("http.enabled", String.valueOf(false));
        defaults.put("node.local", String.valueOf(true));
        defaults.put("path.home", System.getProperty("user.dir"));
        DEFAULTS = Collections.unmodifiableMap(defaults);
        logger = LogFactory.getLog(ElasticsearchAutoConfiguration.class);
    }
}
