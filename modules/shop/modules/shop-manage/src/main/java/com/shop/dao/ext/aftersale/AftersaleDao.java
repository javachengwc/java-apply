package com.shop.dao.ext.aftersale;

import com.shop.dao.BaseDao;
import com.shop.model.pojo.OdAftersale;
import com.shop.model.vo.AftersaleQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 售后访问类
 */
@Repository
public class AftersaleDao  extends BaseDao {

    public int count(AftersaleQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.shop.dao.ext.aftersale.AftersaleDao.count", queryVo);
    }

    public List<OdAftersale> queryPage(AftersaleQueryVo queryVo)
    {
        return getSqlSession().selectList("com.shop.dao.ext.aftersale.AftersaleDao.queryPage", queryVo);
    }
}
