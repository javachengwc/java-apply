package com.cloud.consumer.controller;

import com.cloud.consumer.model.Record;
import com.cloud.consumer.service.ConsumerService;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    private static Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value = "/doAdd", method = RequestMethod.GET)
    public String doAdd(@RequestParam Integer a, @RequestParam Integer b) {
        logger.info("ConsumerController doAdd invoke.....");
        String paramStr="a="+a+"&b="+b;
        String url ="http://"+"webcloud-appa".toUpperCase()+"/web/add";
        String reqUrl =url+"?"+paramStr;
        logger.info("ConsumerController doAdd  reqUrl="+reqUrl);
        //ThreadUtil.sleep(2000l);
        return consumerService.invokeAdd(reqUrl);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String doTest(@RequestParam String content) {
        logger.info("ConsumerController doTest invoke.....");
        return consumerService.test(content);
    }

    @RequestMapping(value = "/getRecord", method = RequestMethod.GET)
    public Record getRecord(@RequestParam Integer id) {
        logger.info("ConsumerController getRecord invoke.....");
        Future<Record> recordFuture= consumerService.getRecordById(id);
        logger.info("ConsumerController getRecord  recordFuture="+recordFuture);
        Record  record=null;
        try {
            if (recordFuture != null) {
                record = recordFuture.get();
            }
        }catch(Exception e) {
            logger.info("ConsumerController error,",e);
        }
        return record;
    }
}
