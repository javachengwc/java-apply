package com.flower.populator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletRequest;

public class ServletRequestBeanPopulator extends BasePopulator {

	protected boolean doPopulate(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {
		/**
		 * 首先过滤掉一些常见的不支持的类型
		 */
		if (target instanceof Collection || target instanceof ServletRequest
				|| target instanceof ResultSet
				|| !(source instanceof ServletRequest)) {
			return false;
		}

		Map requstMap = ((ServletRequest) source).getParameterMap();

		/**
		 * 如果目标类型就是一个Map，则直接返回所转换的Map
		 */
		if (target instanceof Map) {
			((Map) target).putAll(requstMap);
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
				sourcePropertyName = propertiesMapping.get(targetDescriptor
						.getName());
			}

			/**
			 * 如果没有Mapping中所定义的Key而有目标名称的Key则取目标名称为Key
			 */
			if ((sourcePropertyName == null && requstMap
					.containsKey(targetDescriptor.getName()))
					|| (sourcePropertyName != null
							&& !requstMap.containsKey(sourcePropertyName) && requstMap
							.containsKey(targetDescriptor.getName()))) {
				sourcePropertyName = targetDescriptor.getName();
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
				Object sourceValue = requstMap.get(sourcePropertyName);
				Class targetType = targetDescriptor.getPropertyType();
				if (((String[]) sourceValue).length == 0) {
					sourceValue = null;
				} else if (((String[]) sourceValue).length >= 1
						&& !targetType.isArray()) {
					String tempAssignValue = ((String[]) sourceValue)[0];
					if (tempAssignValue != null)
						tempAssignValue = tempAssignValue.trim();
					sourceValue = (tempAssignValue == null || tempAssignValue
							.length() == 0) ? null : tempAssignValue;
				}

				if (sourceValue != null) {
					Object convertedValue = getConverter().convertValue(
							sourceValue, targetType, params);
					writeMethod.invoke(target, new Object[] { convertedValue });
				}
			} catch (Exception e) {
				logger.debug("Exception", e);
			}
		}
		return true;
	}
}
