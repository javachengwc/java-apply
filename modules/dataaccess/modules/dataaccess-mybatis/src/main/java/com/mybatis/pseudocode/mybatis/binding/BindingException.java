package com.mybatis.pseudocode.mybatis.binding;

import com.mybatis.pseudocode.mybatis.exceptions.PersistenceException;

public class BindingException extends PersistenceException
{
    private static final long serialVersionUID = 300802238789381562L;

    public BindingException()
    {
    }

    public BindingException(String message)
    {
        super(message);
    }

    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BindingException(Throwable cause) {
        super(cause);
    }
}
