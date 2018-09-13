package com.mybatis.pseudocode.mybatis.scripting;

import com.mybatis.pseudocode.mybatis.exceptions.PersistenceException;

public class ScriptingException extends PersistenceException
{
    private static final long serialVersionUID = 7642570221267566591L;

    public ScriptingException()
    {
    }

    public ScriptingException(String message)
    {
        super(message);
    }

    public ScriptingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptingException(Throwable cause) {
        super(cause);
    }
}
