package com.shop.book.manage.dao.shop;

import com.shop.book.manage.model.vo.AdvertQueryVo;
import com.shop.book.manage.model.vo.AdvertVo;

import java.util.List;

public interface AdvertDao {

    public List<AdvertVo> queryPage(AdvertQueryVo queryVo);
}
