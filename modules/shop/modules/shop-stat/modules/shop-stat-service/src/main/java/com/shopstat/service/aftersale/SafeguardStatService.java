package com.shopstat.service.aftersale;

import com.shopstat.dao.ext.aftersale.SafeguardStatDao;
import com.shopstat.model.pojo.StatSafeguard;
import com.shopstat.model.vo.StatQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 维权统计服务类
 */
@Service
public class SafeguardStatService {

    @Autowired
    private SafeguardStatDao safeguardStatDao;

    public int count(StatQueryVo queryVo)
   {
       return safeguardStatDao.count(queryVo);
   }

    public List<StatSafeguard> queryPage(StatQueryVo queryVo)
    {
        return safeguardStatDao.queryPage(queryVo);
    }

}
