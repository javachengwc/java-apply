package com.druid.controller;

import com.druid.model.pojo.Entity;
import com.druid.service.EntityService;
import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Api(description = "entity接口")
@RequestMapping("/entity")
@Slf4j
@RestController
public class EntityController {

    @Autowired
    private EntityService entityService;

    @RequestMapping(value = "/queryByName", method = { RequestMethod.GET})
    @ApiOperation("根据名称查询entity")
    public Resp<List<Entity>> queryByName(String name) {
        log.info("EntityController queryByName start,name={}", name);
        if(StringUtils.isBlank(name) ) {
            return Resp.error("参数不能为空");
        }
        List<Entity> list = entityService.queryByName(name);
        list = list==null? Collections.emptyList():list;
        return Resp.data(list);
    }

    @RequestMapping(value = "/addEntity", method = { RequestMethod.POST})
    @ApiOperation("添加entity")
    public Resp<Entity> addEntity(String name) {
        log.info("EntityController addEntity start,name={}", name);
        if(StringUtils.isBlank(name) ) {
            return Resp.error("参数不能为空");
        }
        Entity entity= entityService.addEntity(name);
        return Resp.data(entity);
    }

}
