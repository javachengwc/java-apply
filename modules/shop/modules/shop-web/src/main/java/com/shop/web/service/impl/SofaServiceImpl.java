package com.shop.web.service.impl;

import com.shop.web.dao.SofaDao;
import com.shop.web.model.pojo.SfProduct;
import com.shop.web.model.vo.SofaQueryVo;
import com.shop.web.service.SofaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SofaServiceImpl implements SofaService {

    @Autowired
    private SofaDao sofaDao;

    public List<SfProduct> queryPage(SofaQueryVo queryVo) {

        return sofaDao.queryPage(queryVo);

    }

    public Integer count(SofaQueryVo queryVo) {

        return sofaDao.count(queryVo);
    }
}
