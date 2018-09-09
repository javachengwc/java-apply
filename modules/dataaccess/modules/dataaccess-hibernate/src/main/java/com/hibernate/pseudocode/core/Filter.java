package com.hibernate.pseudocode.core;

import java.util.Collection;

public abstract interface Filter
{
    public abstract String getName();

    //...

    public abstract Filter setParameter(String param, Object object);

    public abstract Filter setParameterList(String param, Collection collection);

    public abstract Filter setParameterList(String param, Object[] arrayObject);

    public abstract void validate() throws HibernateException;
}
