package com.pseudocode.cloud.commons.client.circuitbreaker;

import com.pseudocode.cloud.commons.util.SpringFactoryImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class EnableCircuitBreakerImportSelector extends SpringFactoryImportSelector<EnableCircuitBreaker> {

    @Override
    protected boolean isEnabled() {
        return getEnvironment().getProperty("spring.cloud.circuit.breaker.enabled", Boolean.class, Boolean.TRUE);
    }
}

