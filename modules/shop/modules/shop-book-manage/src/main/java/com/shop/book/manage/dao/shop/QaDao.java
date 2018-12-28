package com.shop.book.manage.dao.shop;

import com.shop.book.manage.model.vo.QaQueryVo;
import com.shop.book.manage.model.vo.QaVo;

import java.util.List;

public interface QaDao {

    public List<QaVo> queryPage(QaQueryVo queryVo);
}
