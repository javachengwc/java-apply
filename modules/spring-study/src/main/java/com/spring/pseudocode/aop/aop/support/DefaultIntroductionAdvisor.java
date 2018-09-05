package com.spring.pseudocode.aop.aop.support;

import com.spring.pseudocode.aop.aop.ClassFilter;
import com.spring.pseudocode.aop.aop.DynamicIntroductionAdvice;
import com.spring.pseudocode.aop.aop.IntroductionAdvisor;
import com.spring.pseudocode.aop.aop.IntroductionInfo;
import com.spring.pseudocode.aop.aopalliance.aop.Advice;
import com.spring.pseudocode.core.core.Ordered;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultIntroductionAdvisor implements IntroductionAdvisor, ClassFilter, Ordered, Serializable
{
    private final Advice advice;
    private final Set<Class<?>> interfaces = new LinkedHashSet();

    private int order = 2147483647;

    public DefaultIntroductionAdvisor(Advice advice)
    {
        this(advice, (advice instanceof IntroductionInfo) ? (IntroductionInfo)advice : null);
    }

    public DefaultIntroductionAdvisor(Advice advice, IntroductionInfo introductionInfo)
    {
        this.advice = advice;
        if (introductionInfo != null) {
            Class[] introducedInterfaces = introductionInfo.getInterfaces();
            if (introducedInterfaces.length == 0) {
                throw new IllegalArgumentException("IntroductionAdviceSupport implements no interfaces");
            }
            for (Class ifc : introducedInterfaces)
                addInterface(ifc);
        }
    }

    public DefaultIntroductionAdvisor(DynamicIntroductionAdvice advice, Class<?> intf)
    {
        this.advice = advice;
        addInterface(intf);
    }

    public void addInterface(Class<?> intf)
    {
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("Specified class [" + intf.getName() + "] must be an interface");
        }
        this.interfaces.add(intf);
    }

    public Class<?>[] getInterfaces()
    {
        return (Class[])this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    public void validateInterfaces() throws IllegalArgumentException
    {
        for (Class ifc : this.interfaces)
            if (((this.advice instanceof DynamicIntroductionAdvice)) &&
                    (!((DynamicIntroductionAdvice)this.advice)
                            .implementsInterface(ifc)))
            {
                throw new IllegalArgumentException("DynamicIntroductionAdvice [" + this.advice + "] does not implement interface [" + ifc
                        .getName() + "] specified for introduction");
            }
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public int getOrder()
    {
        return this.order;
    }

    public Advice getAdvice()
    {
        return this.advice;
    }

    public boolean isPerInstance()
    {
        return true;
    }

    public ClassFilter getClassFilter()
    {
        return this;
    }

    public boolean matches(Class<?> clazz)
    {
        return true;
    }

    public int hashCode()
    {
        return this.advice.hashCode() * 13 + this.interfaces.hashCode();
    }
}