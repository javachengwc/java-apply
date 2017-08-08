package com.datastore.mongodb.controller;

import com.datastore.mongodb.model.Info;
import com.datastore.mongodb.service.InfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
public class InfoController {

    private static Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private InfoService infoService;

    @ResponseBody
    @RequestMapping("saveInfo")
    public Boolean  saveInfo(Info info)
    {
        logger.info("InfoContrller saveInfo start,info={}",info);
        return infoService.saveInfo(info);
    }

    @ResponseBody
    @RequestMapping("queryByNo")
    public Info queryByNo(String no) {
        logger.info("InfoContrller queryByNo start,no={}",no);
        return infoService.queryByNo(no);
    }

    @ResponseBody
    @RequestMapping("delInfo")
    public Boolean delInfo(String no) {
        logger.info("InfoContrller delInfo start,no={}",no);
        return infoService.delInfo(no);
    }

    @ResponseBody
    @RequestMapping("uptInfo")
    public Boolean uptInfo(Info info) {
        logger.info("InfoContrller uptInfo start,info={}",info);
        return infoService.uptInfo(info);
    }
}
