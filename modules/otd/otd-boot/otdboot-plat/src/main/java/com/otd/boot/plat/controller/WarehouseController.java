package com.otd.boot.plat.controller;

import com.model.base.Resp;
import com.otd.boot.component.util.TransUtil;
import com.otd.boot.plat.model.entity.Warehouse;
import com.otd.boot.plat.model.vo.WarehouseVo;
import com.otd.boot.plat.service.WarehouseService;
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
@RequestMapping("/warehouse")
@Api(value = "仓库接口", description = "仓库接口")
@Slf4j
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/queryByWarehouseCode")
    @ApiOperation("根据仓库code查询仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "warehouseCode", value = "warehouseCode", required = true, dataType = "String", paramType = "query")
    })
    public Resp<WarehouseVo> queryByWarehouseCode(@RequestParam(value = "warehouseCode", required = true) String warehouseCode) {
        log.info("WarehouseController queryByWarehouseCode start,warehouseCode={}",warehouseCode);
        Warehouse warehouse =warehouseService.queryByWarehouseCode(warehouseCode);
        if(warehouse==null) {
            return Resp.error("查无仓库");
        }
        WarehouseVo vo = TransUtil.transEntity(warehouse, WarehouseVo.class);
        return Resp.data(vo);
    }
}
