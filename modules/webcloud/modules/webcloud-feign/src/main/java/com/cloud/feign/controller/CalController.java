package com.cloud.feign.controller;

import com.cloud.feign.service.CalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cal")
public class CalController {

    private static Logger logger = LoggerFactory.getLogger(CalController.class);

    @Autowired
    private CalService calculateService;

    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
        logger.info("["+Thread.currentThread().getName()+"]CalController add start,a="+a+",b="+b);
        return calculateService.add(a,b);
    }

}
