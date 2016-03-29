package com.configcenter.service;

import com.configcenter.dao.global.OperateRecordDao;
import com.configcenter.model.OperateRecord;
import com.configcenter.vo.CommonQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作记录服务类
 */
@Service
public class OperateRecordService {

    @Autowired
    private OperateRecordDao operateRecordDao;

    public OperateRecordDao getDao()
    {
        return operateRecordDao;
    }

    public int countAll()
    {
        return getDao().countAll();
    }

    public int add(OperateRecord t)
    {
        return getDao().add(t);
    }

    public List<OperateRecord> queryList(CommonQueryVo queryVo)
    {
        return  getDao().queryList(queryVo);
    }

    public int count(CommonQueryVo queryVo)
    {
        return getDao().count(queryVo);
    }

}

