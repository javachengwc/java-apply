package com.hibernate.pseudocode.core.criterion;

import com.hibernate.pseudocode.core.Criteria;
import com.hibernate.pseudocode.core.HibernateException;
import com.hibernate.pseudocode.core.type.Type;

import java.io.Serializable;

public abstract interface Projection extends Serializable
{
    public abstract String toSqlString(Criteria criteria, int paramInt, CriteriaQuery criteriaQuery) throws HibernateException;

    public abstract String toGroupSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException;

    public abstract Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException;

    public abstract Type[] getTypes(String param, Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException;

    public abstract String[] getColumnAliases(int paramInt);

    public abstract String[] getColumnAliases(String param, int paramInt);

    public abstract String[] getAliases();

    public abstract boolean isGrouped();
}
