package com.shop.book.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.book.manage.dao.shop.DictDao;
import com.shop.book.manage.model.vo.DictQueryVo;
import com.shop.book.manage.model.vo.DictVo;
import com.shop.book.manage.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictDao dictDao;

    public Page<DictVo> queryPage(DictQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<DictVo> list = dictDao.queryPage(queryVo);
        PageInfo<DictVo> pageInfo = new PageInfo<DictVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<DictVo> page = new Page<DictVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }
}
