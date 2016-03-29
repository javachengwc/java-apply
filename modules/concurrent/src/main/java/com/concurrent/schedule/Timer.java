package com.concurrent.schedule;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 基于ScheduledExecutorService的定时器实现
 */
public class Timer {

	private ScheduledExecutorService extScheduledThreadPoolExecutor;

	private final Map<StatEnableTask, Runnable> scheduleTaskMap = new ConcurrentHashMap<StatEnableTask, Runnable>();

	public void setExtScheduledThreadPoolExecutor(ScheduledExecutorService extScheduledThreadPoolExecutor) {
		this.extScheduledThreadPoolExecutor = extScheduledThreadPoolExecutor;
	}

	public String getDescription() {
		return "A timer scheduler";
	}

	public String getName() {
		return getClass().getName();
	}

	public void init() {
	}

	public void destory() {
		extScheduledThreadPoolExecutor.shutdown();
	}

	public Map<StatEnableTask, Runnable> getUnscheduleTasks() {
		return scheduleTaskMap;
	}

	public ScheduledExecutorService getScheduler() {
		return extScheduledThreadPoolExecutor;
	}

	public Set<StatEnableTask> getWrapperRunnableSet() {
		return scheduleTaskMap.keySet();
	}

	public Collection<Runnable> getRunnables() {
		return scheduleTaskMap.values();
	}

	public ScheduledFuture<?> schedule(Runnable command, int delaySeconds) {
		return schedule(command, delaySeconds, TimeUnit.SECONDS);
	}

	@SuppressWarnings("unchecked")
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		long delayMs = TimeUnit.MILLISECONDS.convert(delay, unit);
		StatEnableTask task = new StatEnableTask(command, delayMs, scheduleTaskMap);
		return new WrapperScheduledFuture(extScheduledThreadPoolExecutor.schedule(task, delay, unit), task,
				scheduleTaskMap);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long delaySeconds, long periodSeconds) {
		return scheduleAtFixedRate(command, delaySeconds, periodSeconds, TimeUnit.SECONDS);
	}

	@SuppressWarnings("unchecked")
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		long delayMs = TimeUnit.MILLISECONDS.convert(initialDelay, unit);
		StatEnableTask task = new StatEnableTask(command, delayMs, true, scheduleTaskMap);
		return new WrapperScheduledFuture(extScheduledThreadPoolExecutor.scheduleAtFixedRate(task, initialDelay,
				period, unit), task, scheduleTaskMap);
	}

	private static class StatEnableTask implements Runnable {

		private final Runnable task;
		private final Map<StatEnableTask, Runnable> unscheduleTaskSet;
		private final boolean isScheduleAtFixed;

		private final long executeTime;
		private long createTime;
		private long runTime = 0;
		private long endTime = 0;

		public StatEnableTask(Runnable task, long delayMs, Map<StatEnableTask, Runnable> unscheduleTaskMap) {
			this(task, delayMs, false, unscheduleTaskMap);
		}

		public StatEnableTask(Runnable task, long delayMs, boolean isScheduleAtFixed,
				Map<StatEnableTask, Runnable> unscheduleTaskMap) {
			this.task = task;
			this.unscheduleTaskSet = unscheduleTaskMap;
			this.executeTime = System.currentTimeMillis() + delayMs;
			this.isScheduleAtFixed = isScheduleAtFixed;
			onTaskCreate();
		}

		protected void onTaskCreate() {
			unscheduleTaskSet.put(this, task);
			this.createTime = System.currentTimeMillis();
		}

		protected void onBeforeRun() {
			this.runTime = System.currentTimeMillis();
		}

		protected void onAfterRun() {
			this.endTime = System.currentTimeMillis();
			unscheduleTaskSet.remove(this);
		}

		public void run() {
			onBeforeRun();
			try {
				task.run();
			} finally {
				onAfterRun();
			}

		}

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append("Task type ").append(task.getClass()).append(", createTime=").append(createTime);
			buf.append(", runTime=").append(runTime).append(", endTime=").append(endTime);
			if (runTime != 0 && endTime == 0) {
				buf.append(", executeTime=").append((System.currentTimeMillis() - runTime)).append("ms");
			} else if (runTime != 0 && endTime != 0) {
				buf.append(", executeDuration=").append((endTime - runTime)).append("ms");
			} else if (runTime == 0) {
				buf.append(", schedule time will be ").append(executeTime);
			}
			buf.append(", isScheduleAtFixed=").append(isScheduleAtFixed);
			return buf.toString();
		}

		@Override
		public int hashCode() {
			return task.hashCode();
		}
	}

	private static class WrapperScheduledFuture<V> implements ScheduledFuture<V> {

		private final ScheduledFuture<V> wrapperFuture;
		private final StatEnableTask task;
		Map<StatEnableTask, Runnable> taskMap;

		public WrapperScheduledFuture(ScheduledFuture<V> future, StatEnableTask task,
				Map<StatEnableTask, Runnable> scheduleTaskMap) {
			this.wrapperFuture = future;
			this.task = task;
			this.taskMap = scheduleTaskMap;
		}

		public long getDelay(TimeUnit unit) {
			return wrapperFuture.getDelay(unit);
		}

		public int compareTo(Delayed o) {
			return wrapperFuture.compareTo(o);
		}

		public boolean cancel(boolean mayInterruptIfRunning) {
			this.taskMap.remove(this.task);
			return wrapperFuture.cancel(mayInterruptIfRunning);
		}

		public V get() throws InterruptedException, ExecutionException {
			return wrapperFuture.get();
		}

		public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return wrapperFuture.get(timeout, unit);
		}

		public boolean isCancelled() {
			return wrapperFuture.isCancelled();
		}

		public boolean isDone() {
			return wrapperFuture.isDone();
		}
	}
}
