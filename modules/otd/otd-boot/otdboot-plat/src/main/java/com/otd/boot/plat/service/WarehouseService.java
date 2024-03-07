package com.otd.boot.plat.service;

import com.otd.boot.plat.model.entity.Warehouse;

public interface WarehouseService {

    Warehouse queryByWarehouseCode(String warehouseCode);
}
