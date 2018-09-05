package com.spring.pseudocode.aop.aop.support;

import com.spring.pseudocode.aop.aop.PointcutAdvisor;
import com.spring.pseudocode.aop.aopalliance.aop.Advice;
import com.spring.pseudocode.core.core.Ordered;

import java.io.Serializable;

public abstract class AbstractPointcutAdvisor implements PointcutAdvisor, Ordered, Serializable
{
    private Integer order;

    public void setOrder(int order)
    {
        this.order = Integer.valueOf(order);
    }

    public int getOrder()
    {
        if (this.order != null) {
            return this.order.intValue();
        }
        Advice advice = getAdvice();
        if ((advice instanceof Ordered)) {
            return ((Ordered)advice).getOrder();
        }
        return 2147483647;
    }

    public boolean isPerInstance()
    {
        return true;
    }

    public int hashCode()
    {
        return Serializable.class.hashCode();
    }
}
