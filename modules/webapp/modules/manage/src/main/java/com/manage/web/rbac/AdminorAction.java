package com.manage.web.rbac;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.manage.model.dto.RoleDto;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.query.AdminorQuery;
import com.manage.service.rbac.AdminorService;
import com.manage.util.EncryptUtil;
import com.manage.web.CrudActionSupport;

@Namespace("/rbac")
@Results( { @Result(name = AdminorAction.RESULT, location = "adminor.jsp", type = "dispatcher"),
	@Result(name = AdminorAction.ASSIGNINPUT, location = "adminor-assign.jsp", type = "dispatcher")})
public class AdminorAction extends CrudActionSupport<Adminor>{

	private static final long serialVersionUID = 186753213L;
	
	// 默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null;
	
	public static final String RESULT="result";
    public static final String ASSIGNINPUT="assigninput";
	
	public Integer id;
	
	public Adminor adminor;
	
	public List<RoleDto> seleRoles;
	
	public String roleSelected;
	
	private String ids;
	
	private String oldPassword;
	
	private String newPassword;
	
	private int pageSize1=10;
	
	private int refresh=0;
	
	private PageRequest pageRequestBak =new PageRequest(0,pageSize1);
	
	@Autowired
	public AdminorService adminorService;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<RoleDto> getSeleRoles() {
		return seleRoles;
	}

	public void setSeleRoles(List<RoleDto> seleRoles) {
		this.seleRoles = seleRoles;
	}

	public String getRoleSelected() {
		return roleSelected;
	}

	public void setRoleSelected(String roleSelected) {
		this.roleSelected = roleSelected;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	public PageRequest getPageRequestBak() {
		return pageRequestBak;
	}

	public void setPageRequestBak(PageRequest pageRequestBak) {
		this.pageRequestBak = pageRequestBak;
	}

	public Adminor getAdminor() {
		return adminor;
	}

	public void setAdminor(Adminor adminor) {
		this.adminor = adminor;
	}

	public AdminorService getAdminorService() {
		return adminorService;
	}

	public void setAdminorService(AdminorService adminorService) {
		this.adminorService = adminorService;
	}

	public Adminor getModel() {
		if(adminor==null)
			adminor=new Adminor();
		return adminor;
		
	}
    
	@Override
	protected void prepareModel() throws Exception {
	     if(adminor==null)
	    	 adminor =new Adminor();
	}
	
	@Override
	public String list() throws Exception {

        System.out.println("///adminorAction list() invoked...");
		
		AdminorQuery query = newQuery(AdminorQuery.class, DEFAULT_SORT_COLUMNS);
		
		if(refresh==1)
        {
        	query.setPageSize(pageRequestBak.getPageSize());
        	query.setPageNumber(pageRequestBak.getPageNumber());
        }
		
		Page page = adminorService.findPage(query);
		
		savePage(page, query);
	    
		return SUCCESS;
	}

	@Override
	public String input() throws Exception {
		if(id!=null )
			adminor =adminorService.getById(id);
		return INPUT;
	}

	@Override
	public String save() throws Exception {
        boolean result =false;
		try{
			if(id!=null)
				adminorService.update(adminor);
			else
			{
				Timestamp now =new Timestamp(System.currentTimeMillis());
				adminor.setCreateTime(now);
				String initPassword =EncryptUtil.md5("111111");
				adminor.setPassword(initPassword);
				adminorService.save(adminor);
			}
			result=true;
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
			
		}
		response(result);
		return null;
	}

	@Override
	public String delete() throws Exception {
		//tip 0--操作失败  1--操作成功 
        int tip =0;
		
		try{
              
            System.out.println("ids:"+ids);
            List<Integer> list =new ArrayList<Integer>();

            if(StringUtils.isNotBlank(ids))
    		{
    			for(String perStr:ids.split(","))
    			{
    				list.add(Integer.parseInt(StringUtils.trim(perStr)));
    			}
    		}
            
        	for(Integer perId:list)
            {
        		adminorService.removeAdminor(perId);
            }
		    tip=1;
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
			
		}
		response(tip);
		
		return null;
	}
	
	public void info() throws Exception
	{
		adminor =adminorService.getById(id);
		//Struts2Utils.renderJson(adminor);
        String jsonStr = JSON.toJSONString(adminor);
        HttpRenderUtil.renderJSON(jsonStr, this.getResponse());
	}
	
	
	public void changePwd() throws Exception
	{   
		//0:失败  1：成功 2：密码错误
		int result=0;
		if(StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword) && id!=null )
		{
			adminor =adminorService.getById(id);
			if(adminor!=null)
			{
				if(adminor.getPassword().equals(EncryptUtil.md5(oldPassword)))
				{
					adminor.setPassword(EncryptUtil.md5(newPassword));
					adminorService.update(adminor);
					result=1;//修改成功
				}else
				{
					result=2;//密码错误
				}
			}
		}
		response(result);
		
	}
	
	public String assignInput() throws Exception
	{
		adminor =adminorService.getById(id);
		
		seleRoles=adminorService.getRelaRoles(id);
		
		return ASSIGNINPUT;
		
	}
	public void assign() throws Exception
	{
		boolean result=false;
		
		adminor =adminorService.getById(id);
		
		List<Integer> roleIds =new ArrayList<Integer>();
		
		if(StringUtils.isNotBlank(roleSelected))
		{
			for(String perStr:roleSelected.split(","))
			{
				roleIds.add(Integer.parseInt(StringUtils.trim(perStr)));
			}
		}
		result=adminorService.assign(adminor,roleIds);
	    
		try{
	    	response(result);
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
		
	}
	
}
