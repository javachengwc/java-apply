package com.hibernate.pseudocode.core;


import com.hibernate.pseudocode.core.stat.Statistics;

import javax.naming.Referenceable;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

public abstract interface SessionFactory extends Referenceable, Serializable
{
    public abstract Session openSession() throws HibernateException;

    public abstract Session openSession(Interceptor paramInterceptor) throws HibernateException;

    public abstract Session openSession(Connection paramConnection);

    public abstract Session openSession(Connection paramConnection, Interceptor paramInterceptor);

    public abstract Session getCurrentSession() throws HibernateException;
    public abstract Map getAllClassMetadata();

    public abstract Map getAllCollectionMetadata();

    public abstract Statistics getStatistics();

    public abstract void close() throws HibernateException;

    public abstract boolean isClosed();


    public abstract Set getDefinedFilterNames();

    public abstract boolean containsFetchProfileDefinition(String paramString);
}
