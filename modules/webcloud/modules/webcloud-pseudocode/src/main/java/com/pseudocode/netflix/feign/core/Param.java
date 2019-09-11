package com.pseudocode.netflix.feign.core;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.PARAMETER})
public @interface Param
{
    public abstract String value();

    public abstract Class<? extends Expander> expander();

    public abstract boolean encoded();

    public static final class ToStringExpander implements Param.Expander
    {
        public String expand(Object value)
        {
            return value.toString();
        }
    }

    public static abstract interface Expander
    {
        public abstract String expand(Object paramObject);
    }
}
