package com.spring.pseudocode.beans.factory.config;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class BeanDefinitionHolder
{
    private final BeanDefinition beanDefinition;
    private final String beanName;
    private final String[] aliases;

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName)
    {
        this(beanDefinition, beanName, null);
    }

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName, String[] aliases)
    {
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
        this.aliases = aliases;
    }

    public BeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder)
    {
        this.beanDefinition = beanDefinitionHolder.getBeanDefinition();
        this.beanName = beanDefinitionHolder.getBeanName();
        this.aliases = beanDefinitionHolder.getAliases();
    }

    public BeanDefinition getBeanDefinition()
    {
        return this.beanDefinition;
    }

    public String getBeanName()
    {
        return this.beanName;
    }

    public String[] getAliases()
    {
        return this.aliases;
    }

    public Object getSource()
    {
        return this.beanDefinition.getSource();
    }

    public boolean matchesName(String candidateName)
    {
        return (candidateName != null) && ((candidateName.equals(this.beanName)) ||
                (candidateName
                        .equals(BeanFactoryUtils.transformedBeanName(this.beanName))) ||
                (ObjectUtils.containsElement(this.aliases, candidateName)));
    }

    public String getShortDescription()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Bean definition with name '").append(this.beanName).append("'");
        if (this.aliases != null) {
            sb.append(" and aliases [").append(StringUtils.arrayToCommaDelimitedString(this.aliases)).append("]");
        }
        return sb.toString();
    }

    public String getLongDescription()
    {
        StringBuilder sb = new StringBuilder(getShortDescription());
        sb.append(": ").append(this.beanDefinition);
        return sb.toString();
    }

    public String toString()
    {
        return getLongDescription();
    }

    public boolean equals(Object other)
    {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BeanDefinitionHolder)) {
            return false;
        }
        BeanDefinitionHolder otherHolder = (BeanDefinitionHolder)other;
        return (this.beanDefinition.equals(otherHolder.beanDefinition)) &&
                (this.beanName
                        .equals(otherHolder.beanName)) &&
                (ObjectUtils.nullSafeEquals(this.aliases, otherHolder.aliases));
    }

    public int hashCode()
    {
        int hashCode = this.beanDefinition.hashCode();
        hashCode = 29 * hashCode + this.beanName.hashCode();
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.aliases);
        return hashCode;
    }
}