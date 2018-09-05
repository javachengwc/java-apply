package com.spring.pseudocode.aop.aop.framework;

import com.spring.pseudocode.aop.aop.SpringProxy;
import java.io.Serializable;
import java.lang.reflect.Proxy;

/**
 * 在Spring配置文件中配置<aop:aspectj-autoproxy proxy-target-class="true"/>时会默认创建ObjenesisCglibAopProxy
 */
public class DefaultAopProxyFactory implements AopProxyFactory, Serializable {

    //Spring提供了两种代理类创建方式jdk动态代理和cglib动态代理
    @Override
    public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
        //AopProxy创建是根据目标类是否有接口
        if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
            Class<?> targetClass = config.getTargetClass();
            if (targetClass == null) {
                throw new AopConfigException("TargetSource cannot determine target class: " +
                        "Either an interface or a target is required for proxy creation.");
            }
            if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                return new JdkDynamicAopProxy(config);
            }
            return new ObjenesisCglibAopProxy(config);
        }
        else {
            return new JdkDynamicAopProxy(config);
        }
    }

    private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
        Class<?>[] ifcs = config.getProxiedInterfaces();
        return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
    }

}
