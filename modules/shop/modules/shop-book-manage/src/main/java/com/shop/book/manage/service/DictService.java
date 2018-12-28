package com.shop.book.manage.service;

import com.shop.base.model.Page;
import com.shop.book.manage.model.vo.DictQueryVo;
import com.shop.book.manage.model.vo.DictVo;

public interface DictService {

    public Page<DictVo> queryPage(DictQueryVo queryVo);
}
