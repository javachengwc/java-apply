package com.manage.web.main;

import com.util.enh.BeanCopyUtil;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import cn.org.rapid_framework.page.Page;

import com.manage.model.main.UserMaterial;
import com.manage.model.main.query.UserMaterialQuery;
import com.manage.service.main.UserMaterialService;
import com.manage.web.CrudActionSupport;

@Namespace("/main")
@Results( { @Result(name = UserMaterialAction.RESULT, location = "usermaterial.jsp", type = "dispatcher")})
public class UserMaterialAction  extends CrudActionSupport<UserMaterial>{

	private static final long serialVersionUID = 188909229789213L;
	
	// 默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null;
	
	public static final String RESULT="result";

	public String id;
	
	public UserMaterial userMaterial;
	
	public UserMaterialQuery userMaterialQuery;
	/**
	 * 快速查询中的账号名
	 */
	private String fastQueryActStr;

	/**
	 * 是否多条件查询  0：否 1：是
	 */
	private int morequery=0;
	
	@Autowired
	private UserMaterialService userMaterialService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public String getFastQueryActStr() {
		return fastQueryActStr;
	}

	public void setFastQueryActStr(String fastQueryActStr) {
		this.fastQueryActStr = fastQueryActStr;
	}

	public int getMorequery() {
		return morequery;
	}

	public void setMorequery(int morequery) {
		this.morequery = morequery;
	}

	public UserMaterialQuery getUserMaterialQuery() {
		return userMaterialQuery;
	}

	public void setUserMaterialQuery(UserMaterialQuery userMaterialQuery) {
		this.userMaterialQuery = userMaterialQuery;
	}

	public UserMaterial getUserMaterial() {
		return userMaterial;
	}

	public void setUserMaterial(UserMaterial userMaterial) {
		this.userMaterial = userMaterial;
	}

	public UserMaterialService getUserMaterialService() {
		return userMaterialService;
	}

	public void setUserMaterialService(UserMaterialService userMaterialService) {
		this.userMaterialService = userMaterialService;
	}

	@Override
	public UserMaterial getModel() {
		if(userMaterial==null)
			userMaterial =new UserMaterial();
		return userMaterial;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(userMaterial==null)
			userMaterial =new UserMaterial();
	}


	@Override
	public String list() throws Exception {
		
        System.out.println("///UserMaterialAction list() invoked...");
		
        UserMaterialQuery query = newQuery(UserMaterialQuery.class, DEFAULT_SORT_COLUMNS);
        
		if(userMaterialQuery!=null)
		{
			int pageNum=query.getPageNumber();
			int pageSz=query.getPageSize();
			String sortCols=query.getSortColumns();

            BeanCopyUtil.copyProperties(query,userMaterialQuery);
	
			query.setPageNumber(pageNum);
			query.setPageSize(pageSz);
			query.setSortColumns(sortCols);
		}
		if(morequery==0)
		{
			if(userMaterialQuery!=null)
			   query.setOwner(userMaterialQuery.getOwner());
			
		}
		Page page = userMaterialService.findPage(query);
		
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
}