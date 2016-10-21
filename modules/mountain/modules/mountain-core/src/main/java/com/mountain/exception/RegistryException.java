package com.mountain.exception;

public class RegistryException extends  RuntimeException{

    public RegistryException(String message)
    {
        super(message);
    }

    public RegistryException(String message,Exception e)
    {
        super(message,e);
    }
}
