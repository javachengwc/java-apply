package com.cloud.consumer.service;

import com.cloud.consumer.model.Record;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Service
public class ConsumerService {

    private static Logger logger= LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "failCallback")
    public String invokeAdd(String url) {
        logger.info("ConsumerService invokeAdd invoke,url="+url);
        ThreadUtil.sleep(2000l);
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    //hystrix的fallback方法返回值类型必须与原方法返回值类型相同,Future的返回类型除外,
    //且参数与原方法参相同或比原参只多加一个异常参
    public String failCallback(String url) {
        logger.info("ConsumerService failCallback invoke,url="+url);
        return "remote invoke error";
    }

    //hystrix自定义超时,超时后会调用fallback返回结果
    @HystrixCommand(fallbackMethod = "timeoutFallback",
        commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")}
    )
    public String test(String content) {
        logger.info("ConsumerService test invoke,content="+content);
        ThreadUtil.sleep(2000l);
        return "test";
    }

    public String timeoutFallback(String content) {
        logger.info("ConsumerService timeoutFallback invoke,content="+content);
        return "timeout fallback";
    }

    @HystrixCommand( fallbackMethod ="futureFallback")
    public Future<Record> getRecordById(Integer id) {
        logger.info("ConsumerService getRecordById invoke,id="+id);
        throw new RuntimeException("getRecordById error");
    }

    public Record futureFallback(Integer id, Throwable e ) {
        logger.info("ConsumerService futureFallback invoke,id="+id);
        if(e!=null) {
            logger.info("ConsumerService futureFallback e="+e.getMessage());
        }
        return new Record(id,"name","note");
    }

    public Record directRecord(Integer id) {
        logger.info("ConsumerService directRecord invoke,id="+id);
        throw new RuntimeException("getRecordById error");
    }
}
