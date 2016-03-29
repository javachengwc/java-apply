package com.flower.populator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 * 实现Bean和Map之间的注值
 *
 */
public class BeanMapPopulator extends BasePopulator {

	protected boolean doPopulate(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {

		/**
		 * 首先过滤掉一些常见的不支持的类型
		 */
		if (!(source instanceof Map) && !(target instanceof Map)) {
			return false;
		}

		if (source instanceof Collection || target instanceof Collection
				|| source instanceof ResultSet || target instanceof ResultSet
				|| source instanceof ServletRequest
				|| target instanceof ServletRequest) {
			return false;
		}

		return populateBeanMap(source, target, propertiesMapping, ignoreProperties, params);
	}

	private boolean populateBeanMap(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {
		if (source instanceof Map && !(target instanceof Map)) {
			return mapToBeanPopulate((Map) source, target, propertiesMapping,
					ignoreProperties, params);
		} else if (!(source instanceof Map) && target instanceof Map) {
			return beanToMapPopulate(source, (Map) target, propertiesMapping,
					ignoreProperties);
		}
		return MapToMapPopulate((Map) source, (Map) target, propertiesMapping,
				ignoreProperties);
	}

	private boolean mapToBeanPopulate(Map source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {

		/**
		 * 获得target的属性描述
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
			if ((sourcePropertyName == null && source
					.containsKey(targetDescriptor.getName()))
					|| (sourcePropertyName != null
							&& !source.containsKey(sourcePropertyName) && source
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
				Object convertedValue = getConverter().convertValue(source.get(sourcePropertyName), targetDescriptor.getPropertyType(), params);
				writeMethod.invoke(target, new Object[]{convertedValue});
			} catch (Exception e) {
				logger.debug("Exception", e);
			}
		}

		return true;
	}

	private boolean beanToMapPopulate(Object source, Map target,
			Map<String, String> propertiesMapping, String[] ignoreProperties) {

		/**
		 * 获得source和target的属性描述
		 */
		PropertyDescriptor[] sourceDescriptors = null;
		try {
			sourceDescriptors = Introspector.getBeanInfo(source.getClass())
					.getPropertyDescriptors();
		} catch (IntrospectionException ie) {
			logger.debug("Failed on getting bean's properties", ie);
			return false;
		}

		for (PropertyDescriptor sourceDescriptor : sourceDescriptors) {

			if (sourceDescriptor.getName().equals("class")) {
				continue;
			}

			try {
				Object sourceValue = sourceDescriptor.getReadMethod().invoke(source, new Object[0]);

				if (sourceValue != null) {
					target.put(sourceDescriptor.getName(), sourceValue);
				}
			} catch (Exception e) {
				logger.debug("Exception", e);
			}
		}

		return true;
	}

	private boolean MapToMapPopulate(Map source, Map target,
			Map<String, String> propertiesMapping, String[] ignoreProperties) {
		target.putAll(source);
		return true;
	}
}
