package com.manageplat.model.job;

import com.manageplat.model.HttpUrl;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class JobHttpUrl extends HttpUrl {
	
	private boolean needLogin=false;
	
	private String loginUserId;
	
	public JobHttpUrl(String url)
	{
		super(url);
		injectValue();
	}
	
	public JobHttpUrl(String url,Map<String,String> header)
	{  
		this(url);
		this.header=header;
	}
 
	public void injectValue()
	{
		injectNeedLogin();
		loginUserId=getParam("loginUserId");
	}
	
	public void injectNeedLogin()
	{
		String needLoginStr= getParam("needLogin");
		if(!StringUtils.isBlank(needLoginStr) && "1".equals(needLoginStr))
		{
			needLogin=true;
		}
	}
	
	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	
}
