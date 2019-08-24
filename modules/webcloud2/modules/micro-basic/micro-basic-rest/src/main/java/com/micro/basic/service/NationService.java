package com.micro.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.basic.model.pojo.Nation;

import java.util.List;

public interface NationService extends IService<Nation> {

    public List<Nation> queryAll();
}
