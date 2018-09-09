package com.hibernate.pseudocode.core;


import com.hibernate.pseudocode.core.criterion.Criterion;
import com.hibernate.pseudocode.core.criterion.Order;
import com.hibernate.pseudocode.core.criterion.Projection;
import com.hibernate.pseudocode.core.transform.ResultTransformer;

import java.util.List;

public abstract interface Criteria
{
    public abstract String getAlias();

    public abstract Criteria setProjection(Projection projection);

    public abstract Criteria add(Criterion criterion);

    public abstract Criteria addOrder(Order order);

    public abstract Criteria setFetchMode(String param, FetchMode fetchMode) throws HibernateException;

    public abstract Criteria createAlias(String string1, String string2) throws HibernateException;

    public abstract Criteria createAlias(String string1, String string2, int paramInt) throws HibernateException;

    public abstract Criteria createAlias(String string1, String string2, int paramInt, Criterion criterion) throws HibernateException;

    public abstract Criteria createCriteria(String param) throws HibernateException;

    public abstract Criteria createCriteria(String param, int paramInt) throws HibernateException;

    public abstract Criteria createCriteria(String string1, String string2) throws HibernateException;

    public abstract Criteria createCriteria(String string1, String string2, int paramInt) throws HibernateException;

    public abstract Criteria createCriteria(String string1, String string2, int paramInt, Criterion criterion) throws HibernateException;

    public abstract Criteria setResultTransformer(ResultTransformer resultTransformer);

    public abstract Criteria setMaxResults(int paramInt);

    public abstract Criteria setFirstResult(int paramInt);

    public abstract boolean isReadOnlyInitialized();

    public abstract boolean isReadOnly();

    public abstract Criteria setReadOnly(boolean paramBoolean);

    public abstract Criteria setFetchSize(int paramInt);

    public abstract Criteria setTimeout(int paramInt);

    public abstract Criteria setCacheable(boolean paramBoolean);

    public abstract Criteria setCacheRegion(String param);

    public abstract Criteria setComment(String param);

    //../

    public abstract List list() throws HibernateException;


    public abstract Object uniqueResult() throws HibernateException;
}