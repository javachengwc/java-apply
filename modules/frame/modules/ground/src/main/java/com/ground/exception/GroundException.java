package com.ground.exception;

public class GroundException extends Exception
{
	public String sql;;
	
	public String desc;
	
	public GroundException(String msg) {
		super("msg"+msg);
	}
	
	public GroundException(String msg, String sql) {
		super("msg"+msg+",sql:" +sql);
		this.sql = sql;;
	}
	
	public GroundException(String msg, String sql, String desc) {
		super("msg:" + msg+",sql:"+sql+",desc:"+desc);
		this.sql = sql;
		this.desc = desc;
	}
	
	public GroundException(String msg, Exception e) {
		super("msg:" + msg, e);
	}
	
	public GroundException(String msg, String sql, Exception e) {
		super("msg"+msg+",sql:" +sql, e);
		this.sql = sql;;
	}
	
	public GroundException(String msg, String sql, String desc, Exception e) {
		super("msg:" + msg+",sql:"+sql+",desc:"+desc,e);
		this.sql = sql;
		this.desc = desc;
	}
}
