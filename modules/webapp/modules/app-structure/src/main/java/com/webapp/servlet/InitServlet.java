package com.webapp.servlet;

import java.util.Set;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.webapp.init.Init;
import com.webapp.task.SchemaTask;
import com.webapp.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = -12192220121L;
	
    private static final Logger logger = LoggerFactory.getLogger(InitServlet.class);
	
	public static Timer timer=new Timer();

	@Override
	public void init(ServletConfig config) throws ServletException {  
		  
		super.init(config);
		
		logger.error("InitServlet start");
		
		logger.error("init data start");
		
		Set<Init> inits = SpringContextUtils.getBeanSetOfType(Init.class);
		for(Init init :inits)
		{
			logger.error(" init data  name="+init.getClass().getName());
			init.init();
		}
		
		logger.error("tast start");

		Set<SchemaTask> set =SpringContextUtils.getBeanSetOfType(SchemaTask.class);
		
		for(SchemaTask task:set)
		{
			logger.error(" tast "+task.getClass().getName()+ " schedule "+task.getBeginTime() +" "+task.getFixTime() );
			timer.scheduleAtFixedRate(task,task.getBeginTime(),task.getFixTime());
		}
		
		logger.error("InitServlet end");
	} 
	
	@Override
	public void destroy()
    {
		super.destroy();
		timer.cancel();
		timer=null;
    }
	
}
