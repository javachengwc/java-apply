package com.ground.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ground.condition.DBCondition;
import com.ground.core.GenericDao;
import com.ground.core.callback.IPreparedStatementSetter;
import com.ground.core.datasource.DBEnv;
import com.ground.entity.callback.*;
import com.ground.exception.GroundException;
import com.util.page.Page;
import com.util.page.PageQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;

public class EntityDaoImpl extends GenericDao implements IEntityDao
{
	private static final Logger m_logger = Logger.getLogger(EntityDaoImpl.class);
    
	public EntityDaoImpl()
	{
		
	}
	
	public EntityDaoImpl(DBEnv dataSource)
	{
		super(dataSource);
	}
	
	/**
	 *  得到主键的值存到list里面
	 * @param entityClass
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	private  <T> List<Object> getPkListVal(Class<T> entityClass,T entity) throws Exception{
		Map<String, String> keyFieldStr = EntityFactory.getKeyFiledName(entityClass);
		if(keyFieldStr==null || keyFieldStr.size() <= 0) {
            return null;
        }
		List<Object> listVal = new LinkedList<Object>();
		for(String fieldStr : keyFieldStr.keySet()){
			String getMethodStr = "get" + StringUtils.capitalize(fieldStr);
			Method getMethod = entityClass.getMethod(getMethodStr);
			if(getMethod == null)
				return null;
			Object value = getMethod.invoke(entity);
			if(value == null){
				throw new  Exception("get pk key is null!");
			}
			listVal.add(value);
		}
		return listVal;
	}
	
	/**
	 * 根据主键查询
	 */
	public <T>  T getByPk(Class<T> entityClass, String key, T entity)
		throws Exception
	{
		List<Object> listVal = getPkListVal(entityClass,entity);
		return query(key, EntityFactory.findByIdSql(entityClass),new PkStatementSetterImpl<List<Object>>(listVal),new EntityResultSetWrapImpl<T>(entityClass));
	}
	
	/**
	 * 组装fields与column的对应关系,在setValues里?与实体中的值对应上
	 * @param entityClass
	 * @param entity
	 * @param conditionColumns
	 * @param fields
	 * @throws Exception
	 */
	private <T> void fieldsColumnRelation(Class<T> entityClass, T entity,List<String> conditionColumns, List<Field> fields) throws Exception{
		Map<String,String> columnMap=EntityFactory.getEntityColumnMap(entityClass);
		//偏历filed,如果entity中该属性有值存在，则作为查询的条件
		for(String fieldName:columnMap.keySet())
		{
			Field field=entityClass.getDeclaredField(fieldName);
			if(field!=null)
			{
				field.setAccessible(true);
				Object obj=field.get(entity);
				if(obj!=null)
				{
					conditionColumns.add(columnMap.get(fieldName));
					fields.add(field);
				}
			}
		}
	}
	
	/**
	 * 根据条件查询
	 */
	@Override
	public <T> T getByCondition(Class<T> entityClass, String key, T entity)
			throws Exception {
		List<String> conditionColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, conditionColumns, fields);
		String table =EntityFactory.getEntityTable(entityClass);
		String sql =EntitySqlMarker.makeSelectSql(table,conditionColumns);
		
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);
		return query(key, sql, setter, new EntityResultSetWrapImpl<T>(entityClass));
	}
	/**
	 * 查询所有
	 * @throws Exception
	 */
	public <T> List<T> getAll(Class<T> entityClass,String key) throws Exception
	{
		String sql = EntityFactory.getFindAllSql(entityClass);
		return queryCollection(key, sql,new EntityResultSetWrapImpl<T>(entityClass));
	}
	
	public <T> T updateByPk(Class<T> entityClass,String key,  T entity)
		throws Exception
	{
		String updateSql = EntityFactory.getUpdateSql(entityClass);
		int ret = update(key, updateSql, new EntityStatementSetterImpl<T>(entityClass, entity));
		if (ret > 0) {
			return entity;
		}
		return null;
	}
	
	@Override
	public <T> T updateByCondition(Class<T> entityClass, String key, T entity) throws Exception {
		
		Map<String,String> pks =EntityFactory.getKeyFiledName(entityClass);
		if(pks==null || pks.size()<=0)
		{
			throw new SQLException("updateByCondition param t has not pk cloumn");
		}
		List<String> conditionColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, conditionColumns, fields);
		Iterator<String> it=conditionColumns.iterator();
		while(it.hasNext())
		{
			if(pks.values().contains(it.next()))
			{
				it.remove();
			}
		}
		String table =EntityFactory.getEntityTable(entityClass);
		List<String> wheres =new LinkedList<String>();
		for(String pk :pks.keySet())
		{
			wheres.add(pks.get(pk));
		}
		String sql =EntitySqlMarker.makeUpdateSql(table,conditionColumns,wheres);
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);
		int ret = update(key, sql, setter);
		if (ret > 0) {
			return entity;
		}
		return null;
	}
	
	public <T> int updateByCondition(Class<T> entityClass,String key,DBCondition condition) throws Exception
	{
		String sql =EntitySqlMarker.makeUpdateSql(condition);
		IPreparedStatementSetter setter =new EntityConditionStatmentSetterImpl(entityClass,condition);
		int ret = update(key, sql, setter);
		return ret;
	}
	
//	public <T> T save(Class<T> entityClass,String key, T entity)
//		throws Exception
//	{
//		String saveSql = EntityFactory.getInsertSql(entityClass);
//		Map<String, String> keyFieldStr = EntityFactory.getKeyFiledName(entityClass);
//		if(BlankUtil.isBlank(keyFieldStr) || keyFieldStr.size() ==0)
//		{
//			insert(key, saveSql, new EntityStatementSetterImpl<T>(entityClass,entity,false));
//		}
//		else
//		{
//			Serializable id = insertReturnId(key, saveSql,
//				new EntityStatementSetterImpl<T>(entityClass,entity, false));
//			
//			for(String fieldStr : keyFieldStr.keySet()){
//				String getMethodStr = "get" + StringUtils.capitalize(fieldStr);
//				String setMethodStr = "set" + StringUtils.capitalize(fieldStr);
//				Method getMethod = entityClass.getMethod(getMethodStr);
//				Class<?> returnClass = getMethod.getReturnType();
//				Method setMethod = entityClass.getMethod(setMethodStr, returnClass);
//				Object obj=id;
//				if(returnClass.isAssignableFrom(Integer.class) || returnClass.isAssignableFrom(int.class))
//				{
//					obj=Integer.parseInt(id.toString());
//				}
//				if(returnClass.isAssignableFrom(Long.class) || returnClass.isAssignableFrom(long.class))
//				{
//					obj=Long.parseLong(id.toString());
//				}
//				if(returnClass.isAssignableFrom(String.class)|| returnClass.isAssignableFrom(Character.class) || returnClass.isAssignableFrom(char.class))
//				{
//					obj=id.toString();
//				}
//				setMethod.invoke(entity, obj);
//			}
//		}
//		return entity;
//	}

	@Override
	public <T> int deleteByCondition(Class<T> entityClass, String key,T entity) throws Exception {
		List<String> conditionColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, conditionColumns, fields);
		String table =EntityFactory.getEntityTable(entityClass);
		String sql =EntitySqlMarker.makeDeleteSql(table,conditionColumns);
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);
		return delete(key, sql,setter);
	}
	
	public <T> int deleteByPk(Class<T> entityClass,String key, T entity)
		throws Exception
	{
		List<Object> listVal = getPkListVal(entityClass,entity);
		String sql = EntityFactory.getDeleteSql(entityClass);
		if(listVal==null){
			throw new GroundException("delete by pk error,pk is null.",sql,entity.toString());
		}
		return delete(key,sql ,new PkStatementSetterImpl<List<Object>>(listVal));
	}
	
	public <T> List<T> list(Class<T> entityClass,String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		return queryCollection(key, sql,setter,new EntityResultSetWrapImpl<T>(entityClass));
	}
	
	public <T> List<T> list(Class<T> entityClass,String key,T entity) throws Exception
	{
		List<String> conditionColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, conditionColumns, fields);
		String table =EntityFactory.getEntityTable(entityClass);
		String sql =EntitySqlMarker.makeSelectSql(table,conditionColumns);
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);	
		return queryCollection(key, sql,setter,new EntityResultSetWrapImpl<T>(entityClass));
	}
	/**
	 * 条件查询
	 */
	public <T> List<T> queryByCondition(Class<T> entityClass,String key,DBCondition condition) throws Exception
	{
		String sql=EntitySqlMarker.makeSelectSql(condition);
		IPreparedStatementSetter setter =new EntityConditionStatmentSetterImpl(entityClass,condition);	
		return queryCollection(key, sql,setter,new EntityResultSetWrapImpl<T>(entityClass));	
	}

	@Override
	public <T> T insert(Class<T> entityClass, String key, T entity)
			throws Exception {
		List<String> setColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, setColumns, fields);
		
		String table =EntityFactory.getEntityTable(entityClass);
		
		Map<String, Boolean> pkMap =EntityFactory.getEntityAutoPkMap(entityClass);
		if(pkMap==null)
		{
			pkMap=new HashMap<String,Boolean>();
		}
		String sql =EntitySqlMarker.makeInsertSql(table,setColumns,pkMap);
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);
		int ret = insert(key, sql, setter);
		if (ret > 0) {
			return entity;
		}
		return null;
		
//		String keyFieldStr =null;// EntityFactory.getKeyFiledName(entityClass);
//		
//		//主键为空,或者标志为非自主键
//		if(BlankUtil.isBlank(keyFieldStr))
//		{
//			insert(key, saveSql, new EntityStatementSetterImpl<T>(entityClass,entity));
//		}
//		else
//		{
//			Serializable id = insertReturnId(key, saveSql,
//				new EntityStatementSetterImpl<T>(entityClass,entity));
//			String getMethodStr = "get" + StringUtils.capitalize(keyFieldStr);
//			String setMethodStr = "set" + StringUtils.capitalize(keyFieldStr);
//			Method getMethod = entityClass.getMethod(getMethodStr);
//			Class<?> returnClass = getMethod.getReturnType();
//			Method setMethod = entityClass.getMethod(setMethodStr, returnClass);
//			Object obj=id;
//			if(returnClass.isAssignableFrom(Integer.class) || returnClass.isAssignableFrom(int.class))
//			{
//				obj=Integer.parseInt(id.toString());
//			}
//			if(returnClass.isAssignableFrom(Long.class) || returnClass.isAssignableFrom(long.class))
//			{
//				obj=Long.parseLong(id.toString());
//			}
//			if(returnClass.isAssignableFrom(String.class)|| returnClass.isAssignableFrom(Character.class) || returnClass.isAssignableFrom(char.class))
//			{
//				obj=id.toString();
//			}
//			setMethod.invoke(entity, obj);
//		}
//		return entity;
	}
	
	/**
	 * 分页查询
	 */
	public <T> Page<T> page(Class<T> entityClass,PageQuery<T> page,String key) throws Exception
	{
	    String fragment ="select * from "+ EntityFactory.getEntityTable(entityClass) +" where 1=1 ";	
	    fragment += combinPageQueryStr(entityClass,page.getEntity());
	    
	    String orderBy = page.getOrderBy();
		String order = page.getOrder();
		if (!StringUtils.isBlank(orderBy) && !StringUtils.isBlank(order))
        {
            fragment  += " order by " + orderBy + " " + order;
		}
		int start =page.getStart();
		int pageSize =page.getPageSize();
	    fragment += " limit "+start+","+pageSize;
	    return this.queryPage(key, fragment, new EntityResultSetWrapImpl<T>(entityClass));
	
	}
	
	public <T> String combinPageQueryStr(Class<T> entityClass, T entity) throws Exception
	{
		StringBuffer buf= new StringBuffer();
		
		Map<String,String> columnMap=EntityFactory.getEntityColumnMap(entityClass);
		//偏历filed,如果entity中该属性有值存在，则作为查询的条件
		for(String field:columnMap.keySet())
		{
			String methodStr="get"+StringUtils.capitalize(field);
			Method getMethod =entityClass.getMethod(methodStr);
			if(getMethod==null)
			{
				methodStr="is"+StringUtils.capitalize(field);
				getMethod =entityClass.getMethod(methodStr);
			}
			if(getMethod==null)
				continue;
			Class<?> returnClass =getMethod.getReturnType();
			
			Object obj= getMethod.invoke(entity);
			if(obj==null)
				continue;
            if (m_logger.isDebugEnabled()) {
                m_logger.debug("field is: " + field + ", value is: " + obj);
            }
            //只支持简单类型和时间类型
			if(ClassUtils.isPrimitiveWrapper(returnClass) )
			{
				if(returnClass.isAssignableFrom(Boolean.class))
				{
				   boolean b= Boolean.parseBoolean(obj.toString());
				   buf.append(" and "+ columnMap.get(field)+" ="+ ((b==true)?1:0));   
				}else
				{
				    buf.append(" and "+ columnMap.get(field)+" ="+ obj.toString());
				}
			}else if(returnClass.isAssignableFrom(String.class))
			{
				buf.append(" and "+ columnMap.get(field)+" like '%"+ obj.toString()+"%' ");
			}
			else if(returnClass.isAssignableFrom(Date.class)){
				buf.append(" and "+ columnMap.get(field)+" ='"+ obj.toString() +"'");
			}else {
				continue;
			}
		}
		String result =buf.toString();
		if(StringUtils.isBlank(result)) {
            result = "";
        }
		return result;
	}
	/**
	 * 计数
	 */
	public <T> int count(Class<T> entityClass,String key,T entity) throws Exception
	{
		List<String> conditionColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, conditionColumns, fields);
		String table =EntityFactory.getEntityTable(entityClass);
		String sql =EntitySqlMarker.makeCountSql(table,conditionColumns);
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);
		return count(key, sql, setter);
	}
	
	/**
	 * 全库计数
	 */
	public <T> int countGloble(Class<T> entityClass,T entity) throws Exception
	{
		List<String> conditionColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, conditionColumns, fields);
		String table =EntityFactory.getEntityTable(entityClass);
		String sql =EntitySqlMarker.makeCountSql(table,conditionColumns);
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);
		return countGloble(sql, setter);
	}
	/**
	 * 全库列表
	 */
	public <T> List<T> listAllDb(Class<T> entityClass,T entity) throws Exception
	{
		List<String> conditionColumns=new LinkedList<String>();
		List<Field> fields=new LinkedList<Field>();
		fieldsColumnRelation(entityClass, entity, conditionColumns, fields);
		String table =EntityFactory.getEntityTable(entityClass);
		String sql =EntitySqlMarker.makeSelectSql(table,conditionColumns);
		
		IPreparedStatementSetter setter =new EntitySpecFieldStatmentSetterImpl<T>(entity,fields);
		return queryGlobleCollection(sql, setter, new EntityResultSetWrapImpl<T>(entityClass));
	}
	/**
	 * 全库条件查询
	 */
	public <T> List<T> queryByConditionAllDb(Class<T> entityClass,DBCondition condition) throws Exception
	{
		String sql=EntitySqlMarker.makeSelectSql(condition);
		IPreparedStatementSetter setter =new EntityConditionStatmentSetterImpl(entityClass,condition);	
		return queryGlobleCollection(sql,setter,new EntityResultSetWrapImpl<T>(entityClass));	
	}
}