package com.shopstat.dao.ext.aftersale;

import com.shopstat.dao.BaseDao;
import com.shopstat.dao.mapper.StatSafeguardMapper;
import com.shopstat.model.pojo.StatSafeguard;
import com.shopstat.model.pojo.StatSafeguardExample;
import com.shopstat.model.vo.StatQueryVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 维权统计dao类
 */
@Repository
public class SafeguardStatDao extends BaseDao {

    @Autowired
    private StatSafeguardMapper statSafeguardMapper;

    public void deleteByDate(Date date)
    {
        StatSafeguardExample example = new StatSafeguardExample();
        StatSafeguardExample.Criteria criteria = example.createCriteria();

        criteria.andStatDateEqualTo(date);

        statSafeguardMapper.deleteByExample(example);
    }

    public void batchInsert(List<StatSafeguard> list)
    {
        insertBatch("com.shopstat.dao.ext.aftersale.SafeguardStatDao.batchInsert",list);
    }

    public int count(StatQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.shopstat.dao.ext.aftersale.SafeguardStatDao.count", queryVo);
    }

    public List<StatSafeguard> queryPage(StatQueryVo queryVo)
    {
        return getSqlSession().selectList("com.shopstat.dao.ext.aftersale.SafeguardStatDao.queryPage", queryVo);
    }
}