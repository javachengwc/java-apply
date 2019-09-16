package com.pseudocode.cloud.ribbon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
//@Import(RibbonClientConfigurationRegistrar.class)
public @interface RibbonClients {

    RibbonClient[] value() default {};

    Class<?>[] defaultConfiguration() default {};

}

