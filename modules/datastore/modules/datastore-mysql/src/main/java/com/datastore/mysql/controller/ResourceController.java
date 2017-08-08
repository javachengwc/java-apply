package com.datastore.mysql.controller;

import com.datastore.mysql.model.Resource;
import com.datastore.mysql.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("resource")
public class ResourceController {

    private static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private ResourceService resourceService;

    @ResponseBody
    @RequestMapping("getByName")
    public Resource getByName(String name)
    {
        logger.info("ResourceController getByName start,name={}",name);
        return resourceService.getByName(name);
    }

    @ResponseBody
    @RequestMapping("getById")
    public Resource getById(Integer id)
    {
        logger.info("ResourceController getById start,id={}",id);
        return resourceService.getById(id);
    }

    @ResponseBody
    @RequestMapping("countAll")
    public Integer countAll()
    {
        logger.info("ResourceController countAll start................");
        return resourceService.countAll();
    }

    @ResponseBody
    @RequestMapping("queryAll")
    public List<Resource> queryAll()
    {
        logger.info("ResourceController queryAll start.............");
        return resourceService.queryAll();
    }

}
