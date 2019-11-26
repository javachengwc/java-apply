package com.spring.pseudocode.webmvc.config.annotation;


import com.spring.pseudocode.context.context.annotation.Import;

import java.lang.annotation.*;

//@EnableWebMvc注解类上导入了DelegatingWebMvcConfiguration类，它是WebMvcConfigurationSupport的子类，
//除了实例化WebMvcConfigurationSupport实例以外，还收集BeanFactory中所有WebMvcConfigurer的实现，
//汇集到WebMvcConfigurerComposite中，在WebMvcConfigurationSupport实例化过程中会分别调用这些实现，
// 将相应的实例传入这些实现中，供开发者在此基础上添加自定义的配置。
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc
{
}
