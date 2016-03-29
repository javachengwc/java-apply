package com.concurrent.schedule;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 扩展ScheduledThreadPoolExecutor
 * 
 */
public class ExtScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    public ExtScheduledThreadPoolExecutor(int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		//自定义操作
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
        //自定义操作
	}
}
