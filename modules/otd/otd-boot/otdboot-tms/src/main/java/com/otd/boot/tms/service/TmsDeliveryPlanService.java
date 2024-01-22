package com.otd.boot.tms.service;

import com.otd.boot.tms.model.entity.TmsDeliveryPlan;

public interface TmsDeliveryPlanService {

    /**
     * 根据发货单号查询配送计划
     */
    TmsDeliveryPlan queryByDeliveryNo(String deliveryNo);
}
