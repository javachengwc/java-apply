package com.manage.dao.ibatis;

import java.util.List;

import com.manage.model.BaseQuery;
import com.ibatis.sqlmap.client.SqlMapClient;

import cn.org.rapid_framework.page.Page;

public class IbatisPage<T> extends Page<T> {

	private static final long serialVersionUID = 1697574208611244526L;

	public IbatisPage(int pageNumber, int pageSize, int totalCount) {
		super(pageNumber, pageSize, totalCount);
	}

	@SuppressWarnings("unchecked")
	public IbatisPage(SqlMapClient sqlMapClient,String queryCount,String queryPage,BaseQuery query) 
	{
		super(query.getPageNumber(),query.getPageSize(),0);
        this.pageNumber=query.getPageNumber();
		
		int offset = (this.pageNumber - 1) * this.getPageSize();
		if (offset < 0) {
			offset = 0;
		}
		query.setStart(offset);
		
		//总记录数
		int size = 0;
		try{
	        size=(Integer)sqlMapClient.queryForObject(queryCount, query);
	        System.out.println(",,,,,,,,size:"+size);
	        this.totalCount=size;
	        List<T> list=sqlMapClient.queryForList(queryPage, query);
	        if(list==null)
	        {
	        	 System.out.println(",,,,,,,,list.size is null");
	        }
	        System.out.println(",,,,,,,,list.size:"+list.size());
	        this.setResult(list);
	        
		}catch(Exception e)
		{
			System.out.println("-----ibatisPage excetion-->query:"+query.toString()+"\n      queryPage:"+queryPage+"\n     queryCount:"+queryCount+"\n");
			e.printStackTrace(System.out);
		}
	}
}
