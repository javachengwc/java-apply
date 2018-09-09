package com.hibernate.pseudocode.core.criterion;

import com.hibernate.pseudocode.core.Criteria;
import com.hibernate.pseudocode.core.HibernateException;
import com.hibernate.pseudocode.core.engine.TypedValue;

import java.io.Serializable;

public abstract interface Criterion extends Serializable
{
    public abstract String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException;

    public abstract TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery)  throws HibernateException;
}
