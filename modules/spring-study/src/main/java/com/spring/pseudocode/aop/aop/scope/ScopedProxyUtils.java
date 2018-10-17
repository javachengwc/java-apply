package com.spring.pseudocode.aop.aop.scope;


import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import com.spring.pseudocode.beans.factory.config.BeanDefinitionHolder;
import com.spring.pseudocode.beans.factory.support.AbstractBeanDefinition;
import com.spring.pseudocode.beans.factory.support.BeanDefinitionRegistry;
import com.spring.pseudocode.beans.factory.support.RootBeanDefinition;

public abstract class ScopedProxyUtils
{
    private static final String TARGET_NAME_PREFIX = "scopedTarget.";

    public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definition,
         BeanDefinitionRegistry registry, boolean proxyTargetClass)
    {
        String originalBeanName = definition.getBeanName();
        BeanDefinition targetDefinition = definition.getBeanDefinition();
        String targetBeanName = getTargetBeanName(originalBeanName);

        //创建一个ScopedProxyFactoryBean，这个Bean中保存了目标Bean的名称,
        //同时在内部保存了目标Bean定义的引用。注意并没有对BeanDefinition设置scope,因此这个代理bean的scope就默认是singleton了。
        RootBeanDefinition proxyDefinition = new RootBeanDefinition(ScopedProxyFactoryBean.class);
        proxyDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, targetBeanName));
        //proxyDefinition.setOriginatingBeanDefinition(targetDefinition);
        proxyDefinition.setSource(definition.getSource());
        //proxyDefinition.setRole(targetDefinition.getRole());

        proxyDefinition.getPropertyValues().add("targetBeanName", targetBeanName);
        if (proxyTargetClass) {
            //targetDefinition.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
        }
        else
        {
            proxyDefinition.getPropertyValues().add("proxyTargetClass", Boolean.FALSE);
        }

        proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
        proxyDefinition.setPrimary(targetDefinition.isPrimary());
        if ((targetDefinition instanceof AbstractBeanDefinition)) {
            proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition)targetDefinition);
        }
        targetDefinition.setAutowireCandidate(false);
        targetDefinition.setPrimary(false);
        // 在后面还是会用到目标Bean，因此也需要将它的定义注册到Registry中
        registry.registerBeanDefinition(targetBeanName, targetDefinition);
        return new BeanDefinitionHolder(proxyDefinition, originalBeanName, definition.getAliases());
    }

    public static String getTargetBeanName(String originalBeanName)
    {
        return "scopedTarget." + originalBeanName;
    }

    public static boolean isScopedTarget(String beanName)
    {
        return (beanName != null) && (beanName.startsWith("scopedTarget."));
    }
}
