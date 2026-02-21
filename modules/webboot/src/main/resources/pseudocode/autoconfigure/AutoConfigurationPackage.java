package com.boot.pseudocode.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({AutoConfigurationPackages.Registrar.class})
public @interface AutoConfigurationPackage
{
}
