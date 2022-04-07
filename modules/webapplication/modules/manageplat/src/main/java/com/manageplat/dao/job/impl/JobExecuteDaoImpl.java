package com.manageplat.dao.job.impl;

import com.manageplat.dao.BaseDao;
import com.manageplat.dao.job.JobExecuteDao;
import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.job.JobExecuteQueryVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务执行dao类
 */
public class JobExecuteDaoImpl extends BaseDao implements JobExecuteDao {

    public int insert(JobExecute jobExecute)
    {
        return this.getSqlSession().insert("com.manageplat.dao.job.impl.JobExecuteDaoImpl.insert",jobExecute);
    }

    public void updateSelectiveById(JobExecute jobExecute)
    {
        this.getSqlSession().update("com.manageplat.dao.job.impl.JobExecuteDaoImpl.updateSelectiveById",jobExecute);
    }

    public List<JobExecute> queryPage(JobExecuteQueryVo queryVo)
    {
        return this.getSqlSession().selectList("com.manageplat.dao.job.impl.JobExecuteDaoImpl.queryPage",queryVo);
    }

    public JobInfo queryJobByExecuteId(Integer jobExecutId)
    {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("jobExecutId",jobExecutId);
        return this.getSqlSession().selectOne("com.manageplat.dao.job.impl.JobExecuteDaoImpl.queryJobByExecuteId",param);
    }

    public int  queryJobExecuteCount(int jobId,int queryStartTime,int queryEndTime)
    {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("jobId",jobId);
        param.put("queryStartTime",queryStartTime);
        param.put("queryEndTime",queryEndTime);
        return this.getSqlSession().selectOne("com.manageplat.dao.job.impl.JobExecuteDaoImpl.queryJobExecuteCount",param);
    }
}
