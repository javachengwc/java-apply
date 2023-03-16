package com.druid.service.impl;

import com.druid.dao.mapper.LogMapper;
import com.druid.model.pojo.Log;
import com.druid.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordLog(Log log) {
        logMapper.insertSelective(log);
        return true;
    }

}
