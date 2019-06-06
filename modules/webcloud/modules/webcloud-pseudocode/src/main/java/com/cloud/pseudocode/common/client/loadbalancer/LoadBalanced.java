package com.cloud.pseudocode.common.client.loadbalancer;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Qualifier
public @interface LoadBalanced {
}
