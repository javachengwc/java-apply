package com.flower.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsType {
	private Type[] argTypes;
	public GenericsType(Type genericFieldType){
		if (genericFieldType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericFieldType;
			argTypes = aType.getActualTypeArguments();
		}
	}
	
	@SuppressWarnings("unchecked")
	public TypeVo getType(int index)throws Exception{
		TypeVo vo =  new TypeVo();
		if(argTypes.length>0){
			if (index >= argTypes.length || index < 0) {
				throw new RuntimeException("你输入的索引"
						+ (index < 0 ? "不能小于0" : "超出了参数的总数"));
			}
			if(argTypes[index] instanceof ParameterizedType){
				vo.setNextType(argTypes[index]);
				vo.setCls((Class)((ParameterizedType)argTypes[index]).getRawType());
			}else
			{
				vo.setCls((Class)argTypes[index]);
			}
		}else{
			vo.setCls(Object.class);
		}
		return vo;
	}
}
