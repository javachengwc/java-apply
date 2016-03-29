package com.flower.populator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 * 实现ResultSet到Bean和Map的注值
 *
 */
public class ResultSetBeanPopulator extends BasePopulator {

	protected boolean doPopulate(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {

		/**
		 * 首先过滤掉一些常见的不支持的类型
		 */
		if (target instanceof Collection || target instanceof ResultSet
				|| target instanceof ServletRequest
				|| !(source instanceof ResultSet)) {
			return false;
		}

		/**
		 * 将ResultSet放入一个Map中
		 */
		ResultSet resultSet = (ResultSet) source;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			int columnNumber = resultSet.getMetaData().getColumnCount();
			for (int i = 1; i <= columnNumber; i++) {
				resultMap.put(resultSet.getMetaData().getColumnName(i)
						.toUpperCase(), resultSet.getObject(i));
			}
		} catch (SQLException e) {
			logger.debug("Can not get MetaData from ResultSet", e);
			return false;
		}

		/**
		 * 如果目标类型就是一个Map，则直接返回所转换的Map
		 */
		if (target instanceof Map) {
			((Map) target).putAll(resultMap);
			return true;
		}

		/**
		 * 开始进行Bean的转换获得target的属性描述
		 */
		PropertyDescriptor[] targetDescriptors = null;
		try {
			targetDescriptors = Introspector.getBeanInfo(target.getClass())
					.getPropertyDescriptors();
		} catch (IntrospectionException ie) {
			logger.debug("Failed on getting bean's properties", ie);
			return false;
		}

		/**
		 * 对target中所有的属性进行循环注值
		 */
		for (PropertyDescriptor targetDescriptor : targetDescriptors) {

			/**
			 * 过滤掉ignoreProperties中所指定的不需要注值的属性
			 */
			if (targetDescriptor.getName().equals("class")
					|| !doProcess(targetDescriptor.getName(), ignoreProperties)) {
				continue;
			}

			/**
			 * 获得source对象中属性的名称
			 */
			String sourcePropertyName = null;

			/**
			 * 获得Mapping中所定义Source Map中的Key
			 */
			if (propertiesMapping != null
					&& propertiesMapping
							.containsKey(targetDescriptor.getName())) {
				sourcePropertyName = propertiesMapping.get(
						targetDescriptor.getName()).toUpperCase();
			}

			/**
			 * 如果没有Mapping中所定义的Key而有目标名称的Key则取目标名称为Key
			 */
			if ((sourcePropertyName == null && resultMap
					.containsKey(targetDescriptor.getName().toUpperCase()))
					|| (sourcePropertyName != null
							&& !resultMap.containsKey(sourcePropertyName) && resultMap
							.containsKey(targetDescriptor.getName()
									.toUpperCase()))) {
				sourcePropertyName = targetDescriptor.getName().toUpperCase();
			}

			if (sourcePropertyName == null || sourcePropertyName.length() == 0) {
				continue;
			}

			/**
			 * 进行注值
			 */
			try {
				Method writeMethod = targetDescriptor.getWriteMethod();
				if (writeMethod == null) {
					continue;
				}
				Object convertedValue = getConverter().convertValue(resultMap.get(sourcePropertyName), targetDescriptor.getPropertyType(), params);
				writeMethod.invoke(target, new Object[]{convertedValue});

			} catch (Exception e) {
				logger.debug("Exception", e);
			}
		}
		return true;
	}
}
