package com.app.aop;

import com.util.aop.Aop;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceAop implements Aop {

    //调用次数
    private static AtomicLong invokeCount= new AtomicLong(0l);

    @Override
    public void before(Method method, Object[] args) throws Exception {
        invokeCount.addAndGet(1l);
    }

    @Override
    public void after(Method method, Object[] args) throws Exception {

    }

    public static Long getServiceIncokeCount() {
        return invokeCount.get();
    }
}