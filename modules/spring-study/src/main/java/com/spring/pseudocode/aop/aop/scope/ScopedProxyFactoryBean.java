package com.spring.pseudocode.aop.aop.scope;

import com.spring.pseudocode.aop.aop.framework.ProxyConfig;
import com.spring.pseudocode.beans.factory.BeanFactory;
import com.spring.pseudocode.beans.factory.BeanFactoryAware;
import com.spring.pseudocode.beans.factory.FactoryBean;
import com.spring.pseudocode.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.scope.DefaultScopedObject;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.aop.target.SimpleBeanTargetSource;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;

public class ScopedProxyFactoryBean extends ProxyConfig
        implements FactoryBean<Object>, BeanFactoryAware
{
    private final SimpleBeanTargetSource scopedTargetSource = new SimpleBeanTargetSource();
    private String targetBeanName;
    private Object proxy;

    public ScopedProxyFactoryBean()
    {
        setProxyTargetClass(true);
    }

    public void setTargetBeanName(String targetBeanName)
    {
        this.targetBeanName = targetBeanName;
        this.scopedTargetSource.setTargetBeanName(targetBeanName);
    }

    public void setBeanFactory(BeanFactory beanFactory)
    {
        if (!(beanFactory instanceof ConfigurableBeanFactory)) {
            throw new IllegalStateException("Not running in a ConfigurableBeanFactory: " + beanFactory);
        }
        ConfigurableBeanFactory cbf = (ConfigurableBeanFactory)beanFactory;

        //this.scopedTargetSource.setBeanFactory(beanFactory);

        ProxyFactory pf = new ProxyFactory();
//        pf.copyFrom(this);
        //设置Bean的来源，这个方法决定了每次取target的时候，都会调用beanFactory.getBean。
//        pf.setTargetSource(this.scopedTargetSource);

        Class beanType = beanFactory.getType(this.targetBeanName);
        if (beanType == null) {
            throw new IllegalStateException("Cannot create scoped proxy for bean '" + this.targetBeanName +
                    "': Target type could not be determined at the time of proxy creation.");
        }

        if ((!isProxyTargetClass()) || (beanType.isInterface()) || (Modifier.isPrivate(beanType.getModifiers()))) {
            pf.setInterfaces(ClassUtils.getAllInterfacesForClass(beanType, cbf.getBeanClassLoader()));
        }

        //ScopedObject scopedObject = new DefaultScopedObject(cbf, this.scopedTargetSource.getTargetBeanName());
        ScopedObject scopedObject =null;
        pf.addAdvice(new DelegatingIntroductionInterceptor(scopedObject));
        //将scopedObject作为通知加入到proxy中，DelegatingIntroductionInterceptor作为通知的拦截器，
        //使得所有在proxyBean上调用的getTargetObject方法都被代理到了DefaultScopedObject中。
        //在增加通知的同时会生成DefaultIntroductionAdvisor为advisor，
        pf.addInterface(AopInfrastructureBean.class);
        this.proxy = pf.getProxy(cbf.getBeanClassLoader());
    }

    public Object getObject()
    {
        if (this.proxy == null) {
            throw new FactoryBeanNotInitializedException();
        }
        return this.proxy;
    }

    public Class<?> getObjectType()
    {
        if (this.proxy != null) {
            return this.proxy.getClass();
        }
        return this.scopedTargetSource.getTargetClass();
    }

    public boolean isSingleton()
    {
        return true;
    }
}