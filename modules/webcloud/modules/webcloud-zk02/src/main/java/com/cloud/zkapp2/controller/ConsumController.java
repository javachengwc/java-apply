package com.cloud.zkapp2.controller;

import com.cloud.zkapp2.service.ConsumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consum")
public class ConsumController {

    private static Logger logger = LoggerFactory.getLogger(ConsumController.class);

    @Autowired
    private ConsumService consumService;

    @RequestMapping(value = "/doAdd", method = RequestMethod.GET)
    @ResponseBody
    public String doAdd(@RequestParam Integer a, @RequestParam Integer b) {
        logger.info("ConsumController doAdd invoke.....");
        String paramStr="a="+a+"&b="+b;
        String url ="http://"+"zk01Application"+"/web/add";
        String reqUrl =url+"?"+paramStr;
        logger.info("ConsumController doAdd  reqUrl="+reqUrl);
        return consumService.invokeAdd(reqUrl);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        logger.info("ConsumController test invoke.....");
        return "test";
    }
}
