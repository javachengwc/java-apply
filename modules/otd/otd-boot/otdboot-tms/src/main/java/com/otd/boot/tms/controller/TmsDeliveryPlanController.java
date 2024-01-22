package com.otd.boot.tms.controller;

import com.otd.boot.tms.model.entity.TmsDeliveryPlan;
import com.otd.boot.tms.service.TmsDeliveryPlanService;
import com.otd.boot.web.base.model.vo.Resp;
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
 * 配送计划接口
 */
@RestController
@RequestMapping("/plan")
@Api(value = "配送计划接口", description = "配送计划接口")
@Slf4j
public class TmsDeliveryPlanController {

    @Autowired
    private TmsDeliveryPlanService tmsDeliveryPlanService;

    @GetMapping("/queryByDeliveryNo")
    @ApiOperation("根据发货单号查询配送计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliveryNo", value = "deliveryNo", required = true, dataType = "String", paramType = "query")
    })
    public Resp<TmsDeliveryPlan> queryByDeliveryNo(@RequestParam(value = "deliveryNo", required = true) String deliveryNo) {
        log.info("TmsDeliveryPlanController queryByDeliveryNo start,deliveryNo={}",deliveryNo);
        TmsDeliveryPlan tmsDeliveryPlan =tmsDeliveryPlanService.queryByDeliveryNo(deliveryNo);
        if(tmsDeliveryPlan==null) {
            return Resp.error("无配送计划");
        }
        return Resp.data(tmsDeliveryPlan);
    }
}
