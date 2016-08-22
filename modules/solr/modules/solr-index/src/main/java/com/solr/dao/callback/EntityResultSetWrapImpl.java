package com.solr.dao.callback;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import com.util.BlankUtil;
import com.util.date.SysDateTime;
import com.solr.util.TypeUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * 结果包装类 只支持简单类型
 */
public class EntityResultSetWrapImpl<T> implements ResultSetExtractor<T>,RowMapper<T>
{
	private static Logger m_logger = Logger.getLogger(EntityResultSetWrapImpl.class);
	
    protected Class<T> entityClass;
    
    /**
     * column--field
     */
    private Map<String,String> columnMap;
	
	public EntityResultSetWrapImpl( Class<T> entityClass,Map<String,String> columnMap) {

		this.entityClass =entityClass;
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
				if(columnMap!=null )
				{
					field = columnMap.get(name);
					if(BlankUtil.isBlank(field))
					{
						field = columnMap.get(StringUtils.lowerCase(name));
					}
				}
				if(BlankUtil.isBlank(field))
					continue;
				if(value==null || BlankUtil.isBlank(value))
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
				setMethod.invoke(t, TypeUtils.ObjectToInteger(value));
	            return;
			}
			if(returnClass.isAssignableFrom(Long.class) || returnClass.isAssignableFrom(long.class))
			{
				setMethod.invoke(t, TypeUtils.ObjectToLong(value));
				return;
			}
			if(returnClass.isAssignableFrom(String.class))
			{
				setMethod.invoke(t, TypeUtils.ObjectToString(value));
				return;
			}
			if(returnClass.isAssignableFrom(Boolean.class) || returnClass.isAssignableFrom(boolean.class)  )
			{
				setMethod.invoke(t, TypeUtils.ObjectToBoolean(value));
				return;
			}
			if(returnClass.isAssignableFrom(Byte.class) || returnClass.isAssignableFrom(byte.class)  )
			{
				setMethod.invoke(t, TypeUtils.ObjectToByte(value));
				return;
			}
			if(returnClass.isAssignableFrom(Character.class) || returnClass.isAssignableFrom(char.class)  )
			{
				setMethod.invoke(t, TypeUtils.ObjectToChar(value));
				return;
			}
			if(returnClass.isAssignableFrom(Short.class) || returnClass.isAssignableFrom(short.class)  )
			{
				setMethod.invoke(t, TypeUtils.ObjectToShort(value));
				return;
			}
			if(returnClass.isAssignableFrom(Float.class) || returnClass.isAssignableFrom(float.class)  )
			{
				setMethod.invoke(t, TypeUtils.ObjectToFloat(value));
				return;
			}
			if(returnClass.isAssignableFrom(Double.class) || returnClass.isAssignableFrom(double.class)  )
			{
				setMethod.invoke(t, TypeUtils.ObjectToDouble(value));
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
