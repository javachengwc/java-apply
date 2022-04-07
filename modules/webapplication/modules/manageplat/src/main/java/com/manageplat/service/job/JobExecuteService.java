package com.manageplat.service.job;

import com.manageplat.dao.job.JobExecuteDao;
import com.manageplat.model.job.JobExecute;
import com.manageplat.model.job.JobInfo;
import com.manageplat.model.vo.job.JobExecuteQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobExecuteService {

    @Autowired
    private JobExecuteDao jobExecuteDao;

    public int insert(JobExecute jobExecute)
    {
        return  jobExecuteDao.insert(jobExecute);
    }

    public void updateSelectiveById(JobExecute jobExecute)
    {
        jobExecuteDao.updateSelectiveById(jobExecute);
    }

    public JobInfo queryJobByExecuteId(Integer jobExecutId)
    {
        return  jobExecuteDao.queryJobByExecuteId(jobExecutId);
    }

    //获取任务执行次数
    public int  queryJobExecuteCount(int jobId,int queryStartTime,int queryEndTime)
    {
        return jobExecuteDao.queryJobExecuteCount(jobId,queryStartTime,queryEndTime);
    }

    public List<JobExecute> queryPage(JobExecuteQueryVo queryVo)
    {
        return jobExecuteDao.queryPage(queryVo);
    }
}
