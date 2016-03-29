
package com.flower.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Read {
	/**
	 * 将从哪些范围内读取对象，一般是从request里面 也支持session，application范围
	 * @return scope
	 */
	public Scope scope() default Scope.REQUEST_PARAMETER;

	/**
	 * 将使用什么样的键值读取对象，对于field，就是他名字 对于method的parameter，需要指明
	 * @return the key itself
	 */
	public String key() default "";

	/**
	 * 说明这个对象从那种ioc容器获得
	 *
	 * @return 提供设置缺省值
	 */
	public String object() default "";

	/**
	 * 提供设置缺省值
	 * @return 提供设置缺省值
	 */
	public String defaultValue() default "CORE_ANNOTATION_READ_DEFAULTVALUE_DEFAULT";

}
