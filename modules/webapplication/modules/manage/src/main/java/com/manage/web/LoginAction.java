package com.manage.web;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.manage.model.rbac.Adminor;
import com.manage.service.rbac.AdminorHolder;
import com.manage.service.rbac.AdminorService;
import com.manage.util.EncryptUtil;

@Namespace("/")
@Results( { @Result(name = LoginAction.INDEX, location = "/index.jsp", type = "redirect"),
	@Result(name = LoginAction.LOGIN, location = "/login.jsp", type = "dispatcher")})
public class LoginAction extends CrudActionSupport{
    
	private static final long serialVersionUID = -1239036080797506064L;

	public static final String INDEX="index";
	public static final String LOGIN="login";
	
	private Integer id;
	
	private Integer tip;
	
	private Adminor adminor;
	
	private String name;
	
	private String password;
	
	@Autowired
	private AdminorService adminorService;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Adminor getAdminor() {
		return adminor;
	}

	public void setAdminor(Adminor adminor) {
		this.adminor = adminor;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getTip() {
		return tip;
	}

	public void setTip(Integer tip) {
		this.tip = tip;
	}

	public AdminorService getAdminorService() {
		return adminorService;
	}

	public void setAdminorService(AdminorService adminorService) {
		this.adminorService = adminorService;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}
	
	public Object getModel() {
		
		return null;
	
	}
    /**
     * 登录   
     */
	public String login() throws Exception
	{
	    tip=0;
		
		if(StringUtils.isBlank(name) || StringUtils.isBlank(password))
		{
			tip=1;//登录失败
		}else
		{
			adminor=adminorService.getByName(name);
			if(adminor==null)
			{
				tip=2;//无此账号
			}
			else if(!adminor.getPassword().equals(EncryptUtil.md5(password)))
			{
				tip=3;//密码错误
			}else
			{
				Timestamp loginTime =new Timestamp(System.currentTimeMillis());
				try{
					
					adminor.setLastLoginTime(loginTime);
					
					adminorService.update(adminor);
				}catch(Exception e)
				{
					e.printStackTrace(System.out);
				}
			}
		}
		if(tip!=0)
		{
			return LOGIN;
		}
		
		HttpSession session =this.getRequest().getSession();
		session.setAttribute("userId", adminor.getId());
		session.setAttribute("userName", adminor.getName());
		session.setAttribute("userNickname", adminor.getNickname());
		
		AdminorHolder.setCurrentUserId(adminor.getId());
		
		return INDEX;
	}
	/**
	 * 登出
	 */
	public String logout() throws Exception
	{
		System.out.println("logout");
		
		HttpSession session =this.getRequest().getSession();
		
		session.invalidate();
		
		response(true);
		
		return null;
	}
	
	@Override
	public String list() throws Exception {

		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}
    
}