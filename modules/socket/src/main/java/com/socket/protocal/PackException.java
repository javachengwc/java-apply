package com.socket.protocal;

public class PackException extends RuntimeException
{
    private static final long serialVersionUID = 39999239931L;

    public PackException()
    {
        super( "PackError" );
    }
    
    public PackException(String message)
    {
    	super(message);
    }
}
