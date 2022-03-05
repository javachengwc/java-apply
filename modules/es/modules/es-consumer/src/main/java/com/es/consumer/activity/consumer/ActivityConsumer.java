package com.es.consumer.activity.consumer;

import com.es.consumer.activity.service.ActivityService;
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
@ConditionalOnProperty(name = "message.activity.group", havingValue = "true")
public class ActivityConsumer {

    private static Logger logger = LoggerFactory.getLogger(ActivityConsumer.class);

    @Autowired
    private ActivityService activityService;

    @Value("${message.activity.topic}")
    private String activityTopic;

    @Value("${message.activity.group}")
    private String activityGroup;

    @Bean
    public MessageConsumer consumerActivity(ApplicationConfig applicationConfig) {
        logger.info("ActivityConsumer consumerActivity create messageConsumer......");
        MessageConsumer messageConsumer = new MessageConsumer(applicationConfig, activityService, activityTopic, activityGroup);
        messageConsumer.consume(1);
        return messageConsumer;
    }
}
