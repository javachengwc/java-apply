package com.commonservice.manage.common.utils.job;

import org.quartz.JobExecutionContext;
import com.commonservice.manage.project.monitor.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 * 
 *
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
