package com.es.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.es.consumer.model.entity.Test;

import java.util.List;

/**
 * test服务类
 */
public interface TestService extends IService<Test> {

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    public List<Test> queryByName(String name);

}
