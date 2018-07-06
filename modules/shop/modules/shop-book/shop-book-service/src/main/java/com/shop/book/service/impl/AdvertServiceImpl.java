package com.shop.book.service.impl;

import com.shop.book.dao.mapper.AdvertMapper;
import com.shop.book.enums.AdvertStatuEnum;
import com.shop.book.model.pojo.Advert;
import com.shop.book.model.pojo.AdvertExample;
import com.shop.book.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private AdvertMapper advertMapper;

    //查询上线的广告列表
    public List<Advert> queryUpAdertList() {
        AdvertExample example = new AdvertExample();
        AdvertExample.Criteria criteria = example.createCriteria();
        criteria.andStatuEqualTo(AdvertStatuEnum.UP.getValue());
        example.setOrderByClause(" sort asc ");
        List<Advert> list = advertMapper.selectByExample(example);
        if(list==null) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }
}
