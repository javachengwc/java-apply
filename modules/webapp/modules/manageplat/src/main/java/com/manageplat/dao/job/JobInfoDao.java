package com.manageplat.dao.job;

import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.job.JobQueryVo;

import java.util.List;

/**
 * 任务dao类
 */
public interface JobInfoDao {

    public int insert(JobInfo jobInfo);

    //只更新有值的字段
    public void updateSelectiveById(JobInfo jobInfo);

    public JobInfo getJobByName(String name);

    public JobInfo getJobById(Integer id);

    public List<JobInfo> queryPage(JobQueryVo queryVo);

    public int count(JobQueryVo queryVo);

    public int uptLockRunStatus(String runStatus,Integer id,String oldRunStatus);

}
