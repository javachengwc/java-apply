package com.rule.dto;

public class RuleResponse{
	
	public static RuleResponse SuccessRuleResponse = new RuleResponse(0,"success");
	
	protected String code;
	
	protected String  description;
	
	protected Object rows;

	//原始信息
	protected String originalInfo;
	
	//额外信息
	protected Object extraInfo;
	
	public RuleResponse()
	{
		
	}
	
	public RuleResponse(String  code, String description)
	{
		this.code=code;
		this.description= description;
	}
     
	public RuleResponse(Integer error, String msg)
	{
		
		String errorStr= null;
		if(error!=null)
		{
			errorStr=error.toString();
		}
		this.code=errorStr;
		this.description= msg;
	}
	
	public RuleResponse(Integer error, String msg,Object rows)
	{
		this(error,msg);
		this.rows=rows;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public String getOriginalInfo() {
		return originalInfo;
	}

	public void setOriginalInfo(String originalInfo) {
		this.originalInfo = originalInfo;
	}

	public Object getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Object extraInfo) {
		this.extraInfo = extraInfo;
	}

}
