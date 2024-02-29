package com.otd.boot.plat.controller;

import com.model.base.Resp;
import com.otd.boot.component.util.TransUtil;
import com.otd.boot.plat.model.entity.Factory;
import com.otd.boot.plat.model.vo.FactoryVo;
import com.otd.boot.plat.service.FactoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仓库接口
 */
@RestController
@RequestMapping("/factory")
@Api(value = "仓库接口", description = "仓库接口")
@Slf4j
public class WarehouseController {

    @Autowired
    private FactoryService factoryService;

    @GetMapping("/queryByFactoryNo")
    @ApiOperation("根据工厂编号查询工厂")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "factoryNo", value = "factoryNo", required = true, dataType = "String", paramType = "query")
    })
    public Resp<FactoryVo> queryByFactoryNo(@RequestParam(value = "factoryNo", required = true) String factoryNo) {
        log.info("FactoryController queryByFactoryNo start,factoryNo={}",factoryNo);
        Factory factory =factoryService.queryByFactoryNo(factoryNo);
        if(factory==null) {
            return Resp.error("查无工厂");
        }
        FactoryVo vo = TransUtil.transEntity(factory,FactoryVo.class);
        return Resp.data(vo);
    }
}
