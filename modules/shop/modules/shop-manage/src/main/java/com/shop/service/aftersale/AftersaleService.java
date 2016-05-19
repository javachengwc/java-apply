package com.shop.service.aftersale;

import com.shop.dao.ext.aftersale.AftersaleDao;
import com.shop.model.pojo.OdAftersale;
import com.shop.model.vo.AftersaleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后服务类
 */
@Service
public class AftersaleService {

    @Autowired
    private AftersaleDao aftersaleDao;

    public int count(AftersaleQueryVo queryVo)
    {
        return aftersaleDao.count(queryVo);
    }

    public List<OdAftersale> queryPage(AftersaleQueryVo queryVo)
    {
        return aftersaleDao.queryPage(queryVo);
    }
}
