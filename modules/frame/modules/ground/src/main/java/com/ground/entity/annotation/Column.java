package com.ground.entity.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column
{
	/**表列**/
	public abstract String name();
	
	/**是否为主键**/
	public abstract boolean isPK() default false;
	
	/**是否为自增主键**/
	public abstract boolean autoIncr() default false;
}
