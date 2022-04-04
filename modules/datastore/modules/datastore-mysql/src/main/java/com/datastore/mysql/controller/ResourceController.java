package com.datastore.mysql.controller;

import com.datastore.mysql.model.dto.ResourceDto;
import com.datastore.mysql.model.entity.Resource;
import com.datastore.mysql.service.ResourceService;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource")
@Api(description="资源接口")
public class ResourceController {

    private static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/getByName")
    @ApiOperation("根据名称查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "query")
    })
    public ResourceDto getByName(@RequestParam(value = "name") String name)
    {
        logger.info("ResourceController getByName start,name={}",name);
        Resource resource = resourceService.getByName(name);
        ResourceDto resourceDto = TransUtil.transEntity(resource,ResourceDto.class);
        return resourceDto;
    }

    @GetMapping("/getById")
    @ApiOperation("根据id查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")
    })
    public ResourceDto getById(@RequestParam(value = "id") Integer id)
    {
        logger.info("ResourceController getById start,id={}",id);
        Resource resource = resourceService.getById(id);
        ResourceDto resourceDto = TransUtil.transEntity(resource,ResourceDto.class);
        return resourceDto;
    }

    @GetMapping("/countAll")
    @ApiOperation("查询总数")
    public Integer countAll()
    {
        logger.info("ResourceController countAll start................");
        return resourceService.countAll();
    }

    @GetMapping("/queryAll")
    @ApiOperation("查询所有")
    public List<ResourceDto> queryAll()
    {
        logger.info("ResourceController queryAll start.............");
        List<Resource> list = resourceService.queryAll();
        List<ResourceDto> dtoList = TransUtil.transList(list,ResourceDto.class);
        return dtoList;
    }

}
