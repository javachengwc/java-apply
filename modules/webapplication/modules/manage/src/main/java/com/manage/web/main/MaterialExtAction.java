package com.manage.web.main;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import cn.org.rapid_framework.page.Page;

import com.manage.model.main.MaterialExt;
import com.manage.model.main.query.MaterialExtQuery;
import com.manage.service.main.MaterialExtService;
import com.manage.web.CrudActionSupport;

@Namespace("/main")
@Results( { @Result(name = MaterialExtAction.RESULT, location = "materialext.jsp", type = "dispatcher"),
	@Result(name = MaterialExtAction.PRESENTINPUT, location = "material-present.jsp", type = "dispatcher")})
public class MaterialExtAction  extends CrudActionSupport<MaterialExt>{

	private static final long serialVersionUID = 1822289789213L;
	
	// 默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null;
	
	public static final String RESULT="result";
	public static final String PRESENTINPUT="presentinput";

	private int id;
	
	public String combinid;
	
	public String name;
	
	public MaterialExt material;
	
	private String playerId;
	
	private int count;
	
	@Autowired
	private MaterialExtService materialExtService;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCombinid() {
		return combinid;
	}

	public void setCombinid(String combinid) {
		this.combinid = combinid;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public MaterialExt getMaterial() {
		return material;
	}

	public void setMaterial(MaterialExt materialExt) {
		this.material = materialExt;
	}

	public MaterialExtService getMaterialExtService() {
		return materialExtService;
	}

	public void setMaterialExtService(MaterialExtService materialExtService) {
		this.materialExtService = materialExtService;
	}

	@Override
	public MaterialExt getModel() {
		if(material==null)
			material =new MaterialExt();
		return material;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(material==null)
			material=new MaterialExt();
	}


	@Override
	public String list() throws Exception {
		
        System.out.println("///MaterialExtAction list() invoked...");
		
        MaterialExtQuery query = newQuery(MaterialExtQuery.class, DEFAULT_SORT_COLUMNS);
		
		Page page = materialExtService.findPage(query);
		
		savePage(page, query);
	    
		return RESULT;
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
	/**
	 * 物资赠送界面
	 */
	public String presentInput() throws Exception
	{
		return PRESENTINPUT;
		
	}
	/**
	 * 物资赠送
	 */
	public void present() throws Exception {
	
		 boolean result =false;
		
		 System.out.println("playerId============="+playerId);
	    if(StringUtils.isNotBlank(combinid) && StringUtils.isNotBlank(playerId))
	    {
	    	result= materialExtService.presentMaterial(playerId, combinid, count);
	    }
	    try{
	    	response(result);
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}