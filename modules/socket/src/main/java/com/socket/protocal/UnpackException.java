package com.socket.protocal;

public class UnpackException extends RuntimeException
{
    public static final long serialVersionUID = 1231329303888892L;

    public UnpackException()
    {
        this( "Unpack error" );
    }

    public UnpackException( String message )
    {
        super( message );
    }

    public UnpackException( Throwable cause )
    {
        super( cause );
    }

    public UnpackException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
