package com.djy.config.finagle;

import com.djy.model.Provider;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.util.Duration;
import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Function0;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * finagle服务处理类
 */
public class FinagleServerHandler  implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(FinagleServerHandler.class);

    //服务提供者
    private Provider provider;

    //对应的springBean对象
    private Object service;

    //线程池
    private ExecutorServiceFuturePool futurePool = null;

    public FinagleServerHandler(Provider provider,Object service)
    {
        this.provider=provider;
        this.service=service;
    }

    public FinagleServerHandler(Object service, String id, String api, Integer port, String version, int timeout,Integer threads) {
        this.provider= new Provider(id,api,port,version,timeout,threads);
        this.service = service;
    }

    //启动服务
    public void start() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        //thirft接口类
        Class thriftApi = Class.forName(provider.getApi());
        //提供服务的接口
        Class serviceIface = null;
        Class serviceImpl = null;
        //启动服务的实现类
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

        //多线程
        ExecutorService executorService = Executors.newFixedThreadPool(provider.getThreads());
        futurePool = new ExecutorServiceFuturePool(executorService);

        //ServiceToClient的代理类
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), serviceImpl.getInterfaces(), this);
        //Service的构造方法
        Constructor construct = serviceClass.getConstructor(serviceIface, TProtocolFactory.class);
        TBinaryProtocol.Factory factory = new TBinaryProtocol.Factory();
        //service实例
        Service service = (Service)construct.newInstance(proxy, factory);
        //service绑定参数
        ServerBuilder serverBuilder = ServerBuilder.get()
                .name(provider.getServiceName())
                .codec(ThriftServerFramedCodec.get())
                .requestTimeout(new Duration(provider.getTimeout() * Duration.NanosPerMillisecond()))//请求超时时间
                .keepAlive(true)
                .bindTo(new InetSocketAddress(provider.getPort()));

        ServerBuilder.safeBuild(service, serverBuilder);
        logger.info("FinagleServerHandler start thrift[" + provider.getApi() + "]启动成功,provider:["+provider+"]");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        invokeBefore();
        Object result = null;
        try {
            result = futurePool.apply(new FinagleProxy(method.getName(), args));

        } catch (Exception ex) {
            logger.error("线程["+Thread.currentThread().getName()+"]执行 FinagleServerHandler invoke error,服务:[" + provider.getId() + "]被调用失败。", ex);
            //throw ex;
        }
        invokeAfter();
        return result;
    }

    private void invokeBefore() {

    }

    private void invokeAfter() {

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
                result =  org.apache.commons.lang.reflect.MethodUtils.invokeMethod(FinagleServerHandler.this.service, this.methodName, this.params, paramsClass);
            }
            catch (Exception e) {
                FinagleServerHandler.logger.error("线程["+Thread.currentThread().getName()+"]执行 FinagleServerHandler FinagleProxy apply error,服务[" + FinagleServerHandler.this.provider.getId()+ "]被调用失败。", e);
            }
            return result;
        }
    }
}
