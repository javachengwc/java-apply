package com.ground.core;

import com.ground.core.callback.*;
import com.ground.core.datasource.DBEnv;
import com.util.page.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 一般db访问实现dao
 *
 */
public class GenericDao implements IDao
{
	private DBEnv dataSource;
	
	public GenericDao()
	{
		
	}
	
	public GenericDao(DBEnv dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public DBEnv getDataSource()
	{
		return dataSource;
	}
	
	public void setDataSource(DBEnv dataSource)
	{
		this.dataSource = dataSource;
	}
	/**
	 * 查询 单数据库查询记录
	 * 
	 * @param key 
	 * @param creator 查询创建器
	 * @param setter 查询参数设置器
	 * @param resultSetExtractor 查询结果包装器
	 * @return T
	 * @throws Exception
	 */
	public <T> T query(String key, IPreparedStatementCreator creator,
		IPreparedStatementSetter setter,
		IResultSetExtractor<T> resultSetExtractor)
		throws Exception
	{
		return GenericDBHelper.query(getDataSource(), key, creator, setter,
			resultSetExtractor);
	}
	/**
	 * 查询 单数据库查询记录 不需要参数的设置
	 * 
	 * @param key 
	 * @param creator 查询创建器
	 * @param resultSetExtractor 查询结果包装器
	 * @return T
	 * @throws Exception
	 */
	public <T> T query(String key, IPreparedStatementCreator creator,
		IResultSetExtractor<T> resultSetExtractor)
		throws Exception
	{
		return GenericDBHelper.query(getDataSource(), key, creator,
			resultSetExtractor);
	}
	/**
	 * 查询 单数据库查询记录 原生sql语句的查询
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @param resultSetExtractor 查询结果包装器
	 * @return T
	 * @throws Exception
	 */
	public <T> T query(String key, String sql,
		IResultSetExtractor<T> resultSetExtractor)
		throws Exception
	{
		return GenericDBHelper.query(getDataSource(), key, sql, null,
			resultSetExtractor);
	}
	/**
	 * 查询 单数据库查询记录 原生sql语句并需要设参的查询
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @param setter 查询参数设置器
	 * @param resultSetExtractor 查询结果包装器
	 * @return T
	 * @throws Exception
	 */
	public <T> T query(String key, String sql, IPreparedStatementSetter setter,
		IResultSetExtractor<T> resultSetExtractor)
		throws Exception
	{
		return GenericDBHelper.query(getDataSource(), key, sql, setter,
			resultSetExtractor);
	}
	
	/**
	 * 列表查询 单数据库列表查询
	 * 
	 * @param key 
	 * @param creator 查询创建器
	 * @param setter 查询参数设置器
	 * @param rowMapper 查询结果包装器
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> queryCollection(String key,
		IPreparedStatementCreator creator, IPreparedStatementSetter setter,
		IRowMapper<T> rowMapper)
		throws Exception
	{
		return GenericDBHelper.queryCollection(getDataSource(), key, creator,
			setter, rowMapper);
	}
	
	/**
	 * 列表查询 单数据库列表查询 不需要参数的设置
	 * 
	 * @param key 
	 * @param creator 查询创建器
	 * @param rowMapper 查询结果包装器
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> queryCollection(String key,
		IPreparedStatementCreator creator, IRowMapper<T> rowMapper)
		throws Exception
	{
		return GenericDBHelper.queryCollection(getDataSource(), key, creator,
			rowMapper);
	}
	
	/**
	 * 列表查询 单数据库列表查询 原生sql语句的查询
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @param rowMapper 查询结果包装器
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> queryCollection(String key, String sql,
		IRowMapper<T> rowMapper)
		throws Exception
	{
		return GenericDBHelper.queryCollection(getDataSource(), key, sql, null,
			rowMapper);
	}
	
	/**
	 * 列表查询 单数据库列表查询 原生sql语句并需要设参的查询
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @param setter 查询参数设置器
	 * @param rowMapper 查询结果包装器
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> queryCollection(String key, String sql,
		IPreparedStatementSetter setter, IRowMapper<T> rowMapper)
		throws Exception
	{
		return GenericDBHelper.queryCollection(getDataSource(), key, sql, setter,
			rowMapper);
	}
	/**
	 * 列表查询 全部数据库列表查询 原生sql语句并需要设参的查询
	 * 
	 * @param sql sql语句
	 * @param setter 查询参数设置器
	 * @param rowMapper 查询结果包装器
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> queryGlobleCollection(String sql,
		IPreparedStatementSetter setter, IRowMapper<T> rowMapper)
		throws Exception
	{
		return GenericMutilDBHelper.queryCollection(getDataSource(), sql, setter,
			rowMapper);
	}
	
	/**
	 * db操作，单数据库访问万能方法
	 * 
	 * @param key
	 * @param callback 访问回调器
	 * @return T
	 * @throws Exception
	 */
	public <T> T execute(String key, IJdbcCallback<T> callback)
		throws Exception
	{
		return GenericDBHelper.execute(getDataSource(), key, callback);
	}
	
	/**
	 * 新增 单数据库新增记录
	 * 
	 * @param key 
	 * @param creator db访问创建器
	 * @param setter 参数设置器
	 * @return int 新增记录数
	 * @throws Exception
	 */
	public int insert(String key, IPreparedStatementCreator creator,
		IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.insert(getDataSource(), key, creator, setter);
	}
	/**
	 * 新增 单数据库新增记录 不需要参数的设置
	 * 
	 * @param key 
	 * @param creator db访问创建器
	 * @return int 新增记录数
	 * @throws Exception
	 */
	public int insert(String key, IPreparedStatementCreator creator)
		throws Exception
	{
		return GenericDBHelper.insert(getDataSource(), key, creator);
	}
	/**
	 * 新增 单数据库新增记录 原生sql语句
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @return int 新增记录数
	 * @throws Exception
	 */
	public int insert(String key, String sql)
		throws Exception
	{
		return GenericDBHelper.insert(getDataSource(), key, sql);
	}
	/**
	 * 新增 单数据库新增记录 原生sql语句并需要设参
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @param setter 参数设置器
	 * @return int 新增记录数
	 * @throws Exception
	 */
	public int insert(String key, String sql, IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.insert(getDataSource(), key, sql, setter);
	}
	
	/**
	 * 新增 单数据库新增记录
	 * 
	 * @param key 
	 * @param creator db访问创建器
	 * @param setter 参数设置器
	 * @return int 新增记录生成的主键
	 * @throws Exception
	 */
	public int insertReturnId(String key, IPreparedStatementCreator creator,
		IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.insertReturnId(getDataSource(), key, creator, setter);
	}
	/**
	 * 新增 单数据库新增记录 原生sql语句并需要设参
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @param setter 参数设置器
	 * @return Serializable 新增记录生成的主键
	 * @throws Exception
	 */
	public Serializable insertReturnId(String key, String sql,
		IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.insertReturnId(getDataSource(), key, sql, setter);
	}
	/**
	 * 更新 单数据库更新记录
	 * 
	 * @param key 
	 * @param creator db访问创建器
	 * @param setter 参数设置器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int update(String key, IPreparedStatementCreator creator,
		IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.update(getDataSource(), key, creator, setter);
	}
	/**
	 * 更新 单数据库更新记录 不需要参数的设置
	 * 
	 * @param key 
	 * @param creator db访问创建器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int update(String key, IPreparedStatementCreator creator)
		throws Exception
	{
		return GenericDBHelper.update(getDataSource(), key, creator);
	}	
	/**
	 * 更新 单数据库更新记录 原生sql语句
	 * 
	 * @param key 
	 * @param sql sql语句
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int update(String key, String sql)
		throws Exception
	{
		return GenericDBHelper.update(getDataSource(), key, sql);
	}
	/**
	 * 更新 单数据库更新记录 原生sql语句并需要设参
	 * 
	 * @param key
	 * @param sql sql语句 
	 * @param setter 参数设置器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int update(String key, String sql, IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.update(getDataSource(), key, sql, setter);
	}
	
	/**
	 * 更新或新增  单数据库更新或新增记录
	 * 
	 * @param key
	 * @param updateCreator db更新创建器
	 * @param updateSetter 更新参数设置器
	 * @param insertCreator db新增创建器
	 * @param insertSetter 新增参数设置器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int updateOrInsert(String key,
		IPreparedStatementCreator updateCreator,
		IPreparedStatementSetter updateSetter,
		IPreparedStatementCreator insertCreator,
		IPreparedStatementSetter insertSetter)
		throws Exception
	{
		return GenericDBHelper.updateOrInsert(getDataSource(), key, updateCreator,
			updateSetter, insertCreator, insertSetter);
	}
	/**
	 * 更新或新增  单数据库更新或新增记录  不需要设置参数
	 * 
	 * @param key
	 * @param updateCreator db更新创建器
	 * @param insertCreator db新增创建器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int updateOrInsert(String key,
		IPreparedStatementCreator updateCreator,
		IPreparedStatementCreator insertCreator)
		throws Exception
	{
		return GenericDBHelper.updateOrInsert(getDataSource(), key, updateCreator,
			insertCreator);
	}
	/**
	 * 更新或新增  单数据库更新或新增记录  原生sql语句
	 * 
	 * @param key
	 * @param updateSql 更新语句
	 * @param insertSql 新增语句
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int updateOrInsert(String key, String updateSql, String insertSql)
		throws Exception
	{
		return GenericDBHelper.updateOrInsert(getDataSource(), key, updateSql,
			insertSql);
	}
	/**
	 * 更新或新增  单数据库更新或新增记录  原生sql语句且需设参
	 * 
	 * @param key
	 * @param updateSql 更新语句
	 * @param insertSql 新增语句
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int updateOrInsert(String key, String updateSql,
			IPreparedStatementSetter updateSetter, String insertSql,
			IPreparedStatementSetter insertSetter) throws Exception {
		return GenericDBHelper.updateOrInsert(getDataSource(), key, updateSql,
				updateSetter,insertSql,insertSetter);
	}
	/**
	 * 新增或更新  单数据库新增或更新记录 先insert,如果发现有记录则update
	 * 
	 * @param key
	 * @param updateCreator db更新创建器
	 * @param updateSetter 更新参数设置器
	 * @param insertCreator db新增创建器
	 * @param insertSetter 新增参数设置器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int insertOrUpdate(String key,
		IPreparedStatementCreator updateCreator,
		IPreparedStatementSetter updateSetter,
		IPreparedStatementCreator insertCreator,
		IPreparedStatementSetter insertSetter)
		throws Exception
	{
		return GenericDBHelper.insertOrUpdate(getDataSource(), key, updateCreator,
			updateSetter, insertCreator, insertSetter);
	}
	/**
	 * 删除  单数据库更删除记录
	 * 
	 * @param key
	 * @param creator db删除创建器
	 * @param setter 删除参数设置器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int delete(String key, IPreparedStatementCreator creator,
		IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.delete(getDataSource(), key, creator, setter);
	}
	/**
	 * 删除  单数据库更删除记录 不需要设置参数
	 * 
	 * @param key
	 * @param creator db删除创建器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int delete(String key, IPreparedStatementCreator creator)
		throws Exception
	{
		return GenericDBHelper.delete(getDataSource(), key, creator);
	}
	/**
	 * 删除  单数据库更删除记录 原生sql语句
	 * 
	 * @param key
	 * @param sql sql语句
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int delete(String key, String sql)
		throws Exception
	{
		return GenericDBHelper.delete(getDataSource(), key, sql);
	}
	/**
	 * 删除  单数据库更删除记录 原生sql语句并需要设参
	 * 
	 * @param key
	 * @param sql sql语句
	 * @param setter 删除参数设置器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int delete(String key, String sql, IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.delete(getDataSource(), key, sql, setter);
	}
	/**
	 * 删除  全部数据库删除记录 原生sql语句并需要设参
	 * 
	 * @param sql sql语句
	 * @param setter 删除参数设置器
	 * @return int 影响的记录条数
	 * @throws Exception
	 */
	public int deleteGloble(String sql, IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericMutilDBHelper.delete(getDataSource(), sql, setter);
	}
	/**
	 * 计数  单数据库记录条数
	 * @param key
	 * @param creator db计数创建器
	 * @param setter 计数参数设置器
	 * @return int 记录条数
	 * @throws Exception
	 */
	public int count(String key, IPreparedStatementCreator creator,
		IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.count(getDataSource(), key, creator, setter);
	}
	/**
	 * 计数  单数据库记录条数 不需要参数设置
	 * @param key
	 * @param creator db计数创建器
	 * @return int 记录条数
	 * @throws Exception
	 */
	public int count(String key, IPreparedStatementCreator creator)
		throws Exception
	{
		return GenericDBHelper.count(getDataSource(), key, creator);
	}
	/**
	 * 计数  单数据库记录条数 原生sql语句
	 * @param key
	 * @param sql sql语句
	 * @return int 记录条数
	 * @throws Exception
	 */
	public int count(String key, String sql)
		throws Exception
	{
		return GenericDBHelper.count(getDataSource(), key, sql, null);
	}
	/**
	 * 计数  单数据库记录条数 原生sql语句且需设参
	 * @param key
	 * @param sql sql语句
	 * @param setter 计数参数设置器
	 * @return int 记录条数
	 * @throws Exception
	 */
	public int count(String key, String sql, IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.count(getDataSource(), key, sql, setter);
	}
	/**
	 * 计数  全部数据库记录条数 原生sql语句且需设参
	 * @param sql sql语句
	 * @param setter 计数参数设置器
	 * @return int 记录条数
	 * @throws Exception
	 */
	public int countGloble(String sql, IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericMutilDBHelper.count(getDataSource(), sql, setter);
	}
	/**
	 * 求和  单数据库记录字段求和
	 * @param key 
	 * @param creator db求和创建器
	 * @param setter 求和参数设置器
	 * @return long 求和结果
	 * @throws Exception
	 */
	public long sum(String key, IPreparedStatementCreator creator,
		IPreparedStatementSetter setter)
		throws Exception
	{
		return GenericDBHelper.sum(getDataSource(), key, creator, setter);
	}
	/**
	 * 求和  单数据库记录字段求和 不需要参数设置
	 * @param key 
	 * @param creator db求和创建器
	 * @return long 求和结果
	 * @throws Exception
	 */
	public long sum(String key, IPreparedStatementCreator creator)
		throws Exception
	{
		return GenericDBHelper.sum(getDataSource(), key, creator);
	}
	/**
	 * 求和  单数据库记录字段求和 原生sql语句
	 * @param key 
	 * @param sql sql语句
	 * @return long 求和结果
	 * @throws Exception
	 */
	public long sum(String key, String sql)
		throws Exception
	{
		return GenericDBHelper.sum(getDataSource(), key, sql);
	}
	/**
	 * 求和  单数据库记录字段求和 原生sql语句且需设参
	 * @param key 
	 * @param sql sql语句
	 * @param setter 求和参数设置器
	 * @return long 求和结果
	 * @throws Exception
	 */
	public long sum(String key, String sql, IPreparedStatementSetter setter)
			throws Exception {
		return GenericDBHelper.sum(getDataSource(), key, sql,setter);
	}
	
	/**
	 * 分页查询  单数据库分页查询  
	 * @param key 
	 * @param creator 分页查询创建器
	 * @param mapper 结果包装器
	 * @return Page<T> 分页结果
	 * @throws Exception
	 */
	public <T> Page<T> queryPage(String key,IPreparedStatementCreator creator, IRowMapper<T> mapper) {
		return new DBPage<T>(this,key,creator,mapper);
	}
	
	/**
	 * 分页查询  单数据库分页查询  sql原句
	 * @param key 
	 * @param sql sql语句
	 * @param mapper 结果包装器
	 * @return Page<T> 分页结果
	 * @throws Exception
	 */
	public <T> Page<T> queryPage(String key,String sql,IRowMapper<T> mapper) {
		return new DBPage<T>(this,key,sql,mapper);
	}
	/**
	 * 分页查询  全部数据库分页查询
	 * @param creator 分页查询创建器
	 * @param mapper 结果包装器
	 * @return Page<T> 分页结果
	 * @throws Exception
	 */
	public <T> Page<T> queryGloblePage(IPreparedStatementCreator creator,IRowMapper<T> mapper) {
		return new DBPage<T>(this,creator,mapper);
	}
	/**
	 * 分页查询  全部数据库分页查询 sql原句
	 * @param sql sql语句
	 * @param mapper 结果包装器
	 * @return Page<T> 分页结果
	 * @throws Exception
	 */
	public <T> Page<T> queryGloblePage(String sql,IRowMapper<T> mapper) {
		return new DBPage<T>(this,sql,mapper);
	}
	
}
