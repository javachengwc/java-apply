package com.ground.entity.callback;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ground.core.callback.IResultSetExtractor;
import com.ground.core.callback.IRowMapper;
import com.ground.entity.EntityFactory;
import com.util.base.ObjectUtil;
import com.util.date.SysDateTime;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;


public class EntityResultSetWrapImpl<T> implements IResultSetExtractor<T>,IRowMapper<T>
{
	private static Logger m_logger = Logger.getLogger(EntityResultSetWrapImpl.class);
	
    protected Class<T> entityClass;
    
    /**
     * column--field
     */
    private Map<String,String> columnMap;
	
	public EntityResultSetWrapImpl( Class<T> entityClass) {

		this.entityClass =entityClass;
		
		Map<String,String> fieldColumnMap= EntityFactory.getEntityColumnMap(this.entityClass);
		this.columnMap=new HashMap<String,String>();
		for(Entry<String,String> e:fieldColumnMap.entrySet())
		{
			columnMap.put(e.getValue(), e.getKey());
		}
	}
	
	public EntityResultSetWrapImpl( Class<T> entityClass,Map<String,String> columnMap) {

		this(entityClass);
		this.columnMap=columnMap;
	}

	public Map<String, String> getColumnMap()
	{
		return columnMap;
	}

	public void setColumnMap(Map<String, String> columnMap)
	{
		this.columnMap = columnMap;
	}
    
	public T wrapRs(ResultSet rs)  throws SQLException
	{

		ResultSetMetaData rsmt = rs.getMetaData();
		int columnCount = rsmt.getColumnCount();
		try
		{
		    T t =entityClass.newInstance();
			for(int i = 1; i <= columnCount; i++)
			{
				
				String name = rsmt.getColumnName(i);
				Object value = rs.getObject(i);
				String field =null;
				if(columnMap!=null ) {
                    field = columnMap.get(name);
                }
				if(StringUtils.isBlank(field)) {
                    continue;
                }
				if(value==null)
				{
					continue;
				}
				String methodStr="get"+StringUtils.capitalize(field);
				String setMethodStr="set"+StringUtils.capitalize(field);
				Method getMethod =entityClass.getMethod(methodStr);
				if(getMethod==null)
				{
					methodStr="is"+StringUtils.capitalize(field);
					getMethod =entityClass.getMethod(methodStr);
				}
				if(getMethod==null)
					continue;
				Class<?> returnClass =getMethod.getReturnType();
				Method setMethod =entityClass.getMethod(setMethodStr, returnClass);
				if(setMethod==null)
					continue;
				//只支持简单类型和时间类型
				if(ClassUtils.isPrimitiveOrWrapper(returnClass) || returnClass.isAssignableFrom(String.class))
				{
					invokeSet(returnClass,t,setMethod,value);
				}else if(returnClass.isAssignableFrom(Date.class)){
					invokeSet(returnClass,t,setMethod,rs.getString(i));
				}else {
					continue;
				}
					
			}
			return t;
		}catch(Exception e)
		{
			m_logger.error(e.getMessage());
			e.printStackTrace(System.out);
		}
		return null;	
	}
	
	public T extractData(ResultSet rs) throws SQLException
	{
		if(rs.next())
		    return wrapRs(rs);
		else
			return null;
		
	}
	
	public T mapRow(ResultSet rs, int paramInt) throws SQLException
    {
	    return wrapRs(rs);	
    }
	
	
	public void invokeSet(Class<?> returnClass,T t,Method setMethod,Object value)
	{
		try{
			if(returnClass.isAssignableFrom(Integer.class) || returnClass.isAssignableFrom(int.class))
			{
				setMethod.invoke(t, ObjectUtil.numberSerialize(ObjectUtil.obj2Integer(value),returnClass));
	            return;
			}
			if(returnClass.isAssignableFrom(Long.class) || returnClass.isAssignableFrom(long.class))
			{
				setMethod.invoke(t, ObjectUtil.numberSerialize(ObjectUtil.obj2Long(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(String.class))
			{
				setMethod.invoke(t, ObjectUtil.obj2Str(value));
				return;
			}
			if(returnClass.isAssignableFrom(Boolean.class) || returnClass.isAssignableFrom(boolean.class)  )
			{
				setMethod.invoke(t, ObjectUtil.obj2Boolean(value));
				return;
			}
			if(returnClass.isAssignableFrom(Byte.class) || returnClass.isAssignableFrom(byte.class)  )
			{
				setMethod.invoke(t, ObjectUtil.numberSerialize(ObjectUtil.obj2Byte(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(Character.class) || returnClass.isAssignableFrom(char.class)  )
			{
                Character rt= ObjectUtil.obj2Char(value);
                if(returnClass==char.class && rt==null)
                {
                    rt ="0".toCharArray()[0];
                }
				setMethod.invoke(t, rt);
				return;
			}
			if(returnClass.isAssignableFrom(Short.class) || returnClass.isAssignableFrom(short.class)  )
			{
				setMethod.invoke(t, ObjectUtil.numberSerialize(ObjectUtil.obj2Short(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(Float.class) || returnClass.isAssignableFrom(float.class)  )
			{
				setMethod.invoke(t,ObjectUtil.numberSerialize(ObjectUtil.obj2Float(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(Double.class) || returnClass.isAssignableFrom(double.class)  )
			{
				setMethod.invoke(t, ObjectUtil.numberSerialize(ObjectUtil.obj2Double(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(Date.class)){
				long time = SysDateTime.getTime((String) value);
				Constructor<?> constructor = returnClass.getConstructor(long.class);
				Object obj = constructor.newInstance(time);
				setMethod.invoke(t, obj);
				return;
			}
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
	}	
}
