package com.pseudocode.netflix.ribbon.ribbon;

public class UnsuccessfulResponseException extends Exception {

    public UnsuccessfulResponseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public UnsuccessfulResponseException(String arg0) {
        super(arg0);
    }

    public UnsuccessfulResponseException(Throwable arg0) {
        super(arg0);
    }
}
