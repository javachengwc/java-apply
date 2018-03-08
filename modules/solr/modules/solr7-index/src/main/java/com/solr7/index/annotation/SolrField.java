package com.solr7.index.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SolrField {

    String value() default "";
}
