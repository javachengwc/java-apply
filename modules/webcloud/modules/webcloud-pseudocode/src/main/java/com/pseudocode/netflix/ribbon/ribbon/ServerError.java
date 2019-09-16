package com.pseudocode.netflix.ribbon.ribbon;

public class ServerError extends Exception {

    public ServerError(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerError(String message) {
        super(message);
    }

    public ServerError(Throwable cause) {
        super(cause);
    }

}