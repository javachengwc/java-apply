package com.cloud.pseudocode.ribbon;

import com.cloud.pseudocode.context.named.NamedContextFactory;
import com.cloud.pseudocode.ribbon.core.client.IClient;
import com.cloud.pseudocode.ribbon.core.client.IClientConfigAware;
import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.ILoadBalancer;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Constructor;

public class SpringClientFactory extends NamedContextFactory<RibbonClientSpecification> {

    static final String NAMESPACE = "ribbon";

    public SpringClientFactory() {
        super(RibbonClientConfiguration.class, NAMESPACE, "ribbon.client.name");
    }

    public <C extends IClient<?, ?>> C getClient(String name, Class<C> clientClass) {
        return getInstance(name, clientClass);
    }

    public ILoadBalancer getLoadBalancer(String name) {
        return getInstance(name, ILoadBalancer.class);
    }

    public IClientConfig getClientConfig(String name) {
        return getInstance(name, IClientConfig.class);
    }

    public RibbonLoadBalancerContext getLoadBalancerContext(String serviceId) {
        return getInstance(serviceId, RibbonLoadBalancerContext.class);
    }

    static <C> C instantiateWithConfig(Class<C> clazz, IClientConfig config) {
        return instantiateWithConfig(null, clazz, config);
    }

    static <C> C instantiateWithConfig(AnnotationConfigApplicationContext context,
                                       Class<C> clazz, IClientConfig config) {
        C result = null;

        try {
            Constructor<C> constructor = clazz.getConstructor(IClientConfig.class);
            result = constructor.newInstance(config);
        } catch (Throwable e) {
            // Ignored
        }

        if (result == null) {
            result = BeanUtils.instantiate(clazz);

            if (result instanceof IClientConfigAware) {
                ((IClientConfigAware) result).initWithNiwsConfig(config);
            }

            if (context != null) {
                context.getAutowireCapableBeanFactory().autowireBean(result);
            }
        }

        return result;
    }

    @Override
    public <C> C getInstance(String name, Class<C> type) {
        C instance = super.getInstance(name, type);
        if (instance != null) {
            return instance;
        }
        IClientConfig config = getInstance(name, IClientConfig.class);
        return instantiateWithConfig(getContext(name), type, config);
    }

    @Override
    protected AnnotationConfigApplicationContext getContext(String name) {
        return super.getContext(name);
    }

}
