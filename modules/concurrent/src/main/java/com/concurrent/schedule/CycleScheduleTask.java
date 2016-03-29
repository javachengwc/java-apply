package com.concurrent.schedule;

/**
 * 周期执行的任务
 */
public abstract class CycleScheduleTask extends ScheduleTask {

	private final int delay;
	private final int interval;

	public CycleScheduleTask(int delaySeconds, int intervalSeconds) {
		this.delay = delaySeconds;
		this.interval = intervalSeconds;
	}

	@Override
	public int getScheduleDelaySeconds() {
		return delay;
	}

	@Override
	public int getScheduleIntervalSeconds() {
		return interval;
	}

	@Override
	public boolean scheduleOnce() {
		return false;
	}
}
