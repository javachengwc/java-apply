package com.boot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class I18nConfig {

    private static Logger logger= LoggerFactory.getLogger(I18nConfig.class);

    @Bean("messageSource")
    public MessageSource createMessageSource() {
        logger.info("createMessageSource start..................");
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }
}
