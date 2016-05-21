package com.shopstat.dao.ext.aftersale;

import com.shopstat.dao.BaseDao;
import com.shopstat.model.pojo.StatSafeguard;
import com.shopstat.model.vo.StatQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 维权统计dao类
 */
@Repository
public class SafeguardStatDao extends BaseDao {

    public int count(StatQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.shopstat.dao.ext.aftersale.SafeguardStatDao.count", queryVo);
    }

    public List<StatSafeguard> queryPage(StatQueryVo queryVo)
    {
        return getSqlSession().selectList("com.shopstat.dao.ext.aftersale.SafeguardStatDao.queryPage", queryVo);
    }
}