package com.ground.core;

import com.ground.core.callback.*;
import com.util.page.Page;

import java.io.Serializable;
import java.util.List;

/**
 * db访问接口
 *
 */
public interface IDao {
    
	public <T> T query(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter, IResultSetExtractor<T> resultSetExtractor) throws Exception ;
	
	public <T> T query(String key, IPreparedStatementCreator creator, IResultSetExtractor<T> resultSetExtractor) throws Exception ;
	
	public <T> T query(String key, String sql, IPreparedStatementSetter setter, IResultSetExtractor<T> resultSetExtractor) throws Exception ;

	public <T> T query(String key, String sql, IResultSetExtractor<T> resultSetExtractor) throws Exception ;
	
	public <T> List<T> queryCollection(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter, IRowMapper<T> rowMapper) throws Exception ;
	
	public <T> List<T> queryCollection(String key, IPreparedStatementCreator creator, IRowMapper<T> rowMapper) throws Exception ;
	
	public <T> List<T> queryCollection(String key, String sql, IPreparedStatementSetter setter, IRowMapper<T> rowMapper) throws Exception ;
	
	public <T> List<T> queryCollection(String key, String sql, IRowMapper<T> rowMapper) throws Exception ;
	
	public <T> List<T> queryGlobleCollection(String sql, IPreparedStatementSetter setter, IRowMapper<T> rowMapper) throws Exception ;
	
	public <T> Page<T> queryPage(String key, IPreparedStatementCreator creator, IRowMapper<T> mapper) ;
	
	public <T> Page<T> queryPage(String key, String sql, IRowMapper<T> mapper) ;
	
	public <T> Page<T> queryGloblePage(IPreparedStatementCreator creator, IRowMapper<T> mapper) ;
	
	public <T> Page<T> queryGloblePage(String sql, IRowMapper<T> mapper) ;
	
	public <T> T execute(String key, IJdbcCallback<T> callback) throws Exception ;

	public int insert(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter) throws Exception ;

	public int insert(String key, IPreparedStatementCreator creator) throws Exception ;

	public int insert(String key, String sql, IPreparedStatementSetter setter) throws Exception ;
	
	public int insert(String key, String sql) throws Exception ;

	public int insertReturnId(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter) throws Exception ;
	
	public Serializable insertReturnId(String key, String sql, IPreparedStatementSetter setter) throws Exception;

	public int update(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter) throws Exception ;
	
	public int update(String key, IPreparedStatementCreator creator) throws Exception ;

	public int update(String key, String sql, IPreparedStatementSetter setter) throws Exception ;
	
	public int update(String key, String sql) throws Exception ;
	
	public int updateOrInsert(String key, IPreparedStatementCreator updateCreator, IPreparedStatementSetter updateSetter,
                              IPreparedStatementCreator insertCreator, IPreparedStatementSetter insertSetter) throws Exception ;

	public int updateOrInsert(String key, IPreparedStatementCreator updateCreator, IPreparedStatementCreator insertCreator) throws Exception ;

	public int updateOrInsert(String key, String updateSql, IPreparedStatementSetter updateSetter, String insertSql, IPreparedStatementSetter insertSetter) throws Exception ;
	
	public int updateOrInsert(String key, String updateSql, String insertSql) throws Exception ;
	
	public int insertOrUpdate(String key, IPreparedStatementCreator updateCreator, IPreparedStatementSetter updateSetter,
                              IPreparedStatementCreator insertCreator, IPreparedStatementSetter insertSetter) throws Exception ;

	public int delete(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter) throws Exception ;

	public int delete(String key, IPreparedStatementCreator creator) throws Exception ;
	
	public int delete(String key, String sql, IPreparedStatementSetter setter) throws Exception ;
	
	public int delete(String key, String sql) throws Exception ;
	
	public int deleteGloble(String sql, IPreparedStatementSetter setter) throws Exception;

	public int count(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter) throws Exception ;

	public int count(String key, IPreparedStatementCreator creator) throws Exception ;

	public int count(String key, String sql, IPreparedStatementSetter setter) throws Exception ;
	
	public int count(String key, String sql) throws Exception ;

	public int countGloble(String sql, IPreparedStatementSetter setter) throws Exception;
	
	public long sum(String key, IPreparedStatementCreator creator, IPreparedStatementSetter setter) throws Exception ;

	public long sum(String key, IPreparedStatementCreator creator) throws Exception ;

	public long sum(String key, String sql, IPreparedStatementSetter setter) throws Exception ;
	
	public long sum(String key, String sql) throws Exception ;
}
