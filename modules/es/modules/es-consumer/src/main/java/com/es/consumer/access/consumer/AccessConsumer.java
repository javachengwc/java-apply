package com.es.consumer.access.consumer;

import com.es.consumer.access.service.AccessService;
import com.es.consumer.base.consumer.MessageConsumer;
import com.es.consumer.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "message.access.group", havingValue = "true")
public class AccessConsumer {

    private static Logger logger = LoggerFactory.getLogger(AccessConsumer.class);

    @Autowired
    private AccessService accessService;

    @Value("${message.access.topic}")
    private String accessTopic;

    @Value("${message.access.group}")
    private String accessGroup;

    @Bean
    public MessageConsumer consumerAccess(ApplicationConfig applicationConfig) {
        logger.info("AccessConsumer consumerAccess create messageConsumer......");
        MessageConsumer messageConsumer = new MessageConsumer(applicationConfig, accessService, accessTopic, accessGroup);
        messageConsumer.consume(1);
        return messageConsumer;
    }
}
