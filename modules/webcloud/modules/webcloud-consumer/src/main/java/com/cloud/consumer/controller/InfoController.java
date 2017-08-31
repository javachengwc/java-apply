package com.cloud.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InfoController {

    private static Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public String info()
    {
        logger.info("InfoController info invoked.................");
        return "webcloud-consumer";
    }

    @RequestMapping(value = "invokeInfo", method = RequestMethod.GET)
    public String invokeInfo()
    {
        logger.info("InfoController invokeInfo invoked.................");
        return restTemplate.getForEntity("http://localhost:2222/info", String.class).getBody();
    }
}