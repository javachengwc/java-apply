package com.mybatis.pseudocode.mybatisspring.annotation;

import com.mybatis.pseudocode.mybatisspring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
@Import({MapperScannerRegistrar.class})
public @interface MapperScan
{
    public abstract String[] value();

    public abstract String[] basePackages();

    public abstract Class<?>[] basePackageClasses();

    public abstract Class<? extends BeanNameGenerator> nameGenerator();

    public abstract Class<? extends Annotation> annotationClass();

    public abstract Class<?> markerInterface();

    public abstract String sqlSessionTemplateRef();

    public abstract String sqlSessionFactoryRef();

    public abstract Class<? extends MapperFactoryBean> factoryBean();
}
