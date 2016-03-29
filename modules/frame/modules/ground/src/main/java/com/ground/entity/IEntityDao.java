package com.ground.entity;

import com.ground.condition.DBCondition;
import com.ground.core.IDao;
import com.ground.core.callback.IPreparedStatementSetter;
import com.util.page.Page;
import com.util.page.PageQuery;

import java.util.List;


public interface IEntityDao extends IDao
{
	public <T> T getByPk(Class<T> entityClass, String key, T entity) throws Exception;

	public <T> T getByCondition(Class<T> entityClass, String key, T entity) throws Exception;

	public <T> List<T> getAll(Class<T> entityClass, String key) throws Exception;
	
	public <T> T updateByPk(Class<T> entityClass, String key, T entity) throws Exception;
	
	public <T> T updateByCondition(Class<T> entityClass, String key, T entity) throws Exception;
	
	public <T> int updateByCondition(Class<T> entityClass, String key, DBCondition condition) throws Exception;
	
	public <T> T insert(Class<T> entityClass, String key, T entity) throws Exception;
	
	public <T> int deleteByPk(Class<T> entityClass, String key, T entity) throws Exception;
	
	public <T> int deleteByCondition(Class<T> entityClass, String key, T entity) throws Exception;
	
	public <T> List<T> list(Class<T> entityClass, String key, String sql, IPreparedStatementSetter setter) throws Exception;

	public <T> List<T> list(Class<T> entityClass, String key, T entity) throws Exception;
	
	public <T> List<T> queryByCondition(Class<T> entityClass, String key, DBCondition condition) throws Exception;

	public <T> Page<T> page(Class<T> entityClass, PageQuery<T> page, String key) throws Exception;
	
	public <T> int count(Class<T> entityClass, String key, T entity) throws Exception;
	
	public <T> int countGloble(Class<T> entityClass, T entity) throws Exception;
	
	public <T> List<T> listAllDb(Class<T> entityClass, T entity) throws Exception;
	
	public <T> List<T> queryByConditionAllDb(Class<T> entityClass, DBCondition condition) throws Exception;
}
