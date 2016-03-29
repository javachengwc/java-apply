package com.concurrent.schedule;

/**
 * 只执行一次的任务
 */
public abstract class ScheduleOnceTask extends ScheduleTask {

	private int delay;

	public ScheduleOnceTask(int delaySeconds) {
		if (delaySeconds < 0)
			throw new IllegalArgumentException("bad delay seconds " + delaySeconds);
		this.delay = delaySeconds;
	}

	@Override
	public int getScheduleDelaySeconds() {
		return delay;
	}

	@Override
	public int getScheduleIntervalSeconds() {
		return -1;
	}
}
