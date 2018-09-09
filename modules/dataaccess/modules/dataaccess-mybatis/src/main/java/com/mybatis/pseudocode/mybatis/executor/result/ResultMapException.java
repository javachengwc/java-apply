package com.mybatis.pseudocode.mybatis.executor.result;

import com.mybatis.pseudocode.mybatis.exceptions.PersistenceException;

public class ResultMapException extends PersistenceException
{
    private static final long serialVersionUID = 3272060569707623L;

    public ResultMapException()
    {
    }

    public ResultMapException(String message)
    {
        super(message);
    }

    public ResultMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultMapException(Throwable cause) {
        super(cause);
    }
}