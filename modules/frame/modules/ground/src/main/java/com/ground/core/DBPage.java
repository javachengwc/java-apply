package com.ground.core;

import java.util.List;

import com.ground.core.callback.IPreparedStatementCreator;
import com.ground.core.callback.IRowMapper;
import com.util.page.Page;
import org.apache.commons.lang3.StringUtils;

public class DBPage<T> extends Page<T> {

	private static final long serialVersionUID = 1697574208611244526L;

	private IRowMapper<T> mapper;
	
	public DBPage(int pageNumber, int pageSize, int totalCount) {
		super(pageNumber, pageSize, totalCount);
	}

	public IRowMapper<T> getMapper()
	{
		return mapper;
	}

	public void setMapper(IRowMapper<T> mapper)
	{
		this.mapper = mapper;
	}

	public DBPage(GenericDao dao,String key,IPreparedStatementCreator creator,IRowMapper<T> mapper)
	{
		this(dao,key,creator.genericSql(),mapper);
		
	}
	
	public DBPage(IDao dao,String key,String sql,IRowMapper<T> mapper)
	{
		String queryPage=null;
		String queryCount=null;
		try{
			queryPage=sql;
			int limitIndex=queryPage.lastIndexOf("limit");
			if(limitIndex<0)
				limitIndex=queryPage.lastIndexOf("LIMIT");
			if(limitIndex<0)
				throw new IllegalArgumentException("查询sql语句无分页limit关键字!");
			queryCount =" select count(1) from ("+queryPage.substring(0,limitIndex)+") combinsource ";
			String afterlimit =queryPage.substring(limitIndex+"limit".length(), queryPage.length());
			String pageRela [] =afterlimit.split(",");
			int pageSize=Integer.parseInt(StringUtils.trim(pageRela[1]));
			this.setPageSize(pageSize);
			int pre=Integer.parseInt(StringUtils.trim(pageRela[0]));
			int currentPageNo=pre/pageSize +1;
			this.setPageNo(currentPageNo);
			this.setMapper(mapper);
			int size=(Integer)dao.count(key, queryCount);
	        this.setTotalCount(size);
	        List<T> list=dao.queryCollection(key, queryPage, mapper);
	        if(list==null)
	        {
	        	 System.out.println(",,,,,,,,list.size is null");
	        }
	        this.setResult(list);
			
		}catch(Exception e)
		{
			System.out.println("-----jdbcPage excetion-->\n      queryPage:"+queryPage+"\n     queryCount:"+queryCount+"\n");
	        e.printStackTrace(System.out);
		}
		
	}
	
	public DBPage(GenericDao dao,IPreparedStatementCreator creator,IRowMapper<T> mapper)
	{
		this(dao,creator.genericSql(),mapper);
		
	}
	
	public DBPage(IDao dao,String sql,IRowMapper<T> mapper) 
	{
		String queryPage=null;
		String queryCount=null;
		try{
			queryPage=sql;
			int limitIndex=queryPage.lastIndexOf("limit");
			if(limitIndex<0)
				limitIndex=queryPage.lastIndexOf("LIMIT");
			if(limitIndex<0)
				throw new IllegalArgumentException("查询sql语句无分页limit关键字!");
			queryCount =" select count(1) from ("+queryPage.substring(0,limitIndex)+") combinsource ";
			String afterlimit =queryPage.substring(limitIndex+"limit".length(), queryPage.length());
			String pageRela [] =afterlimit.split(",");
			int pageSize=Integer.parseInt(StringUtils.trim(pageRela[1]));
			this.setPageSize(pageSize);
			int pre=Integer.parseInt(StringUtils.trim(pageRela[0]));
			int currentPageNo=pre/pageSize +1;
			this.setPageNo(currentPageNo);
			this.setMapper(mapper);
			int size=(Integer)dao.countGloble(queryCount,null);
	        this.setTotalCount(size);
	        List<T> list=dao.queryGlobleCollection(queryPage,null, mapper);
	        if(list==null)
	        {
	        	 System.out.println(",,,,,,,,list.size is null");
	        }
	        this.setResult(list);
			
		}catch(Exception e)
		{
			System.out.println("-----jdbcPage excetion-->\n      queryPage:"+queryPage+"\n     queryCount:"+queryCount+"\n");
	        e.printStackTrace(System.out);
		}
	}
}
