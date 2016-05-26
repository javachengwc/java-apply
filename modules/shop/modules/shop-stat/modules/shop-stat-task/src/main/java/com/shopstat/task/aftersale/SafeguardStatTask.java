package com.shopstat.task.aftersale;

import com.shopstat.executor.IExecutor;
import com.shopstat.task.BaseTask;
import com.shopstat.task.SpringContextLoadUtil;
import com.shopstat.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;

/**
 * 维权统计任务类
 */
public class SafeguardStatTask extends BaseTask {

    public static void main(String args [])
    {

        ApplicationContext applicationContext=SpringContextLoadUtil.getApplicationContext();
        IExecutor executor =applicationContext.getBean("safeguardStatExecutor",IExecutor.class);
        DateTime statDate = DateTimeUtil.getDateTime(new DateTime().minusDays(1).toString(DateTimeUtil.FMT_YMD),DateTimeUtil.FMT_YMD);
        if(args!=null && args.length>0 && DateTimeUtil.checkDate(args[0]))
        {
            statDate= DateTimeUtil.getDateTime(args[0],DateTimeUtil.FMT_YMD);
        }

        SafeguardStatTask task = new SafeguardStatTask();
        task.doTask(executor,statDate);
    }
}
