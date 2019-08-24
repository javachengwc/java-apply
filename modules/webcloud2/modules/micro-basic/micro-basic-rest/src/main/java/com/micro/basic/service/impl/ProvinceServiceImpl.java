package com.micro.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.basic.dao.mapper.ProvinceMapper;
import com.micro.basic.model.pojo.Province;
import com.micro.basic.service.ProvinceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements ProvinceService {

    public List<Province> queryAll() {
        List<Province> list =this.list();
        return list;
    }
}
