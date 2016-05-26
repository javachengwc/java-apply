package com.shopstat.task;

import com.shopstat.executor.IExecutor;
import org.joda.time.DateTime;

public class BaseTask implements ITask{

    public void doTask(IExecutor executor,DateTime dateTime)
    {
        executor.exec(dateTime);
    }
}
