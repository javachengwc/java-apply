package com.ground.entity.callback;

import com.util.TypeUtil;
import com.util.date.SysDateTime;

import java.sql.PreparedStatement;
import java.util.Date;

public class SetterUtil {
	
	public static void invokeSet(PreparedStatement ps,Class<?> returnClass,Object value,int index)
	{
		try{
			if(returnClass.isAssignableFrom(Integer.class) || returnClass.isAssignableFrom(int.class))
			{
				ps.setInt(index, TypeUtil.objectToInteger(value));
	            return;
			}
			if(returnClass.isAssignableFrom(Long.class) || returnClass.isAssignableFrom(long.class))
			{
				ps.setLong(index, TypeUtil.objectToLong(value));
				return;
			}
			if(returnClass.isAssignableFrom(String.class)|| returnClass.isAssignableFrom(Character.class) || returnClass.isAssignableFrom(char.class))
			{
				ps.setString(index, TypeUtil.objectToString(value));
				return;
			}
			if(returnClass.isAssignableFrom(Boolean.class) || returnClass.isAssignableFrom(boolean.class)  )
			{
				ps.setBoolean(index, TypeUtil.objectToBoolean(value));
				return;
			}
			if(returnClass.isAssignableFrom(Byte.class) || returnClass.isAssignableFrom(byte.class)  )
			{
				ps.setByte(index, TypeUtil.objectToByte(value));
				return;
			}
			if(returnClass.isAssignableFrom(Short.class) || returnClass.isAssignableFrom(short.class)  )
			{
				ps.setShort(index, TypeUtil.objectToShort(value));
				return;
			}
			if(returnClass.isAssignableFrom(Float.class) || returnClass.isAssignableFrom(float.class)  )
			{
				ps.setFloat(index, TypeUtil.objectToFloat(value));
				return;
			}
			if(returnClass.isAssignableFrom(Double.class) || returnClass.isAssignableFrom(double.class)  )
			{
				ps.setDouble(index, TypeUtil.objectToDouble(value));
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
