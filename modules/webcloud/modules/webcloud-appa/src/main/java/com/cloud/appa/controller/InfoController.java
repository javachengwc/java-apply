package com.cloud.appa.controller;

import com.util.base.ThreadUtil;
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
        ThreadUtil.sleep(2000l);
        String url ="http://"+"webcloud-consumer".toUpperCase()+"/info";
        return restTemplate.getForEntity(url, String.class).getBody();
    }

}
