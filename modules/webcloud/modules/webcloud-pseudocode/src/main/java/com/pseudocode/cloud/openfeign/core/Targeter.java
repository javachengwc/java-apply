package com.pseudocode.cloud.openfeign.core;

import com.pseudocode.netflix.feign.core.Feign;
import com.pseudocode.netflix.feign.core.Target;

interface Targeter {
    <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
                 Target.HardCodedTarget<T> target);
}