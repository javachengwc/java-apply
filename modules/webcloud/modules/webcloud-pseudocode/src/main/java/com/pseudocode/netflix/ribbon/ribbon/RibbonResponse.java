package com.pseudocode.netflix.ribbon.ribbon;

import com.pseudocode.netflix.hystrix.core.HystrixInvokableInfo;

public abstract class RibbonResponse<T> {

    public abstract T content();

    public abstract HystrixInvokableInfo<?> getHystrixInfo();
}
