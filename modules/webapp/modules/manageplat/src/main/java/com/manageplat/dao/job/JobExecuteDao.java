package com.manageplat.dao.job;

import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobInfo;

/**
 * 任务执行dao类
 */
public interface JobExecuteDao {

    public int insert(JobExecute jobExecute);

    public void updateSelectiveById(JobExecute jobExecute);

    public JobInfo queryJobByExecuteId(Integer jobExecutId);
}
