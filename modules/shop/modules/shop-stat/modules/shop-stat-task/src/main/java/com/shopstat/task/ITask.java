package com.shopstat.task;

import com.shopstat.executor.IExecutor;
import org.joda.time.DateTime;

/**
 * 任务父类
 */
public interface ITask {

    public void doTask(IExecutor executor,DateTime dateTime);
}
