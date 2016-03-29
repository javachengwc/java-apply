package com.flower.populator;

import java.util.Map;

import org.apache.log4j.Logger;

import com.flower.converter.PrimitiveType;

public abstract class BaseSimplePopulator implements IPopulator {

	/**
	 * 用于记录日志的Logger
	 */
	protected final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 实现了IPopulator中的相应方法
	 */
	public boolean populate(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {

		if (source == null || target == null
				|| source.getClass() == Object.class
				|| target.getClass() == Object.class
				|| PrimitiveType.isPriType(source.getClass())
				|| PrimitiveType.isPriType(target.getClass())
				|| PrimitiveType.isPriArrayType(source.getClass())
				|| PrimitiveType.isPriArrayType(target.getClass())) {
			return false;
		}

		return doPopulate(source, target, propertiesMapping, ignoreProperties);
	}

	/**
	 * 子类所需要实现的抽象方法
	 * 将source对象中的值注入到target对象中。
	 * @param source 注值时的源对象
	 * @param target 注值时的目标对象
	 * @param propertiesMapping 注值时可以指定源对象中属性和目标对象中属性的对应关系，Map中的Key为目标对象属性的名称，String类型；Value为源对象属性的名称，String类型。
	 * @param ignoreProperties 目标对象中需要被忽略的属性名称的String数组。
	 * @return 是否成功的进行了注值，如果一个实现没有能力对指定的对象进行注值，也应返回false
	 */
	protected abstract boolean doPopulate(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params);
}
