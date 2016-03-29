package com.socket.workshop;

public interface ITaskWorker
{
	public void onInit()
		throws Exception;
	
	public void doTask(TaskItem taskItem)
		throws Exception;
	
}
