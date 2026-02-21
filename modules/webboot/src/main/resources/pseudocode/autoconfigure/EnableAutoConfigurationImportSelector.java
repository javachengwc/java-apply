package com.boot.pseudocode.autoconfigure;

import org.springframework.core.type.AnnotationMetadata;

@Deprecated
public class EnableAutoConfigurationImportSelector extends AutoConfigurationImportSelector
{
    protected boolean isEnabled(AnnotationMetadata metadata)
    {
        //配置项spring.boot.enableautoconfiguration来开关是否自动装配，默认为true
        if (getClass().equals(EnableAutoConfigurationImportSelector.class)) {
            return ((Boolean)getEnvironment().getProperty("spring.boot.enableautoconfiguration", Boolean.class,
                    Boolean.valueOf(true))).booleanValue();
        }
        return true;
    }
}
