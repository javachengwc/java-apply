package com.solr.util;

import com.util.date.SysDateTime;

import java.sql.PreparedStatement;
import java.util.Date;


public class SetterUtil {
	
	public static void invokeSet(PreparedStatement ps,Class<?> returnClass,Object value,int index)
	{
		try{
			if(returnClass.isAssignableFrom(Integer.class) || returnClass.isAssignableFrom(int.class))
			{
				ps.setInt(index, TypeUtils.ObjectToInteger(value));
	            return;
			}
			if(returnClass.isAssignableFrom(Long.class) || returnClass.isAssignableFrom(long.class))
			{
				ps.setLong(index, TypeUtils.ObjectToLong(value));
				return;
			}
			if(returnClass.isAssignableFrom(String.class)|| returnClass.isAssignableFrom(Character.class) || returnClass.isAssignableFrom(char.class))
			{
				ps.setString(index, TypeUtils.ObjectToString(value));
				return;
			}
			if(returnClass.isAssignableFrom(Boolean.class) || returnClass.isAssignableFrom(boolean.class)  )
			{
				ps.setBoolean(index, TypeUtils.ObjectToBoolean(value));
				return;
			}
			if(returnClass.isAssignableFrom(Byte.class) || returnClass.isAssignableFrom(byte.class)  )
			{
				ps.setByte(index, TypeUtils.ObjectToByte(value));
				return;
			}
			if(returnClass.isAssignableFrom(Short.class) || returnClass.isAssignableFrom(short.class)  )
			{
				ps.setShort(index, TypeUtils.ObjectToShort(value));
				return;
			}
			if(returnClass.isAssignableFrom(Float.class) || returnClass.isAssignableFrom(float.class)  )
			{
				ps.setFloat(index, TypeUtils.ObjectToFloat(value));
				return;
			}
			if(returnClass.isAssignableFrom(Double.class) || returnClass.isAssignableFrom(double.class)  )
			{
				ps.setDouble(index, TypeUtils.ObjectToDouble(value));
				return;
			}
			if(returnClass.isAssignableFrom(Date.class)){
				ps.setString(index, SysDateTime.getDatetime((Date) value));
				return;
			}
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
	}
}
