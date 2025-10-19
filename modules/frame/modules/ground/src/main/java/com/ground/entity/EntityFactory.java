package com.ground.entity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ground.entity.annotation.Column;
import com.ground.entity.annotation.Entity;
import com.ground.entity.annotation.Transient;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityFactory
{
	private static Logger m_logger = LoggerFactory.getLogger(EntityFactory.class);
	//实体--表名映射
	public static Map<String,String> entityTableMap=new HashMap<String,String>();
	//实体字段与表字段映射
	public static Map<String,Map<String,String>> entityColumnMap=new HashMap<String,Map<String,String>>();
	//实体字段类型映射
	public static Map<String,Map<String,Class<?>>> entityFieldTypeMap =new HashMap<String,Map<String,Class<?>>>();
	//实体主键映射
	public static Map<String,Map<String,String>> entityKeyMap=new LinkedHashMap<String,Map<String,String>>();	
	//实体主键是否自增映射
	public static Map<String,Map<String,Boolean>> entityAutoPkMap=new HashMap<String,Map<String,Boolean>>();	
	//实体--get sql语句映射
	public static Map<String,String> findByIdSqlMap =new HashMap<String,String>();
	//实体--find all语句映射
	public static Map<String,String> findAllSqlMap =new HashMap<String,String>();
	//实体--update sql语句映射
	public static Map<String,String> updateSqlMap=new HashMap<String,String>();
	//实体--insert sql语句映射
	public static Map<String,String> insertSqlMap=new HashMap<String,String>();
	//实体--deleteSqlMap语句映射
	public static Map<String,String> deleteSqlMap=new HashMap<String,String>();
	
	public static void regist(Set<String> clazzs)
	{
		for(String clazzName:clazzs)
		{
			try{
			    Class<?> clazz=Class.forName(clazzName);
			    regist(clazz);
			}catch(Exception e)
			{
				e.printStackTrace(System.out);
		    	m_logger.error(e.getMessage());
			}	    
		}
		//show();
	}
	
	@SuppressWarnings("unchecked")
	public static void regist(Class<?> clazz) throws Exception
	{
		String entityName =clazz.getName();
		Entity entityAn = (Entity)clazz.getAnnotation(Entity.class);
		if(entityAn==null)
			return;
		String tableName=entityAn.table();
		entityTableMap.put(entityName, tableName);
		
        Map<String,String> columnMap =new LinkedMap();
        Map<String,Class<?>> typeMap =new HashMap<String,Class<?>>();
		
        //主键<pkFieldName,pkColumn>;
        LinkedHashMap<String,String> pkFieldName =new LinkedHashMap<String, String>();
        //主键是否自增
        Map<String,Boolean> autoPk=new HashMap<String,Boolean>();	

		for(Field field:clazz.getDeclaredFields())
		{
			//该字段不需要关联数据库
			Transient trans = field.getAnnotation(Transient.class);
			if(trans != null){
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			//属性名
			String fieldName=field.getName();
			
			//列名
			String columnName="";
			
			if(column==null){
				throw new Exception("Column annotation must be used in an entity, you can use transient if you don't want to map this field! entityName:"+entityName+",fieldName:"+fieldName);
				//columnName = fieldName;
			}else{
				columnName = column.name();
				//如果是主键就先不放到columnMap里面,因为在update的时候,主键会出现在最后一个问号.(update set user = ?,passwd=? where id = ?);那么在setValues的时候会出现对应不上值
				if(column.isPK()){
					pkFieldName.put(fieldName, columnName);
					boolean autoIncr = column.autoIncr();
					autoPk.put(columnName,autoIncr);
					HashMap<String, Boolean > temp = new HashMap<String, Boolean>();
					temp.put(fieldName,autoIncr);
					
					entityAutoPkMap.put(entityName, temp);
					entityKeyMap.put(entityName, pkFieldName);
				}else{
					columnMap.put(fieldName, columnName);	
				}
			}
			Class<?> type=(Class<?>)field.getGenericType();
			typeMap.put(fieldName, type);
		}
		//把主键放到map的最后一个位置,为了不在setValues时发生错误
		for(String filedName : pkFieldName.keySet()){
			columnMap.put(filedName, pkFieldName.get(filedName));
		}
		entityColumnMap.put(entityName, columnMap);
		entityFieldTypeMap.put(entityName, typeMap);
		List<String> columns=new ArrayList<String>();
		Collections.addAll(columns, columnMap.values().toArray(new String[columnMap.size()]));
		
		//只有主键不为空才会生成该对应的SQL语句 select,delete,update by pk
		if(autoPk != null && autoPk.size() > 0){
			String findById=EntitySqlMarker.findById(tableName,autoPk);
			findByIdSqlMap.put(entityName, findById);
			
			String updateSqlStr=EntitySqlMarker.makeUpdateSqlByPk(tableName,columns,autoPk);
			updateSqlMap.put(entityName, updateSqlStr);
			
			String deleteSqlStr=EntitySqlMarker.makeDeleteSqlByPk(tableName,columns,autoPk);
			deleteSqlMap.put(entityName,deleteSqlStr);
		}
		
		String insertSqlStr =EntitySqlMarker.makeInsertSql(tableName, columns,autoPk);
		insertSqlMap.put(entityName, insertSqlStr);
		
		String findAllSqlStr =EntitySqlMarker.makeSelectAllSql(tableName);
		findAllSqlMap.put(entityName, findAllSqlStr);
		
	}
	
	
	public static String getEntityTable(Class<?> clazz)
	{
		return entityTableMap.get(clazz.getName());
	}
	
	public static Map<String,String> getEntityColumnMap(Class<?> clazz)
	{
		return entityColumnMap.get(clazz.getName());
	}
	
	public static Map<String, String> getKeyFiledName(Class<?> clazz)
	{
		return entityKeyMap.get(clazz.getName());
	}
	
	public static  Map<String, Boolean> getEntityAutoPkMap(Class<?> clazz) {
		return entityAutoPkMap.get(clazz.getName());
	}
	
	/**
	 * 通过实体类名获取对应的表名称
	 * @param clazz  类名全称
	 * @return
	 */
	public static String getEntityTableName(String clazz)
	{
		return entityTableMap.get(clazz);
	}
	
	/**
	 * 通过实体类名,属性名获取对应的表的列名称
	 * @param clazz 类名全称
	 * @param fieldName 属性名
	 * @return
	 */
	public static String getEntityColumnName(String clazz,String fieldName)
	{
		Map<String,String> columnMap =entityColumnMap.get(clazz);
		if(columnMap ==null)
			return null;
		return columnMap.get(fieldName);
	}
	
	/**
	 * 通过实体类名,属性名获取属性的类型
	 * @param clazz 类名全称
	 * @param fieldName 属性名
	 * @return
	 */
	public static Class<?> getEntityFieldType(String clazz,String fieldName)
	{
		Map<String,Class<?>> typeMap =entityFieldTypeMap.get(clazz);
		if(typeMap ==null)
			return null;
		return typeMap.get(fieldName);
	}
	
	public static String findByIdSql(Class<?> clazz)
	{
		return findByIdSqlMap.get(clazz.getName());
	}
	
	public static String getUpdateSql(Class<?> clazz)
	{
		return updateSqlMap.get(clazz.getName());
	}
	
	public static String getInsertSql(Class<?> clazz)
	{
		return insertSqlMap.get(clazz.getName());
	}
	
	public static String getDeleteSql(Class<?> clazz)
	{
		return deleteSqlMap.get(clazz.getName());
	}
	
	public static String getFindAllSql(Class<?> clazz)
	{
		return findAllSqlMap.get(clazz.getName());
	}
	
	public static void show()
	{
		System.out.println("--------------entity factory show--------------");
		System.out.println("------------entityTableMap show------------");
		showMap(entityTableMap);
		System.out.println("------------entityColumnMap show------------");
		for(String entity:entityColumnMap.keySet())
		{
			System.out.println(entity+" :");
			showMap(entityColumnMap.get(entity));
		}
		System.out.println("------------entityKeyMap show------------");
		for(String pk:entityKeyMap.keySet())
		{
			System.out.println(pk+" :");
			showMap(entityColumnMap.get(pk));
		}
		
		
		System.out.println("------------getSqlMap show------------");
		showMap(findByIdSqlMap);
		System.out.println("------------updateSqlMap show------------");
		showMap(updateSqlMap);
		System.out.println("------------insertSqlMap show------------");
		showMap(insertSqlMap);
		System.out.println("------------deleteSqlMap show------------");
		showMap(deleteSqlMap);
		
		System.out.println("--------------entity factory show end --------------");
	}
	public static void showMap(Map<String,String> map)
	{
		for(String entity: map.keySet())
		{
			System.out.println(entity+" --> "+map.get(entity).toString());
		}	
	}
}
