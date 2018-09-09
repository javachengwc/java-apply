package com.hibernate.pseudocode.core;


public class HibernateException extends RuntimeException
{
    public HibernateException(Throwable root)
    {
        super(root);
    }

    public HibernateException(String string, Throwable root) {
        super(string, root);
    }

    public HibernateException(String s) {
        super(s);
    }
}