package com.concurrent.schedule;

public abstract class ScheduleTask implements Runnable {

	/**
	 * 初次延迟执行秒数
	 */
	public abstract int getScheduleDelaySeconds();

	/**
	 * 多次执行间隔秒数
	 */
	public abstract int getScheduleIntervalSeconds();

	/**
	 * 是否只执行一次，用于区分定时任务和周期任务
	 */
	public boolean scheduleOnce() {
		return true;
	}
}
