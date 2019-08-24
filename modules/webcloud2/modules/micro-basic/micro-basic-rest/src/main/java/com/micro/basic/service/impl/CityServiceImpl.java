package com.micro.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.basic.dao.mapper.CityMapper;
import com.micro.basic.model.pojo.City;
import com.micro.basic.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

    public List<City> queryCityList(String provinceCode) {
        List<City> list = baseMapper.selectList(new QueryWrapper<City>().eq("parent_code",provinceCode));
        return list;
    }

}
