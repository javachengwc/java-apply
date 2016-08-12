package com.djy.config.finagle;

import com.djy.config.ReferenceBean;
import com.djy.constant.Constant;
import com.djy.core.Invoker;
import com.djy.model.Consumer;
import com.djy.model.SpecUrl;
import com.twitter.finagle.Service;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Future;
import com.util.net.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * finagle异步调用处理类
 */
public class FinagleAsynInvokeHandler implements InvocationHandler {

    private static Logger logger = LoggerFactory.getLogger(FinagleAsynInvokeHandler.class);

    //thirft接口的ServiceToClient类
    private Class clientClass;

    //clientClass的构造函数
    private Constructor constructor = null;

    private Invoker invoker;

    //消费者信息
    private Consumer consumer;

    public FinagleAsynInvokeHandler(Consumer consumer) {
        this.consumer=consumer;
        if (!StringUtils.isBlank(consumer.getDirecturl())) {
            String serviceName = consumer.getSid();
            if (StringUtils.isBlank(serviceName)) {
                //临时名称
                serviceName = "tmpServiceName";
                logger.error("FinagleAsynInvokeHandler create sid is null,提供临时名称"+serviceName+"以便可调用");
            }
            String invokeUrl = consumer.getDirecturl()+ "/" + serviceName + "?timeout=" + consumer.getTimeout();
            invoker = new FinagleInvoker(SpecUrl.valueOf(invokeUrl));
        }
        init(consumer.getApi());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        Object result = null;
        invokeBefore();
        final String serviceName =consumer.getSid();
        String surl = null;
        try {
            Service<ThriftClientRequest, byte[]> service = null;
            //thirft客户端对象，类似XXService.ServiceToClient client
            Object target = null;
            if (invoker != null) {
                service = (Service<ThriftClientRequest, byte[]>) invoker.getProvider();
                surl = invoker.getUrl().toUrlStr();
            } else {
                SpecUrl routeUrl =ReferenceBean.getRouter(serviceName);
                if (routeUrl!=null && !"false".equals(routeUrl.getParameter(Constant.USEABLE_KEY, "true"))) {
                    Invoker invoker = ReferenceBean.getProvider(serviceName);
                    surl = invoker.getUrl().toUrlStr();
                    service = (Service<ThriftClientRequest, byte[]>) invoker.getProvider();
                }
            }
            if(service==null)
            {
                throw new RuntimeException("FinagleAsynInvokeHandler invoke finagle service is null");
            }
            //获取客户端对象
            //类似XXService.ServiceToClient client = new XX.ServiceToClient(service, new TBinaryProtocol.Factory());
            target = constructor.newInstance(service, new TBinaryProtocol.Factory());
            //调用原始对象的方法
            result = method.invoke(target, args);
            final String serviceUrl =surl;
            if (result instanceof Future) {
                ((Future)result).addEventListener(
                    new InvokeListener(NetUtil.getLocalHost(),consumer.getId(),serviceName,serviceUrl,consumer.getApi(),consumer.getVersion(),clientClass.getName(),method.getName() )
                    {
                        //res为该方法的返回值, 可拿到该值做后续的业务处理
                        public void onSuccess(Object res) {
                            //如果需要记录成功数,可在此记数
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            logger.error("FinagleAsynInvokeHandler invoke 消费者调用服务:["+ serviceName+ "],thrift api:[" + consumer.getApi() + "]失败, 服务提供者url:[" + serviceUrl + "]", throwable);
                        }
                    });
            }
        } catch (Exception e) {
            logger.error("FinagleAsynInvokeHandler invoke 消费者调用服务:["+ serviceName+ "],thrift api:[" + consumer.getApi() + "]失败, 服务提供者url:[" + surl + "]", e);
            //throw e;
        }
        invokeAfter();
        return result;
    }

    //获取目标对象的代理对象
    public Object getProxy() throws Exception {
        return Proxy.newProxyInstance(clientClass.getClassLoader(),clientClass.getInterfaces(), this);
    }

    public void init(String api) {
        try {
            Class thriftApi = Class.forName(api);
            for (Class c : thriftApi.getDeclaredClasses()) {
                if (c.getSimpleName().equals("ServiceToClient")) {
                    clientClass = c;
                    break;
                }
            }
            constructor = clientClass.getConstructor(com.twitter.finagle.Service.class, TProtocolFactory.class);
        } catch (Exception e) {
            logger.error("FinagleAsynInvokeHandler init api:["+api+"] error, 获取[" + consumer.getSid() + "]服务 thrift api [" + api + "]的客户端ServiceToClient class类与构造函数失败", e);
        }
    }

    private void invokeBefore() {

    }

    private void invokeAfter() {

    }

}