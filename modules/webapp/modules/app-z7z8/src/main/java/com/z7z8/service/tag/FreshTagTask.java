package com.z7z8.service.tag;

import com.webapp.task.SchemaTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class FreshTagTask extends SchemaTask {
	
	private static Logger logger = LoggerFactory.getLogger(FreshTagTask.class);
	
	public static long FIX_FRESH_TIME =10*60*60*1000;;//10小时
	
	public FreshTagTask()
	{
		this.setBeginTime(FIX_FRESH_TIME);
		this.setFixTime(FIX_FRESH_TIME);
	}
	
	public FreshTagTask(long fixTime )
	{
		this.setBeginTime(fixTime);
		this.setFixTime(fixTime);
	}

	public void run()
    {
		try
		{
			logger.info("FreshTagTask start,");
			
			TagManager.getInstance().init();
			
			logger.info("FreshTagTask end,");
		}catch(Exception e)
		{
			logger.error("FreshTagTask exception,",e);
		}
    }
}