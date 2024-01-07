package com.otd.demo.service.impl;

import com.otd.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DemoServiceImpl implements DemoService {

    @Override
    public int updateDemo(int id) {
        log.info("DemoServiceImpl updateDemo id={}",id);
        return 1;
    }
}
