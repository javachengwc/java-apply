package com.otd.boot.plat.service.impl;

import com.otd.boot.plat.dao.mapper.WarehouseMapper;
import com.otd.boot.plat.model.entity.Warehouse;
import com.otd.boot.plat.model.entity.WarehouseExample;
import com.otd.boot.plat.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public Warehouse queryByWarehouseCode(String warehouseCode) {
        WarehouseExample example = new WarehouseExample();
        example.createCriteria().andWarehouseCodeEqualTo(warehouseCode);
        List<Warehouse> list = warehouseMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

}
