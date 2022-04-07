package com.manage.service.exception;

public class OverTimeException extends RuntimeException{

	private static final long serialVersionUID = -6365308997773301342L;
	
	private final String tip="自定义处理超时异常!";
	
    public String getMessage()
    {
    	return super.getMessage()+"\n"+tip;
    }
}
