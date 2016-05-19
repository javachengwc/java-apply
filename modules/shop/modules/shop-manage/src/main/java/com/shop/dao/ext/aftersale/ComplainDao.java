package com.shop.dao.ext.aftersale;

import com.shop.dao.BaseDao;
import com.shop.model.pojo.OdComplain;
import com.shop.model.vo.AftersaleQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 投诉访问类
 */
@Repository
public class ComplainDao   extends BaseDao {

    public int count(AftersaleQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.shop.dao.ext.aftersale.ComplainDao.count", queryVo);
    }

    public List<OdComplain> queryPage(AftersaleQueryVo queryVo)
    {
        return getSqlSession().selectList("com.shop.dao.ext.aftersale.ComplainDao.queryPage", queryVo);
    }
}