package com.shop.book.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.book.manage.dao.shop.QaDao;
import com.shop.book.manage.model.vo.QaQueryVo;
import com.shop.book.manage.model.vo.QaVo;
import com.shop.book.manage.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QaServiceImpl implements QaService {

    @Autowired
    private QaDao qaDao;

    public Page<QaVo> queryPage(QaQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<QaVo> list = qaDao.queryPage(queryVo);
        PageInfo<QaVo> pageInfo = new PageInfo<QaVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<QaVo> page = new Page<QaVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }
}
