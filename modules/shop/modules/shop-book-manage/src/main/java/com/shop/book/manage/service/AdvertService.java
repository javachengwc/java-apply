package com.shop.book.manage.service;

import com.shop.base.model.Page;
import com.shop.book.manage.model.vo.AdvertQueryVo;
import com.shop.book.manage.model.vo.AdvertVo;

public interface AdvertService {

    public Page<AdvertVo> queryPage(AdvertQueryVo queryVo);
}
