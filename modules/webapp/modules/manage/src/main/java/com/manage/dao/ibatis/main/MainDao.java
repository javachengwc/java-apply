package com.manage.dao.ibatis.main;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.ibatis.sqlmap.client.SqlMapClient;

public abstract class MainDao<E,PK extends Serializable>  extends BaseIbatisDao<E, PK>
{
    

	@Resource(name="sqlMapClient1")
	private SqlMapClient sqlMapClient;
	
    protected Log log = LogFactory.getLog(getClass());
		
    public abstract Class<E> getEntityClass();
    
    @PostConstruct
    public void initSqlMapClient()
    {
    	super.setSqlMapClient(sqlMapClient);
    }
    
  
}