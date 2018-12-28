package com.shop.book.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.book.manage.dao.shop.AdvertDao;
import com.shop.book.manage.model.vo.AdvertQueryVo;
import com.shop.book.manage.model.vo.AdvertVo;
import com.shop.book.manage.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private AdvertDao advertDao;

    public Page<AdvertVo> queryPage(AdvertQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<AdvertVo> list = advertDao.queryPage(queryVo);
        PageInfo<AdvertVo> pageInfo = new PageInfo<AdvertVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<AdvertVo> page = new Page<AdvertVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }
}
