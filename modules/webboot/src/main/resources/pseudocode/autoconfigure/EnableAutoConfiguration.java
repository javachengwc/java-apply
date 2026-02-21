package com.boot.pseudocode.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfigurationImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@SuppressWarnings("deprecation")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(EnableAutoConfigurationImportSelector.class)
//@Import注解通过导入的方式实现把实例加入springIOC容器中。
//@Import注解支持导入普通的java类,并将其声明成一个springbean
//@Import注解支持基于ImportSelector的使用,把Selector命中的实例声明成一个springbean。
//@Import注解支持基于ImportBeanDefinitionRegistrar的使用,
//它的registerBeanDefinitions方法会把自定义的beanDefinition注册到BeanDefinitionRegistry,从而生成对应的springbean
public @interface EnableAutoConfiguration
{
    public static final String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}
