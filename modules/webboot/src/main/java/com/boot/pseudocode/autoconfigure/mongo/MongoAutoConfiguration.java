package com.boot.pseudocode.autoconfigure.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;
import java.net.UnknownHostException;

@Configuration
@ConditionalOnClass({MongoClient.class})
@EnableConfigurationProperties({MongoProperties.class})
@ConditionalOnMissingBean(type={"org.springframework.data.mongodb.MongoDbFactory"})
public class MongoAutoConfiguration
{
    private final MongoProperties properties;
    private final MongoClientOptions options;
    private final Environment environment;
    private MongoClient mongo;

    public MongoAutoConfiguration(MongoProperties properties, ObjectProvider<MongoClientOptions> options, Environment environment)
    {
        this.properties = properties;
        this.options = ((MongoClientOptions)options.getIfAvailable());
        this.environment = environment;
    }
    @PreDestroy
    public void close() {
        if (this.mongo != null)
            this.mongo.close();
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoClient mongo() throws UnknownHostException {
        this.mongo = this.properties.createMongoClient(this.options, this.environment);
        return this.mongo;
    }
}
