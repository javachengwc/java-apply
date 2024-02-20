package com.otd.boot.oms.service.base;

import com.otd.boot.oms.model.entity.BaseProduct;

public interface BaseProductService {

    /**
     * 根据产品编码查询物料
     */
    BaseProduct queryByProductCode(String productCode);
}
