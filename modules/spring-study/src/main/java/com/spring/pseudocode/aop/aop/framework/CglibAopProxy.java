package com.spring.pseudocode.aop.aop.framework;

import com.spring.pseudocode.core.cglib.proxy.Callback;
import com.spring.pseudocode.core.cglib.proxy.Enhancer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.SmartClassLoader;
import org.springframework.util.ClassUtils;

import java.io.Serializable;

//cglib代理
public class CglibAopProxy implements AopProxy, Serializable
{
    //...
    protected static final Log logger = LogFactory.getLog(CglibAopProxy.class);

    protected final AdvisedSupport advised;
    protected Object[] constructorArgs;
    protected Class<?>[] constructorArgTypes;

    public CglibAopProxy(AdvisedSupport config)  throws AopConfigException
    {
        if ((config.getAdvisors().length == 0) && (config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE)) {
            throw new AopConfigException("No advisors and no TargetSource specified");
        }
        this.advised = config;
        //this.advisedDispatcher = new AdvisedDispatcher(this.advised);
    }

    public void setConstructorArguments(Object[] constructorArgs, Class<?>[] constructorArgTypes)
    {
        if ((constructorArgs == null) || (constructorArgTypes == null)) {
            throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' need to be specified");
        }
        if (constructorArgs.length != constructorArgTypes.length) {
            throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length +
                    ") must match number of 'constructorArgTypes' (" + constructorArgTypes.length + ")");
        }

        this.constructorArgs = constructorArgs;
        this.constructorArgTypes = constructorArgTypes;
    }

    public Object getProxy()
    {
        return getProxy(null);
    }

    public Object getProxy(ClassLoader classLoader)
    {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetSource());
        }
        try
        {
            Class rootClass = this.advised.getTargetClass();
            Class proxySuperClass = rootClass;
            if (ClassUtils.isCglibProxyClass(rootClass)) {
                proxySuperClass = rootClass.getSuperclass();
                Class[] additionalInterfaces = rootClass.getInterfaces();
                for (Class additionalInterface : additionalInterfaces) {
                    this.advised.addInterface(additionalInterface);
                }

            }

//            validateClassIfNecessary(proxySuperClass, classLoader);

            Enhancer enhancer = createEnhancer();
            if (classLoader != null) {
                enhancer.setClassLoader(classLoader);
                if (((classLoader instanceof SmartClassLoader)) &&
                        (((SmartClassLoader)classLoader)
                                .isClassReloadable(proxySuperClass)))
                {
                    enhancer.setUseCache(false);
                }
            }
//            enhancer.setSuperclass(proxySuperClass);
//            enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
//            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
//            enhancer.setStrategy(new ClassLoaderAwareUndeclaredThrowableStrategy(classLoader));

//            Callback[] callbacks = getCallbacks(rootClass);
            Callback[] callbacks =null;
            Class[] types = new Class[callbacks.length];
            for (int x = 0; x < types.length; x++) {
                types[x] = callbacks[x].getClass();
            }
//
//            enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised
//                            .getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
//            enhancer.setCallbackTypes(types);
//            return createProxyClassAndInstance(enhancer, callbacks);
            return  null;
        }
        catch (Exception ee)
        {
            throw new AopConfigException("Unexpected AOP exception", ee);
        }
    }

    protected Enhancer createEnhancer()
    {
        return new Enhancer();
    }

}
