package com.shop.service.aftersale;

import com.shop.dao.ext.aftersale.SafeguardRightDao;
import com.shop.model.pojo.OdSafeguardRight;
import com.shop.model.vo.AftersaleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 维权服务类
 */
@Service
public class SafeguardRightService {

    @Autowired
    private SafeguardRightDao safeguardRightDao;

    public int count(AftersaleQueryVo queryVo)
    {
        return safeguardRightDao.count(queryVo);
    }

    public List<OdSafeguardRight> queryPage(AftersaleQueryVo queryVo)
    {
        return safeguardRightDao.queryPage(queryVo);
    }
}