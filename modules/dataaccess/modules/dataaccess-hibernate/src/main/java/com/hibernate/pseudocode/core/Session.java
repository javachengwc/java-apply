package com.hibernate.pseudocode.core;


import java.io.Serializable;
import java.sql.Connection;

public abstract interface Session extends Serializable
{
    public abstract EntityMode getEntityMode();

    public abstract Session getSession(EntityMode entityMode);

    public abstract void flush() throws HibernateException;

    //...

    public abstract SessionFactory getSessionFactory();

    /** @deprecated */
    public abstract Connection connection() throws HibernateException;

    public abstract Connection close() throws HibernateException;

    public abstract void cancelQuery() throws HibernateException;

    public abstract boolean isOpen();

    public abstract boolean isConnected();

    public abstract boolean isDirty() throws HibernateException;

    public abstract boolean isDefaultReadOnly();

    public abstract void setDefaultReadOnly(boolean paramBoolean);

    public abstract Serializable getIdentifier(Object object) throws HibernateException;

    public abstract boolean contains(Object object);

    public abstract void evict(Object object) throws HibernateException;

    //...

    public abstract Object load(Class paramClass, Serializable serializable) throws HibernateException;

    public abstract Object load(String param, Serializable serializable) throws HibernateException;

    public abstract void load(Object object, Serializable serializable) throws HibernateException;

    //...

    public abstract Serializable save(Object object) throws HibernateException;

    public abstract Serializable save(String param, Object object) throws HibernateException;

    public abstract void saveOrUpdate(Object object) throws HibernateException;

    public abstract void saveOrUpdate(String param, Object object) throws HibernateException;

    public abstract void update(Object object) throws HibernateException;

    public abstract void update(String param, Object object) throws HibernateException;

    public abstract Object merge(Object object) throws HibernateException;

    public abstract Object merge(String param, Object object) throws HibernateException;

    public abstract void persist(Object object) throws HibernateException;

    public abstract void persist(String param, Object object) throws HibernateException;

    public abstract void delete(Object object) throws HibernateException;

    public abstract void delete(String param, Object object) throws HibernateException;

    //...

    public abstract Transaction beginTransaction() throws HibernateException;

    public abstract Transaction getTransaction();

    public abstract Criteria createCriteria(Class paramClass);

    public abstract Criteria createCriteria(Class paramClass, String param);

    public abstract Criteria createCriteria(String param);

    public abstract Criteria createCriteria(String param1, String param2);

    public abstract Query createQuery(String param) throws HibernateException;

    public abstract SQLQuery createSQLQuery(String param) throws HibernateException;

    public abstract Query createFilter(Object objectg, String param) throws HibernateException;

    public abstract Query getNamedQuery(String param) throws HibernateException;

    public abstract void clear();

    public abstract Object get(Class paramClass, Serializable serializable) throws HibernateException;

    //...

    public abstract Filter enableFilter(String param);

    public abstract Filter getEnabledFilter(String param);

    public abstract void disableFilter(String param);

    public abstract boolean isReadOnly(Object object);

    public abstract void setReadOnly(Object object, boolean paramBoolean);

    //...
}
