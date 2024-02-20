package com.otd.boot.oms.service.base.impl;

import com.otd.boot.oms.dao.mapper.BaseProductMapper;
import com.otd.boot.oms.model.entity.BaseProduct;
import com.otd.boot.oms.model.entity.BaseProductExample;
import com.otd.boot.oms.service.base.BaseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseProductServiceImpl implements BaseProductService {

    @Autowired
    private BaseProductMapper baseProductMapper;

    @Override
    public BaseProduct queryByProductCode(String productCode) {
        BaseProductExample example = new BaseProductExample();
        example.createCriteria().andProductCodeEqualTo(productCode);
        List<BaseProduct> list =baseProductMapper.selectByExample(example);
        if(list==null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
