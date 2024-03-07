package com.otd.boot.plat.service;

import com.otd.boot.plat.model.entity.LogisticsProvider;

public interface LogisticsProviderService {

    LogisticsProvider queryByNo(String logisticsProviderNo);
}
