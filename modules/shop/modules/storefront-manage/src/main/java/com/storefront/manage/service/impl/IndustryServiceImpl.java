package com.storefront.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.storefront.manage.dao.IndustryDao;
import com.storefront.manage.model.vo.IndustryQueryVo;
import com.storefront.manage.model.vo.IndustryVo;
import com.storefront.manage.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustryServiceImpl implements IndustryService {

    @Autowired
    private IndustryDao industryDao;

    public Page<IndustryVo> queryPage(IndustryQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<IndustryVo> list = industryDao.queryPage(queryVo);
        PageInfo<IndustryVo> pageInfo = new PageInfo<IndustryVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<IndustryVo> page = new Page<IndustryVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }
}
