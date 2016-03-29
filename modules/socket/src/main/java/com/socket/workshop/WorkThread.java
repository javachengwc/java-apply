package com.socket.workshop;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class WorkThread extends Thread
{
	private static Logger m_logger = Logger.getLogger(WorkThread.class);
	
	private LinkedBlockingQueue<TaskItem> m_taskQueue;
	
	private ITaskWorker m_iTaskWorker;
	
	public static int index = 0;
	
	public WorkThread(LinkedBlockingQueue<TaskItem> taskQueue,
		String workerClassName) throws Exception
	{
		m_taskQueue = taskQueue;
		
		m_iTaskWorker = (ITaskWorker)Class.forName(workerClassName)
			.newInstance();
		m_iTaskWorker.onInit();
		
		this.setName("work thread - "
			+ m_iTaskWorker.getClass().getSimpleName() + index);
		index++;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				TaskItem item = m_taskQueue.take();
				onTask(item);
			}
			catch(Exception ex)
			{
				m_logger.error("run task error!", ex);
			}
		}
	}
	
	public void onTask(TaskItem taskItem)
		throws Exception
	{
		m_iTaskWorker.doTask(taskItem);
	}
	
	public void onError(Exception ex, TaskItem taskItem)
		throws Exception
	{
		
	}
}
