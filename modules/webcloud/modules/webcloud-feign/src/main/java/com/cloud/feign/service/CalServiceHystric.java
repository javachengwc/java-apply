package com.cloud.feign.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalServiceHystric implements CalService {

    private static Logger logger= LoggerFactory.getLogger(CalServiceHystric.class);

    public Integer add(Integer a, Integer b)
    {
        logger.info("["+Thread.currentThread().getName()+"]CalServiceHystric add invoke,a="+a+",b="+b);
        return 0;
    }
}
