package com.shop.book.manage.service;

import com.shop.base.model.Page;
import com.shop.book.manage.model.vo.QaQueryVo;
import com.shop.book.manage.model.vo.QaVo;

public interface QaService {

    public Page<QaVo> queryPage(QaQueryVo queryVo);
}
