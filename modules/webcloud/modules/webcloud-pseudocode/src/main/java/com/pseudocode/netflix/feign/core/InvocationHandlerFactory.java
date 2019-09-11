package com.pseudocode.netflix.feign.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public interface InvocationHandlerFactory {

    InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch);

    interface MethodHandler {

        Object invoke(Object[] argv) throws Throwable;
    }

    static final class Default implements InvocationHandlerFactory {

        @Override
        public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
            return new ReflectiveFeign.FeignInvocationHandler(target, dispatch);
        }
    }
}
