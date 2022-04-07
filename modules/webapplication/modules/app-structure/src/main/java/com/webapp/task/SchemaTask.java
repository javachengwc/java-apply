package com.webapp.task;

import java.util.TimerTask;

public class SchemaTask extends TimerTask{

	//开始时间
	protected long beginTime=0l;
	
	//间隔时间
	protected long fixTime;

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getFixTime() {
		return fixTime;
	}

	public void setFixTime(long fixTime) {
		this.fixTime = fixTime;
	}
    
	public SchemaTask()
	{
		
	}
	
	public SchemaTask(long beginTime,long fixTime )
	{
		this.beginTime= beginTime;
		this.fixTime = fixTime;
	}
	
	public SchemaTask(long fixTime)
	{
		this.fixTime =fixTime;
	}
	
	@Override
	public void run() {
		
		
	}
	
}
