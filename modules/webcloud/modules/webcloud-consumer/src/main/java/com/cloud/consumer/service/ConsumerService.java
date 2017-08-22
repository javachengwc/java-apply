package com.cloud.consumer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerService {

    private static Logger logger= LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "failCallback")
    public String invokeAdd(String url) {
        logger.info("ConsumerService invokeAdd invoke,url="+url);
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    //hystrix的fallback方法必须与原方法返回值类型相同,且参数与原方法参相同或比原参只多加一个异常参
    public String failCallback(String url) {
        logger.info("ConsumerService failCallback invoke,url="+url);
        return "remote invoke error";
    }
}
