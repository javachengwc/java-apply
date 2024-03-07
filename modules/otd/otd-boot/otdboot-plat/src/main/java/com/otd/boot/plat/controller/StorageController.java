package com.otd.boot.plat.controller;


import com.model.base.Resp;
import com.otd.boot.component.util.TransUtil;
import com.otd.boot.plat.model.entity.Storage;
import com.otd.boot.plat.model.vo.StorageVo;
import com.otd.boot.plat.service.StorageService;
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
 * 库存地接口
 */
@RestController
@RequestMapping("/storage")
@Api(value = "库存地接口", description = "库存地接口")
@Slf4j
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/queryByStorageCode")
    @ApiOperation("根据库存地编码查询库存地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storageCode", value = "storageCode", required = true, dataType = "String", paramType = "query")
    })
    public Resp<StorageVo> queryByStorageCode(@RequestParam(value = "storageCode", required = true) String storageCode) {
        log.info("StorageController queryByStorageCode start,storageCode={}",storageCode);
        Storage storage =storageService.queryByStorageCode(storageCode);
        if(storage==null) {
            return Resp.error("查无库存地");
        }
        StorageVo vo = TransUtil.transEntity(storage, StorageVo.class);
        return Resp.data(vo);
    }
}
