package com.otd.boot.plat.service;

import com.otd.boot.plat.model.entity.Factory;

public interface FactoryService {

    Factory queryByFactoryNo(String factoryNo);
}
