package com.app.util;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Component
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext; // Spring应用上下文环境


	public void setApplicationContext(final ApplicationContext applicationContext) {
		SpringContextUtils.applicationContext = applicationContext;
	}


	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

    public static Object getBean(Class clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

	public static Object getBean(final String name) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(final String name, final Class<T> requiredType) {
		if (applicationContext == null) {
			return null;
		}
		return (T) applicationContext.getBean(name, requiredType);
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> getBeanSetOfType(final Class<T> type) {
		if (applicationContext == null) {
			return Collections.emptySet();
		}
		Map<?, ?> map = applicationContext.getBeansOfType(type);
		Set<T> set = new HashSet<T>(map.size());
		for (Entry<?, ?> entry : map.entrySet()) {
			set.add((T) entry.getValue());
		}
		return set;
	}

	public static String[] getBeanNamesOfType(final Class<?> type) {
		if (applicationContext == null) {
			return new String[0];
		}
		return applicationContext.getBeanNamesForType(type);
	}
}
