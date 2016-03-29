package com.solr.web.action.response;

public class ExceptionResponse extends Return
{
	private static final long serialVersionUID = -561226350L;

	public ExceptionResponse(String msg)
	{
		this.put(DEFAULT_RESULT_STR, 1);
		this.put(DEFAULT_MSG_STR, msg);
	}
	
	public ExceptionResponse(String msg,Throwable e)
	{
		this(msg);
		this.put("exception",(e==null||e.getCause()==null)?"e is null":e.getCause().toString());
		
	}
	
	public ExceptionResponse(int result,String msg,Throwable e)
	{
		super(result,msg);
		this.put("exception", (e==null||e.getCause()==null)?"e is null":e.getCause().toString());
	}
	
}
