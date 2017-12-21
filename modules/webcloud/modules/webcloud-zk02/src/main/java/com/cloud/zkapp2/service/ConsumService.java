package com.cloud.zkapp2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumService {

    private static Logger logger= LoggerFactory.getLogger(ConsumService.class);

    @Autowired
    private RestTemplate restTemplate;

    public String invokeAdd(String url) {
        logger.info("ConsumService invokeAdd invoke,url="+url);
        return restTemplate.getForEntity(url, String.class).getBody();
    }
}
