package com.ground.entity.callback;

import com.util.base.ObjectUtil;
import com.util.date.SysDateTime;

import java.sql.PreparedStatement;
import java.util.Date;

public class SetterUtil {
	
	public static void invokeSet(PreparedStatement ps,Class<?> returnClass,Object value,int index)
	{
		try{
			if(returnClass.isAssignableFrom(Integer.class) || returnClass.isAssignableFrom(int.class))
			{
				ps.setInt(index, (Integer)ObjectUtil.numberSerialize(ObjectUtil.obj2Integer(value), returnClass));
	            return;
			}
			if(returnClass.isAssignableFrom(Long.class) || returnClass.isAssignableFrom(long.class))
			{
				ps.setLong(index, (Long)ObjectUtil.numberSerialize(ObjectUtil.obj2Long(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(String.class) || returnClass.isAssignableFrom(Character.class) || returnClass.isAssignableFrom(char.class) )
			{
                String rt = ObjectUtil.obj2Str(value);
                if(returnClass==char.class && rt==null ) {
                    rt="";
                }
				ps.setString(index,rt);
				return;
			}
			if(returnClass.isAssignableFrom(Boolean.class) || returnClass.isAssignableFrom(boolean.class)  )
			{
				ps.setBoolean(index,ObjectUtil.obj2Boolean(value));
				return;
			}
			if(returnClass.isAssignableFrom(Byte.class) || returnClass.isAssignableFrom(byte.class)  )
			{
				ps.setByte(index, (Byte)ObjectUtil.numberSerialize(ObjectUtil.obj2Byte(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(Short.class) || returnClass.isAssignableFrom(short.class)  )
			{
				ps.setShort(index,(Short)ObjectUtil.numberSerialize(ObjectUtil.obj2Short(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(Float.class) || returnClass.isAssignableFrom(float.class)  )
			{
				ps.setFloat(index,(Float)ObjectUtil.numberSerialize(ObjectUtil.obj2Float(value),returnClass));
				return;
			}
			if(returnClass.isAssignableFrom(Double.class) || returnClass.isAssignableFrom(double.class)  )
			{
				ps.setDouble(index,(Double)ObjectUtil.numberSerialize(ObjectUtil.obj2Double(value),returnClass));
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
