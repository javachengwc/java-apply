package com.mina.server;


import java.io.Serializable;

public class RemoteInvokeResult implements Serializable {

    public static final int SUCCESS=0;

    public static final int FAIL=1;

    private int status = SUCCESS;

    private Throwable throwable;

    private Object obj;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}

