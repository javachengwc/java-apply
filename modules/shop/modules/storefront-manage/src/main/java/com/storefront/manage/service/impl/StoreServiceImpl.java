package com.storefront.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.storefront.manage.dao.StoreDao;
import com.storefront.manage.model.vo.StoreQueryVo;
import com.storefront.manage.model.vo.StoreVo;
import com.storefront.manage.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDao storeDao;

    public Page<StoreVo> queryPage(StoreQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<StoreVo> list = storeDao.queryPage(queryVo);
        PageInfo<StoreVo> pageInfo = new PageInfo<StoreVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<StoreVo> page = new Page<StoreVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }
}
