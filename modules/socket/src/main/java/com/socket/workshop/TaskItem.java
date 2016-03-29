package com.socket.workshop;

import java.nio.channels.SelectionKey;

public class TaskItem
{
	public SelectionKey m_key;
	public Object m_data;
	
	public TaskItem(SelectionKey key, Object data)
	{
		m_data = data;
		m_key = key;
	}
}
