package com.shop.service.aftersale;

import com.shop.dao.ext.aftersale.ComplainDao;
import com.shop.model.pojo.OdComplain;
import com.shop.model.vo.AftersaleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投诉服务类
 */
@Service
public class ComplainService {

    @Autowired
    private ComplainDao complainDao;

    public int count(AftersaleQueryVo queryVo)
    {
        return complainDao.count(queryVo);
    }

    public List<OdComplain> queryPage(AftersaleQueryVo queryVo)
    {
        return complainDao.queryPage(queryVo);
    }
}
