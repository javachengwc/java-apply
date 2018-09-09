package com.hibernate.pseudocode.core;

public class CallbackException extends HibernateException
{
    public CallbackException(Exception root)
    {
        super("An exception occurred in a callback", root);
    }

    public CallbackException(String message) {
        super(message);
    }

    public CallbackException(String message, Exception e) {
        super(message, e);
    }
}
