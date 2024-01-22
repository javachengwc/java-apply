package com.otd.boot.tms.service.impl;

import com.otd.boot.tms.dao.mapper.TmsDeliveryPlanMapper;
import com.otd.boot.tms.model.entity.TmsDeliveryPlan;
import com.otd.boot.tms.model.entity.TmsDeliveryPlanExample;
import com.otd.boot.tms.service.TmsDeliveryPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TmsDeliveryPlanServiceImpl implements TmsDeliveryPlanService {

    @Autowired
    private TmsDeliveryPlanMapper tmsDeliveryPlanMapper;

    @Override
    public TmsDeliveryPlan queryByDeliveryNo(String deliveryNo) {
        log.info("TmsDeliveryPlanServiceImpl queryByDeliveryNo deliveryNo={}",deliveryNo);
        TmsDeliveryPlanExample example = new TmsDeliveryPlanExample();
        TmsDeliveryPlanExample.Criteria criteria =example.createCriteria();
        criteria.andDeliveryNoEqualTo(deliveryNo);
        List<TmsDeliveryPlan> list =tmsDeliveryPlanMapper.selectByExample(example);
        if(list==null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
