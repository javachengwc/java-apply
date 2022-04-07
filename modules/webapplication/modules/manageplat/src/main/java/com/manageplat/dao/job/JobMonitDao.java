package com.manageplat.dao.job;

import com.manageplat.model.job.JobMonit;
import com.manageplat.model.vo.job.JobMonitQueryVo;

import java.util.List;

/**
 * 任务监控dao类
 */
public interface JobMonitDao {

    public int insert(JobMonit jobMonit);

    public void updateSelectiveById(JobMonit jobMonit);

    public List<JobMonit> queryPage(JobMonitQueryVo queryVo);
}
