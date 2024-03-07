package com.otd.boot.plat.controller;

import com.model.base.Resp;
import com.otd.boot.component.util.TransUtil;
import com.otd.boot.plat.model.entity.LogisticsProvider;
import com.otd.boot.plat.model.vo.LogisticsProviderVo;
import com.otd.boot.plat.service.LogisticsProviderService;
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
 * 物流商接口
 */
@RestController
@RequestMapping("/logisticsProvider")
@Api(value = "物流商接口", description = "物流商接口")
@Slf4j
public class LogisticsProviderController {

    @Autowired
    private LogisticsProviderService logisticsProviderService;

    @GetMapping("/queryByNo")
    @ApiOperation("根据物流商编号查询物流商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "logisticsProviderNo", value = "logisticsProviderNo", required = true, dataType = "String", paramType = "query")
    })
    public Resp<LogisticsProviderVo> queryByNo(@RequestParam(value = "logisticsProviderNo", required = true) String logisticsProviderNo) {
        log.info("LogisticsProviderController queryByNo start,logisticsProviderNo={}",logisticsProviderNo);
        LogisticsProvider logisticsProvider =logisticsProviderService.queryByNo(logisticsProviderNo);
        if(logisticsProvider==null) {
            return Resp.error("查无物流商");
        }
        LogisticsProviderVo vo = TransUtil.transEntity(logisticsProvider, LogisticsProviderVo.class);
        return Resp.data(vo);
    }
}
