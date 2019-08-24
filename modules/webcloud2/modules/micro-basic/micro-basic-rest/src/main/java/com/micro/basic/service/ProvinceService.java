package com.micro.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.basic.model.pojo.Province;

import java.util.List;

public interface ProvinceService extends IService<Province> {

    public List<Province> queryAll();
}
