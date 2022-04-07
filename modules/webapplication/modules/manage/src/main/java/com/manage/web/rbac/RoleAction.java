package com.manage.web.rbac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.manage.model.dto.ResourcePermissionDto;
import com.manage.model.rbac.Role;
import com.manage.model.rbac.query.RoleQuery;
import com.manage.service.rbac.ResourcePermissionService;
import com.manage.service.rbac.ResourceService;
import com.manage.service.rbac.RoleService;
import com.manage.web.CrudActionSupport;

@Namespace("/rbac")
@Results( { @Result(name = RoleAction.RESULT, location = "role.jsp", type = "dispatcher"),
@Result(name = RoleAction.AUTHINPUT, location = "role-auth.jsp", type = "dispatcher")})
public class RoleAction extends CrudActionSupport<Role>{

	private static final long serialVersionUID = 186753213L;
	
	// 默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null;
	
	public static final String RESULT="result";
	public static final String AUTHINPUT="authinput";

	public Integer id;
	
	public Role role;
	
	private String ids;
	
	//角色对应的权限字串 如:1_1, 2_2, resourceid_value
	private String auths;
		
	private int pageSize1=10;
	
	private int refresh=0;
	
	private PageRequest pageRequestBak =new PageRequest(0,pageSize1);
	
	private List<ResourcePermissionDto> rpList =new ArrayList<ResourcePermissionDto>();
	
	@Autowired
	public RoleService roleService;
	
	@Autowired
	public ResourceService resourceService;
	
	@Autowired
	public ResourcePermissionService resourcePermissionService;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getAuths() {
		return auths;
	}

	public void setAuths(String auths) {
		this.auths = auths;
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

	public List<ResourcePermissionDto> getRpList() {
		return rpList;
	}

	public void setRpList(List<ResourcePermissionDto> rpList) {
		this.rpList = rpList;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
    
	public ResourcePermissionService getResourcePermissionService() {
		return resourcePermissionService;
	}

	public void setResourcePermissionService(
			ResourcePermissionService resourcePermissionService) {
		this.resourcePermissionService = resourcePermissionService;
	}

	public Role getModel() {
		if(role==null)
			role=new Role();
		return role;
		
	}
    
	@Override
	protected void prepareModel() throws Exception {
	     if(role==null)
	    	 role =new Role();
	}
	
	@Override
	public String list() throws Exception {

        System.out.println("///roleAction list() invoked...");
		
		RoleQuery query = newQuery(RoleQuery.class, DEFAULT_SORT_COLUMNS);
		
		if(refresh==1)
        {
        	query.setPageSize(pageRequestBak.getPageSize());
        	query.setPageNumber(pageRequestBak.getPageNumber());
        }
		
		Page page = roleService.findPage(query);
		
		savePage(page, query);
	    
		return SUCCESS;
	}

	@Override
	public String input() throws Exception {
		if(id!=null )
			role =roleService.getById(id);
		return INPUT;
	}
	
	@Override
	public String save() throws Exception {
        boolean result =false;
		try{
			if(id!=null)
				roleService.update(role);
			else
			{
				roleService.save(role);
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
        		roleService.removeRole(perId);
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
		role =roleService.getById(id);
        String jsonStr = JSON.toJSONString(role);
        HttpRenderUtil.renderJSON(jsonStr, this.getResponse());
		//Struts2Utils.renderJson(role);
	}
	
	
	public String authInput() throws Exception
	{
		rpList =resourceService.findRpFullByRole(id);
		System.out.println("authInput invoked....");
		System.out.println(rpList.size());
		return AUTHINPUT;
	}
	
	public void auth() throws Exception
	{
		System.out.println("roleId:"+id);
		System.out.println("auths:"+auths);
		//1_1, 2_2
		boolean result =false;
		try{
			Map<Integer,Long> rpMap =new HashMap<Integer,Long>();
			if(StringUtils.isNotBlank(auths))
			{
				String args[]= auths.split(",");
				for(String rpStr:args)
				{
					String mapStr[]=rpStr.split("_");
					Integer key=Integer.parseInt(StringUtils.trim(mapStr[0]));
					Long value=Long.parseLong(StringUtils.trim(mapStr[1]));
					Long oldValue =rpMap.get(key);
					if(oldValue!=null)
						value=value+oldValue.longValue();
					rpMap.put(key,value);
				}
				
			}
			resourcePermissionService.authByRole(id, rpMap);
			result=true;
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
			
		}
		response(result);
	}
}
