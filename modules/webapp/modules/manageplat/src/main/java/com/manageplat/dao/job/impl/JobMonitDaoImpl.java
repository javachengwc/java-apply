package com.manageplat.dao.job.impl;

import com.manageplat.dao.BaseDao;
import com.manageplat.dao.job.JobMonitDao;
import com.manageplat.model.job.JobMonit;
import com.manageplat.model.vo.job.JobMonitQueryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务监控dao类
 */
public class JobMonitDaoImpl extends BaseDao implements JobMonitDao {

    public int insert(JobMonit jobMonit)
    {
        return this.getSqlSession().insert("com.manageplat.dao.job.impl.JobMonitDaoImpl.insert",jobMonit);
    }

    public void updateSelectiveById(JobMonit jobMonit)
    {
        this.getSqlSession().update("com.manageplat.dao.job.impl.JobMonitDaoImpl.updateSelectiveById",jobMonit);
    }

    public List<JobMonit> queryPage(JobMonitQueryVo queryVo)
    {
        return this.getSqlSession().selectList("com.manageplat.dao.job.impl.JobMonitDaoImpl.queryPage",queryVo);
    }
}
