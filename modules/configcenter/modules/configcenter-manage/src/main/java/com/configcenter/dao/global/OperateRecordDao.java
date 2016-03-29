package com.configcenter.dao.global;

import com.configcenter.model.OperateRecord;
import com.configcenter.vo.CommonQueryVo;

import java.util.List;

/**
 * 操作记录访问类
 */
public interface OperateRecordDao{

    public int countAll();

    public int add(OperateRecord operateRecord);

    public List<OperateRecord> queryList(CommonQueryVo queryVo);

    public int count(CommonQueryVo queryVo);
}
