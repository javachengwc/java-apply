package com.manage.web.rbac;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.util.web.HttpRenderUtil;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.manage.model.rbac.Module;
import com.manage.model.rbac.Resource;
import com.manage.model.rbac.query.ModuleQuery;
import com.manage.service.rbac.ModuleService;
import com.manage.service.rbac.ResourceService;
import com.manage.web.CrudActionSupport;
import com.manage.web.CurrentRequestContext;

@Namespace("/rbac")
@Results( { @Result(name = ModuleAction.RESULT, location = "module.jsp", type = "dispatcher"),
	@Result(name = ModuleAction.TREE, location = "module-tree.jsp", type = "dispatcher"),
	@Result(name = ModuleAction.HIERARCHYPAGE, location = "module-hierarchy-page.jsp", type = "dispatcher")})
public class ModuleAction  extends CrudActionSupport<Module>{

	private static final long serialVersionUID = 1867532423L;
	
	// 默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null;
	
	public static final String RESULT="result";
	public static final String TREE="tree";
	public static final String HIERARCHYPAGE="hierarchyPage";
	
	private Module module;
	
	private Integer id;
	
	private Integer parentid;
	
	private Integer pid;
	
    private String ids;
	
	private int pageSize1=10;
	
	private int refresh=0;
	
	private ModuleQuery moduleQuery;
    
    private PageRequest pageRequestBak =new PageRequest(0,pageSize1);
    
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CurrentRequestContext currentRequestContext;
	
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public ModuleQuery getModuleQuery() {
		return moduleQuery;
	}

	public void setModuleQuery(ModuleQuery moduleQuery) {
		this.moduleQuery = moduleQuery;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getParentid() {
		return parentid;
	}
	
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	
	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public PageRequest getPageRequestBak() {
		return pageRequestBak;
	}

	public void setPageRequestBak(PageRequest pageRequest) {
		this.pageRequestBak = pageRequest;
	}

	public ModuleService getModuleService() {
		return moduleService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public CurrentRequestContext getCurrentRequestContext() {
		return currentRequestContext;
	}

	public void setCurrentRequestContext(CurrentRequestContext currentRequestContext) {
		this.currentRequestContext = currentRequestContext;
	}

	public Module getModel() {
		if(module==null)
			module=new Module();
		return module;
	}
    
	@Override
	protected void prepareModel() throws Exception {
		if(module==null)
			module=new Module();
		
	} 
	@Override
	public String list() throws Exception {

		System.out.println("///moduleAction list() invoked...");
		
		ModuleQuery query = newQuery(ModuleQuery.class, DEFAULT_SORT_COLUMNS);
		
		Page page = moduleService.findPage(query);
		
		savePage(page, query);
	    
		return SUCCESS;
	}
	/**
	 * 分层分页查询
	 */
	public String hierarchyPage() throws Exception
	{
        ModuleQuery query = newQuery(ModuleQuery.class, DEFAULT_SORT_COLUMNS);
        
        if(refresh==1)
        {
        	query.setPageSize(pageRequestBak.getPageSize());
        	query.setPageNumber(pageRequestBak.getPageNumber());
        }
		Page page = moduleService.findHierarchyPage(query);
		
		savePage(page, query);
	    
		return HIERARCHYPAGE;
	}

	@Override
	public String input() throws Exception {
		if(id!=null )
			module =moduleService.getById(id);
		return INPUT;
	}

	@Override
	public String save() throws Exception {
		
		boolean result =false;
		
		try{
			if(id!=null)
				moduleService.update(module);
			else
			{
				if(pid!=null && pid>0)
				    module.setParentid(pid);
				moduleService.save(module);
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
		//tip 0--操作失败  1--操作成功 2--有子模块 3--有挂资源
        int tip =0;
		
		try{
            boolean needDeal=true;
			  
            System.out.println("ids:"+ids);
            List<Integer> list =new ArrayList<Integer>();

            if(StringUtils.isNotBlank(ids))
    		{
    			for(String perStr:ids.split(","))
    			{
    				list.add(Integer.parseInt(StringUtils.trim(perStr)));
    			}
    		}
    		//检查
            for(Integer perId:list)
            {
            	tip=checkModule(perId);
            	if(tip!=1)
            	{
            		needDeal=false;
            		break;
            	}
            }
            //处理
            if(needDeal)
            {
            	for(Integer perId:list)
                {
            		 moduleService.removeById(perId);
                }
			    tip=1;
            }
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
			
		}
		response(tip);
		
		return null;
	}
    
	private int checkModule(Integer moduleId)
	{
		int tip=1;
		
		List<Module> mList=moduleService.getChildsByParentId(moduleId);
        //有子模块
        if(mList!=null && mList.size()>0)
        {
        	tip=2;
        }
        
        List<Resource> rList=resourceService.getResourcesByMd(moduleId);
        
        //有挂资源
        if(rList!=null && rList.size()>0)
        {
        	tip=3;
        }
        return tip;
	}
	
	
	public void info() throws Exception
	{
		module =moduleService.getById(id);
        String jsonStr = JSON.toJSONString(module);
        HttpRenderUtil.renderJSON(jsonStr, this.getResponse());
		//Struts2Utils.renderJson(module);

	}
	
	public String tree() throws Exception
	{
		return TREE;
	}
	/**
	 * 模块树 （模块只有两层）
	 * @throws Exception
	 */
	public void child() throws Exception
	{
		JSONArray array=new JSONArray();
		if(id==null)
		{
			JSONObject obj=new JSONObject();
			obj.put("id", 0);
			obj.put("name","模块");
			obj.put("isParent",true);
			array.add(obj);
		}else
		{
			List<Module> list = moduleService.getChildsByParentId(id);
		    for(Module m:list)
		    {
		    	JSONObject obj=new JSONObject();
				obj.put("id", m.getId());
				obj.put("pId", id);
				obj.put("name",m.getName());
				//第一层
				if(id==0)
				    obj.put("isParent",true);
				else
				{
					//第二层
					obj.put("isParent", false);
				}
				array.add(obj);
		    }
		}
		response(array.toString());
	}
}
