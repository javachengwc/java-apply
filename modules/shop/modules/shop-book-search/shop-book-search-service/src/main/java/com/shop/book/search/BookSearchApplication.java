package com.shop.book.search;

import com.shop.book.search.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude=SolrAutoConfiguration.class)
@EnableDiscoveryClient
public class BookSearchApplication {

    private static Logger logger = LoggerFactory.getLogger(BookSearchApplication.class);

    public static void main(String[] args) {
        logger.info("BookSearchApplication start  begin........");
        SpringApplicationBuilder builder =new SpringApplicationBuilder(new Object[] { BookSearchApplication.class, Config.class });
        builder.run(args);
        logger.info("BookSearchApplication start success........");
    }

}
