package com.micro.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.basic.dao.mapper.NationMapper;
import com.micro.basic.model.pojo.Nation;
import com.micro.basic.service.NationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements NationService {

    public List<Nation> queryAll() {
        List<Nation> list =this.list();
        return list;
    }
}
