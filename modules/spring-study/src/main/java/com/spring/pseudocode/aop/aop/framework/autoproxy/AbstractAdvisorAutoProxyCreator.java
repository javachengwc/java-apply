package com.spring.pseudocode.aop.aop.framework.autoproxy;


import com.spring.pseudocode.aop.aop.Advisor;
import com.spring.pseudocode.aop.aop.support.AopUtils;
import com.spring.pseudocode.beans.factory.BeanFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;

public abstract class AbstractAdvisorAutoProxyCreator extends AbstractAutoProxyCreator
{
    private BeanFactoryAdvisorRetrievalHelper advisorRetrievalHelper;

    public void setBeanFactory(BeanFactory beanFactory)
    {
        //super.setBeanFactory(beanFactory);
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalStateException("Cannot use AdvisorAutoProxyCreator without a ConfigurableListableBeanFactory");
        }
        initBeanFactory((ConfigurableListableBeanFactory)beanFactory);
    }

    protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.advisorRetrievalHelper = new BeanFactoryAdvisorRetrievalHelperAdapter(beanFactory);
    }

    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource)
    {
        List advisors = findEligibleAdvisors(beanClass, beanName);
        if (advisors.isEmpty()) {
            return DO_NOT_PROXY;
        }
        return advisors.toArray();
    }

    //findEligibleAdvisors()方法获取当前对象的Advisors
    protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName)
    {
        //findCandidateAdvisors()方法找到Spring中所有的Advisor
        List candidateAdvisors = findCandidateAdvisors();
        //findAdvisorsThatCanApply()方法过滤出适合当前对象的advisors.
        List eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
        extendAdvisors(eligibleAdvisors);
        if (!eligibleAdvisors.isEmpty()) {
            //advisor执行先后顺序是由“每个切面的order属性”而定的，order越小，则该该切面中的通知越先被执行。
            eligibleAdvisors = sortAdvisors(eligibleAdvisors);
        }
        return eligibleAdvisors;
    }

    protected List<Advisor> findCandidateAdvisors()
    {
        //return this.advisorRetrievalHelper.findAdvisorBeans();
        return null;
    }

    protected List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName)
    {
        //ProxyCreationContext.setCurrentProxiedBeanName(beanName);
        try {
            //遍历所有的advisor。查看当前advisor的pointCut是否适用于当前对象，如果是，加入候选队列，否则跳过
            List localList = AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
            return localList;
        }
        finally {
            //ProxyCreationContext.setCurrentProxiedBeanName(null);
        }
    }

    protected boolean isEligibleAdvisorBean(String beanName)
    {
        return true;
    }

    //advisor执行先后顺序是由“每个切面的order属性”而定的，order越小，则该该切面中的通知越先被执行。
    protected List<Advisor> sortAdvisors(List<Advisor> advisors)
    {
        AnnotationAwareOrderComparator.sort(advisors);
        return advisors;
    }

    protected void extendAdvisors(List<Advisor> candidateAdvisors)
    {

    }

    protected boolean advisorsPreFiltered()
    {
        return true;
    }

    private class BeanFactoryAdvisorRetrievalHelperAdapter extends BeanFactoryAdvisorRetrievalHelper
    {
        public BeanFactoryAdvisorRetrievalHelperAdapter(ConfigurableListableBeanFactory beanFactory)
        {

            super(beanFactory);
        }

        protected boolean isEligibleBean(String beanName)
        {
            return AbstractAdvisorAutoProxyCreator.this.isEligibleAdvisorBean(beanName);
        }
    }
}
