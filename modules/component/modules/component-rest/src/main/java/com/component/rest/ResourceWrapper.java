package com.component.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ResourceWrapper implements InvocationHandler {

    private static Logger logger= LoggerFactory.getLogger(ResourceWrapper.class);

	protected Object obj;

    public ResourceWrapper(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getTargetException();
            String methodName= method.getName();
            boolean isWebApplicationException=(throwable instanceof WebApplicationException);
            logger.error("ResourceWrapper invoke error.method={},isWebApplicationException={}",
                    methodName,isWebApplicationException,throwable);
            throw throwable;
        }
    }
}
