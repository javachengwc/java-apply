package com.socket.workshop;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class WorkShop
{
	private static Logger m_logger = Logger.getLogger(WorkShop.class);
	
	private LinkedBlockingQueue<TaskItem> m_taskQueue = new LinkedBlockingQueue<TaskItem>();
	
	private Set<WorkThread> m_threadPool = new HashSet<WorkThread>();
	
	public WorkShop() throws Exception
	{
	}
	
	public void create(int workerCount, String workerClassName)
		throws Exception
	{
		for(int i = 0; i < workerCount; i++)
		{
			WorkThread worker = new WorkThread(m_taskQueue, workerClassName);
			worker.start();
			m_threadPool.add(worker);
		}
	}
	
	private int num = 0;
	
	public void pushTask(TaskItem taskitem)
		throws Exception
	{
		m_taskQueue.put(taskitem);
		if(m_taskQueue.size() >= 10)
		{
			if(num % 5 == 0)
			{
				m_logger.warn("the m_taskQueue size="+m_taskQueue.size()+"num:"+num);
			}
			num++;
		}
	}
}
