package com.pseudocode.cloud.commons.client.loadbalancer;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryException;

import java.net.URI;

public abstract class LoadBalancedRecoveryCallback<T, R> implements RecoveryCallback<T> {

    protected abstract T createResponse(R response, URI uri);

    @Override
    public T recover(RetryContext context) throws Exception {
        Throwable lastThrowable = context.getLastThrowable();
        if (lastThrowable != null) {
            if (lastThrowable instanceof RetryableStatusCodeException) {
                RetryableStatusCodeException ex = (RetryableStatusCodeException) lastThrowable;
                return createResponse((R) ex.getResponse(), ex.getUri());
            } else if (lastThrowable instanceof Exception){
                throw (Exception)lastThrowable;
            }
        }
        throw new RetryException("Could not recover", lastThrowable);
    }
}
