package com.datastore.mysql.controller;

import com.datastore.mysql.model.dto.ResourceDto;
import com.datastore.mysql.model.entity.Resource;
import com.datastore.mysql.service.ResourceService;
import com.datastore.mysql.service.impl.ResourceServiceImpl;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CountDownLatch;


@RestController
@RequestMapping("/lock")
@Api(description="数据库锁接口")
public class LockController {

    private static Logger logger = LoggerFactory.getLogger(LockController.class);

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/prepareLatch")
    @ApiOperation("准备并发信号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "数量", required = false, defaultValue = "1", dataType = "Integer", paramType = "query")
    })
    public boolean prepareLatch(@RequestParam(value = "num",required = false,defaultValue = "1") Integer num)
    {
        logger.info("LockController prepareLatch start,num={}",num);
        if(num==null) {
            num=1;
        }
        ResourceServiceImpl.latch = new CountDownLatch(num);
        return true;
    }

    @PostMapping("/releaseLatch")
    @ApiOperation("释放并发信号")
    public boolean releaseLatch()
    {
        logger.info("LockController releaseLatch start......");
        ResourceServiceImpl.latch.countDown();
        return true;
    }

    @PostMapping("/uptResource")
    @ApiOperation("更新资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isShow", value = "是否显示", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer", paramType = "query")
    })
    public ResourceDto uptResource(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "isShow",required = false) Integer isShow,
                                   @RequestParam(value = "sort",required = false) Integer sort)
    {
        logger.info("LockController uptResource start,name={}",name);
        Resource paramRes = new Resource();
        paramRes.setName(name);
        paramRes.setIsShow(isShow);
        paramRes.setSort(sort);

        resourceService.uptByName(paramRes);
        Resource resource = resourceService.getByName(name);
        ResourceDto resourceDto = TransUtil.transEntity(resource,ResourceDto.class);
        return resourceDto;
    }

    @PostMapping("/showResource")
    @ApiOperation("更改资源为显示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "query")
    })
    public ResourceDto showResource(@RequestParam(value = "name") String name)
    {
        logger.info("LockController showResource start,name={}",name);
        resourceService.uptShowByName(name);
        Resource resource = resourceService.getByName(name);
        ResourceDto resourceDto = TransUtil.transEntity(resource,ResourceDto.class);
        return resourceDto;
    }

}
