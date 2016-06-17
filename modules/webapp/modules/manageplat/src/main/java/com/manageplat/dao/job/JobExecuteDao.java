package com.manageplat.dao.job;

import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.job.JobExecuteQueryVo;

import java.util.List;

/**
 * 任务执行dao类
 */
public interface JobExecuteDao {

    public int insert(JobExecute jobExecute);

    public void updateSelectiveById(JobExecute jobExecute);

    public List<JobExecute> queryPage(JobExecuteQueryVo queryVo);

    public JobInfo queryJobByExecuteId(Integer jobExecutId);

    public int  queryJobExecuteCount(int jobId,int queryStartTime,int queryEndTime);
}
