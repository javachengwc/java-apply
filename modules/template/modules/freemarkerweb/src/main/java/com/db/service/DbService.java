package com.db.service;


import com.db.dao.ext.DbDao;
import com.db.model.pojo.TbDb;
import com.db.model.vo.DbQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbService {

    @Autowired
    private DbDao dbDao;

    public int count(DbQueryVo queryVo)
    {
        return dbDao.count(queryVo);
    }

    public List<TbDb> queryPage(DbQueryVo queryVo) {
        return dbDao.queryPage(queryVo);
    }

}
