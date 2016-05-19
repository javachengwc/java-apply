package com.shop.dao.ext.aftersale;

import com.shop.dao.BaseDao;
import com.shop.model.pojo.OdSafeguardRight;
import com.shop.model.vo.AftersaleQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 维权访问类
 */
@Repository
public class SafeguardRightDao   extends BaseDao {

    public int count(AftersaleQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.shop.dao.ext.aftersale.SafeguardRightDao.count", queryVo);
    }

    public List<OdSafeguardRight> queryPage(AftersaleQueryVo queryVo)
    {
        return getSqlSession().selectList("com.shop.dao.ext.aftersale.SafeguardRightDao.queryPage", queryVo);
    }
}