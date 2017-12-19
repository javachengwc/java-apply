package com.cloud.feign.controller;

import com.cloud.feign.service.CalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cal")
public class CalController {

    private static Logger logger = LoggerFactory.getLogger(CalController.class);

    @Autowired
    private CalService calService;

    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    @ResponseBody
    public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
        logger.info("["+Thread.currentThread().getName()+"]CalController add start,a="+a+",b="+b);
        return calService.add(a,b);
    }
}
