package com.hibernate.pseudocode.core;

public abstract interface Transaction
{
    public abstract void begin() throws HibernateException;

    public abstract void commit() throws HibernateException;

    public abstract void rollback() throws HibernateException;

    public abstract boolean wasRolledBack() throws HibernateException;

    public abstract boolean wasCommitted() throws HibernateException;

    public abstract boolean isActive() throws HibernateException;

    //...

    public abstract void setTimeout(int paramInt);
}
