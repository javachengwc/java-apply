package com.djy.config.finagle;

import com.djy.core.Invoker;
import com.djy.model.SpecUrl;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.builder.ClientConfig;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * finagle client service类
 */
public class FinagleInvoker implements Invoker{

    private static Logger logger = LoggerFactory.getLogger(FinagleInvoker.class);

    private SpecUrl url;

    private Service<ThriftClientRequest, byte[]> service;

    public FinagleInvoker(SpecUrl url) {
        this.url = url;
        initService();
    }

    @Override
    public SpecUrl getUrl() {
        return url;
    }

    @Override
    public Object getProvider() {
        if(service.isAvailable()){
            return service;
        }else{
            logger.error("FinagleInvoker service connection is invalidation,address:["+url.getAddress()+"]");
        }
        return service;
    }

    public void initService(){
        String timeoutStr = url.getParameter("timeout", "30000");
        int timeout = Integer.parseInt(timeoutStr);
        ClientBuilder<ThriftClientRequest, byte[], ClientConfig.Yes, ClientConfig.Yes, ClientConfig.Yes> clientBuilder = ClientBuilder
                .get().codec(ThriftClientFramedCodec.get())
                .hosts(new InetSocketAddress(url.getIp(), url.getPort()))
                .name(url.getServiceName())
                .keepAlive(true)
                .hostConnectionLimit(30)
                .daemon(true)
                .timeout(new Duration(timeout * Duration.NanosPerMillisecond()))
                .retries(3);
        service = ClientBuilder.safeBuild(clientBuilder);
    }
}
