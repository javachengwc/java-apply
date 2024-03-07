package com.otd.boot.plat.service.impl;

import com.otd.boot.plat.dao.mapper.LogisticsProviderMapper;
import com.otd.boot.plat.model.entity.LogisticsProvider;
import com.otd.boot.plat.model.entity.LogisticsProviderExample;
import com.otd.boot.plat.service.LogisticsProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticsProviderServiceImpl implements LogisticsProviderService {

    @Autowired
    private LogisticsProviderMapper logisticsProviderMapper;

    @Override
    public LogisticsProvider queryByNo(String logisticsProviderNo) {
        LogisticsProviderExample example= new LogisticsProviderExample();
        example.createCriteria().andLogisticsProviderNoEqualTo(logisticsProviderNo);
        List<LogisticsProvider> list = logisticsProviderMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
