package com.manage.dao.ibatis.rbac;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manage.dao.ibatis.BaseIbatisDao;
import com.ibatis.sqlmap.client.SqlMapClient;


public abstract class RbacDao<E,PK extends Serializable>  extends BaseIbatisDao<E, PK>
{

    protected  Log log = LogFactory.getLog(getClass());

	@Resource(name="sqlMapClient2")
	private SqlMapClient sqlMapClient;

    public abstract Class<E> getEntityClass();
    
    @PostConstruct
    public void initSqlMapClient()
    {
    	super.setSqlMapClient(sqlMapClient);
    }
    
  
}
