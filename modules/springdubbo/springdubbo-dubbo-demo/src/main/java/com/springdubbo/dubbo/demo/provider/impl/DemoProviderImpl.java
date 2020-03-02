package com.springdubbo.dubbo.demo.provider.impl;

import com.model.base.Resp;
import com.springdubbo.dubbo.demo.provider.IDemoProvider;
import org.apache.dubbo.config.annotation.Service;

@Service
public class DemoProviderImpl implements IDemoProvider {

    @Override
    public Resp<Integer> getDemo(Integer id) {
        return Resp.data(id);
    }
}
