package com.shopstat.executor;

import org.joda.time.DateTime;

/**
 * 任务执行器
 */
public interface IExecutor {

    public void exec(DateTime dateTime);
}
