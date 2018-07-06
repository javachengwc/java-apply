package com.shop.book.service;

import com.shop.book.model.pojo.Advert;

import java.util.List;

public interface AdvertService  {

    //查询上线的广告列表
    public List<Advert> queryUpAdertList();
}
