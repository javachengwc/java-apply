package com.shop.book.manage.dao.shop;

import com.shop.book.manage.model.vo.DictQueryVo;
import com.shop.book.manage.model.vo.DictVo;

import java.util.List;

public interface DictDao {

    public List<DictVo> queryPage(DictQueryVo queryVo);
}
