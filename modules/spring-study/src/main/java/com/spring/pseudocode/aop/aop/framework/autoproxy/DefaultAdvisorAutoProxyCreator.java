package com.spring.pseudocode.aop.aop.framework.autoproxy;


import com.spring.pseudocode.beans.factory.BeanNameAware;

/**
 * Advisor是切点和增强的复合体，Advisor本身已经包含了足够的信息：横切逻辑（要织入什么）以及连接点（织入到哪里）。
 * DefaultAdvisorAutoProxyCreator能够扫描容器中的Advisor，并将Advisor自动织入到匹配的目标Bean中，即为匹配的目标Bean自动创建代理。
 */
public class DefaultAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator implements BeanNameAware
{
    public static final String SEPARATOR = ".";

    private boolean usePrefix = false;

    private String advisorBeanNamePrefix;

    public void setUsePrefix(boolean usePrefix)
    {
        this.usePrefix = usePrefix;
    }

    public boolean isUsePrefix()
    {
        return this.usePrefix;
    }

    public void setAdvisorBeanNamePrefix(String advisorBeanNamePrefix)
    {
        this.advisorBeanNamePrefix = advisorBeanNamePrefix;
    }

    public String getAdvisorBeanNamePrefix()
    {
        return this.advisorBeanNamePrefix;
    }

    public void setBeanName(String name)
    {
        if (this.advisorBeanNamePrefix == null)
            this.advisorBeanNamePrefix = (name + ".");
    }

    protected boolean isEligibleAdvisorBean(String beanName)
    {
        return (!isUsePrefix()) || (beanName.startsWith(getAdvisorBeanNamePrefix()));
    }

}
