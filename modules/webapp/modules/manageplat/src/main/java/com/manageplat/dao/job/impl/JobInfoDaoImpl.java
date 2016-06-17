package com.manageplat.dao.job.impl;

import com.manageplat.dao.BaseDao;
import com.manageplat.dao.job.JobInfoDao;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.job.JobQueryVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务dao类
 */
public class JobInfoDaoImpl extends BaseDao implements JobInfoDao {

    public int insert(JobInfo jobInfo)
    {
        return this.getSqlSession().insert("com.manageplat.dao.job.impl.JobInfoDaoImpl.insert",jobInfo);
    }

    //只更新有值的字段
    public void updateSelectiveById(JobInfo jobInfo)
    {
        this.getSqlSession().update("com.manageplat.dao.job.impl.JobInfoDaoImpl.updateSelectiveById",jobInfo);
    }

    public JobInfo getJobByName(String name)
    {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("name",name);
        return  this.getSqlSession().selectOne("com.manageplat.dao.job.impl.JobInfoDaoImpl.getJobByName", param);
    }

    public JobInfo getJobById(Integer id)
    {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("id",id);
        return  this.getSqlSession().selectOne("com.manageplat.dao.job.impl.JobInfoDaoImpl.getJobById", param);
    }

    public List<JobInfo> queryPage(JobQueryVo queryVo)
    {
        return this.getSqlSession().selectList("com.manageplat.dao.job.impl.JobInfoDaoImpl.queryPage",queryVo);
    }

    public int count(JobQueryVo queryVo)
    {
        return this.getSqlSession().selectOne("com.manageplat.dao.job.impl.JobInfoDaoImpl.count", queryVo);
    }

    public int uptLockRunStatus(String runStatus,Integer id,String oldRunStatus)
    {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("id",id);
        param.put("cdnRunStatus",oldRunStatus);
        param.put("runStatus",runStatus);
        return this.getSqlSession().update("com.manageplat.dao.job.impl.JobInfoDaoImpl.uptLockRunStatus",param);
    }

    public List<JobInfo> querySubJob(int jobId)
    {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("parentId",jobId);
        return  this.getSqlSession().selectList("com.manageplat.dao.job.impl.JobInfoDaoImpl.querySubJob", param);
    }
}
