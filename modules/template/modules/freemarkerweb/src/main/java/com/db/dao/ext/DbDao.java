package com.db.dao.ext;

import com.db.dao.BaseDao;
import com.db.model.pojo.TbDb;
import com.db.model.vo.DbQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DbDao extends BaseDao {

    public int count(DbQueryVo queryVo)
    {
        return (Integer)getSqlSession().selectOne("com.db.dao.ext.DbDao.count", queryVo);
    }

    public List<TbDb> queryPage(DbQueryVo queryVo)
    {
        return getSqlSession().selectList("com.db.dao.ext.DbDao.queryPage", queryVo);
    }
}