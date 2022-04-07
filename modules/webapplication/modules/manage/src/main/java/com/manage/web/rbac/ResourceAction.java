package com.manage.web.rbac;

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

import com.manage.model.rbac.Resource;
import com.manage.model.rbac.query.ResourceQuery;
import com.manage.service.rbac.ResourceService;
import com.manage.web.CrudActionSupport;
@Namespace("/rbac")
@Results( { @Result(name = ResourceAction.RESULT, location = "resource.jsp", type = "dispatcher"),
	@Result(name = ResourceAction.MAIN, location = "resource-main.jsp", type = "dispatcher"),
	@Result(name = ResourceAction.HIERARCHYPAGE, location = "resource-hierarchy-page.jsp", type = "dispatcher")})
public class ResourceAction extends CrudActionSupport<Resource>{

	private static final long serialVersionUID = 186753213L;
	
	// 默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null;
	
	public static final String RESULT="result";
	public static final String MAIN="main";
	public static final String HIERARCHYPAGE="hierarchyPage";

	public Integer id;
	
	public Resource resource;
	
	public Integer moduleid;
	
	public Integer mid;
	
    private String ids;
	
	private int pageSize1=10;
	
	private int refresh=0;
	
	private PageRequest pageRequestBak =new PageRequest(0,pageSize1);
	
	@Autowired
	public ResourceService resourceService;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Integer getModuleid() {
		return moduleid;
	}

	public void setModuleid(Integer moduleid) {
		this.moduleid = moduleid;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
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

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public Resource getModel() {
		if(resource==null)
			resource=new Resource();
		return resource;
		
	}
    
	@Override
	protected void prepareModel() throws Exception {
	     if(resource==null)
	    	 resource =new Resource();
	}
	
	@Override
	public String list() throws Exception {

        System.out.println("///resourceAction list() invoked...");
		
		ResourceQuery query = newQuery(ResourceQuery.class, DEFAULT_SORT_COLUMNS);
		
		Page page = resourceService.findPage(query);
		
		savePage(page, query);
	    
		return SUCCESS;
	}
	
	/**
	 * 分层分页查询
	 */
	public String hierarchyPage() throws Exception
	{
        ResourceQuery query = newQuery(ResourceQuery.class, DEFAULT_SORT_COLUMNS);
        
        if(refresh==1)
        {
        	query.setPageSize(pageRequestBak.getPageSize());
        	query.setPageNumber(pageRequestBak.getPageNumber());
        }
		Page page = resourceService.findHierarchyPage(query);
		
		savePage(page, query);
	    
		return HIERARCHYPAGE;
	}
	
	public String main() throws Exception
	{
		return MAIN;
	}

	@Override
	public String input() throws Exception {
		if(id!=null )
			resource =resourceService.getById(id);
		return INPUT;
	}

	@Override
	public String save() throws Exception {
        boolean result =false;
		try{
			if(id!=null)
				resourceService.update(resource);
			else
			{
				if(mid!=null && mid>0)
				    resource.setModuleid(mid);
				resourceService.save(resource);
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
        		resourceService.removeResource(perId);
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
		resource =resourceService.getById(id);
        String jsonStr = JSON.toJSONString(resource);
        HttpRenderUtil.renderJSON(jsonStr,this.getResponse());
		//Struts2Utils.renderJson(resource);
	}
	
}
