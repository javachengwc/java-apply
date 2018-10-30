package com.spring.pseudocode.aop.aop.framework.autoproxy;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.FactoryBean;
import org.springframework.aop.TargetSource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * BeanNameAutoProxyCreator是基于Bean配置名规则的自动代理创建器
 * 允许为特定配置名的Bean自动创建代理实例的代理创建器
 */
public class BeanNameAutoProxyCreator extends AbstractAutoProxyCreator
{
    private List<String> beanNames;

    public void setBeanNames(String[] beanNames)
    {
        this.beanNames = new ArrayList(beanNames.length);
        for (String mappedName : beanNames)
            this.beanNames.add(StringUtils.trimWhitespace(mappedName));
    }

    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource)
    {
        if (this.beanNames != null) {
            for (String mappedName : this.beanNames) {
                if (FactoryBean.class.isAssignableFrom(beanClass)) {
                    if (!mappedName.startsWith("&")) {
                        continue;
                    }
                    mappedName = mappedName.substring("&".length());
                }
                if (isMatch(beanName, mappedName)) {
                    return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
                }
                BeanFactory beanFactory = getBeanFactory();
                if (beanFactory != null) {
                    String[] aliases = beanFactory.getAliases(beanName);
                    for (String alias : aliases) {
                        if (isMatch(alias, mappedName)) {
                            return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
                        }
                    }
                }
            }
        }
        return DO_NOT_PROXY;
    }

    protected boolean isMatch(String beanName, String mappedName)
    {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

    public void setBeanFactory(com.spring.pseudocode.beans.factory.BeanFactory beanFactory) throws BeansException {

    }
}
