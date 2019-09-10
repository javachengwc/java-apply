package com.pseudocode.cloud.commons.client.circuitbreaker;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableCircuitBreakerImportSelector.class})
public @interface EnableCircuitBreaker {

}
