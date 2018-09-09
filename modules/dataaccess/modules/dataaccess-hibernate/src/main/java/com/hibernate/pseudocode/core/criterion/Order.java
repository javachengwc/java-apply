package com.hibernate.pseudocode.core.criterion;


import com.hibernate.pseudocode.core.Criteria;
import com.hibernate.pseudocode.core.HibernateException;
import com.hibernate.pseudocode.core.type.Type;

import java.io.Serializable;

public class Order implements Serializable
{
    private boolean ascending;
    private boolean ignoreCase;
    private String propertyName;

    public String toString()
    {
        return this.propertyName + ' ' + (this.ascending ? "asc" : "desc");
    }

    public Order ignoreCase() {
        this.ignoreCase = true;
        return this;
    }

    protected Order(String propertyName, boolean ascending)
    {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException
    {
//        String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, this.propertyName);
//        Type type = criteriaQuery.getTypeUsingProjection(criteria, this.propertyName);
        StringBuffer fragment = new StringBuffer();
//        for (int i = 0; i < columns.length; i++) {
//            SessionFactoryImplementor factory = criteriaQuery.getFactory();
//            boolean lower = (this.ignoreCase) && (type.sqlTypes(factory)[i] == 12);
//            if (lower) {
//                fragment.append(factory.getDialect().getLowercaseFunction()).append('(');
//            }
//
//            fragment.append(columns[i]);
//            if (lower) fragment.append(')');
//            fragment.append(this.ascending ? " asc" : " desc");
//            if (i >= columns.length - 1) continue; fragment.append(", ");
//        }
        return fragment.toString();
    }

    public static Order asc(String propertyName)
    {
        return new Order(propertyName, true);
    }

    public static Order desc(String propertyName)
    {
        return new Order(propertyName, false);
    }
}
