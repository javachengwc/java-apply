package com.util.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类说明：字段注解定义;表示要做注入
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
	
	/**是否为单实模式  默认为true,目前只支持单例**/
	boolean isSingle() default true;
	
	/**实例名称  如果注入类型的实例不唯一，需要用名称标示(全路径[com.yy.etn..])**/
	Class<?> instance() default Object.class;
}
