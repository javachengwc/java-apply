package com.thrift.server;

import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Function0;

import java.lang.reflect.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinagleServerHandler implements InvocationHandler
{
    private static final Logger logger = LoggerFactory.getLogger(FinagleServerHandler.class);

    private ExecutorServiceFuturePool futurePool = null;

    private Object service;

    private String api = "";

    private Integer threads = Integer.valueOf(10);

    private ServerBuilder serverBuilder;

    public FinagleServerHandler(Object service, String api, ServerBuilder serverBuilder)
    {
        this.service = service;
        this.api = api;
        this.serverBuilder=serverBuilder;

    }

    public void start() throws Exception
    {
        Class thriftApi = Class.forName(this.api);

        Class serviceIface = null;
        Class serviceImpl = null;

        Class serviceClass = null;
        for (Class c : thriftApi.getDeclaredClasses()) {
            if ("Service".equals(c.getSimpleName())) {
                serviceClass = c;
            }
            if ("ServiceIface".equals(c.getSimpleName())) {
                serviceIface = c;
            }
            if ("ServiceToClient".equals(c.getSimpleName())) {
                serviceImpl = c;
            }
        }

        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceImpl.getInterfaces(), this);

        Constructor con = serviceClass.getConstructor(new Class[] { serviceIface, TProtocolFactory.class });

        Service service = (Service)con.newInstance(new Object[] { proxy, new TBinaryProtocol.Factory() });

        ExecutorService executorService = Executors.newFixedThreadPool(this.threads.intValue());
        this.futurePool = new ExecutorServiceFuturePool(executorService);

        ServerBuilder.safeBuild(service, serverBuilder);
        logger.info("Finagle thrift [" + this.api + "] 启动成功 !!!!");
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        invokeBefore();
        Object result = null;
        try {
            result = this.futurePool.apply(new FinagleProxy(method.getName(), args));
        }
        catch (Exception ex) {
            logger.error("服务提供者,提供的服务名:[" + this.api + "] 被调用失败 !!!!", ex);
            throw ex;
        }

        invokeAfter();
        return result;
    }

    private void invokeBefore()
    {
    }

    private void invokeAfter()
    {
    }

    private class FinagleProxy extends Function0
    {
        private Object[] params;
        private String methodName;

        public FinagleProxy(String methodName, Object[] params)
        {
            this.methodName = methodName;
            this.params = params;
        }

        public Object apply()
        {
            Object result = null;
            Class[] paramsClass = null;
            try {
                if ((this.params != null) && (this.params.length > 0)) {
                    paramsClass = new Class[this.params.length];
                    for (int i = 0; i < this.params.length; i++)
                    {
                        Object paramObj = this.params[i];
                        if (paramObj != null)
                            paramsClass[i] = paramObj.getClass();
                        else {
                            paramsClass[i] = null;
                        }
                    }
                }

                result = MethodUtils.invokeMethod(FinagleServerHandler.this.service, this.methodName, this.params, paramsClass);

            }
            catch (Exception e) {
                FinagleServerHandler.logger.error("服务提供者,提供的服务名:[" + FinagleServerHandler.this.api + "] 被调用失败 !!!!", e);

            }
            return result;
        }
    }
}