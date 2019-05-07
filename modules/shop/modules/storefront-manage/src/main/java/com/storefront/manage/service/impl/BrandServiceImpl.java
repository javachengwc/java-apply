package com.storefront.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.storefront.manage.dao.BrandDao;
import com.storefront.manage.model.vo.BrandQueryVo;
import com.storefront.manage.model.vo.BrandVo;
import com.storefront.manage.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    public Page<BrandVo> queryPage(BrandQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<BrandVo> list = brandDao.queryPage(queryVo);
        PageInfo<BrandVo> pageInfo = new PageInfo<BrandVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<BrandVo> page = new Page<BrandVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }
}
