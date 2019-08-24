package com.micro.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.basic.model.pojo.City;

import java.util.List;

public interface CityService extends IService<City> {

    public List<City> queryCityList(String provinceCode);
}
