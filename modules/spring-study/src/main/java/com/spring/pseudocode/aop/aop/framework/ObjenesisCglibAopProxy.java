package com.spring.pseudocode.aop.aop.framework;

import com.spring.pseudocode.core.cglib.proxy.Callback;
import com.spring.pseudocode.core.cglib.proxy.Enhancer;
import com.spring.pseudocode.core.cglib.proxy.Factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.objenesis.SpringObjenesis;

public class ObjenesisCglibAopProxy extends CglibAopProxy
{
    private static final Log logger = LogFactory.getLog(ObjenesisCglibAopProxy.class);

    private static final SpringObjenesis objenesis = new SpringObjenesis();

    public ObjenesisCglibAopProxy(AdvisedSupport config)
    {
        super(config);
    }

    protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks)
    {
        Class proxyClass = enhancer.createClass();
        Object proxyInstance = null;
        if (objenesis.isWorthTrying()) {
            try {
                proxyInstance = objenesis.newInstance(proxyClass, enhancer.getUseCache());
            }
            catch (Throwable ex) {
                logger.debug("Unable to instantiate proxy using Objenesis, falling back to regular proxy construction", ex);
            }

        }
        if (proxyInstance == null)
        {
            try
            {
                proxyInstance = this.constructorArgs != null ? proxyClass
                        .getConstructor(this.constructorArgTypes)
                        .newInstance(this.constructorArgs) : proxyClass
                        .newInstance();
            }
            catch (Throwable ex) {
                throw new AopConfigException("Unable to instantiate proxy using Objenesis, " +
                        "and regular proxy instantiation via default constructor fails as well", ex);
            }
        }
        ((Factory)proxyInstance).setCallbacks(callbacks);
        return proxyInstance;
    }
}