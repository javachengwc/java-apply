package com.pseudocode.netflix.hystrix.core;


import java.util.concurrent.Future;

import rx.Observable;

public interface HystrixExecutable<R> extends HystrixInvokable<R> {

    public R execute();

    public Future<R> queue();

    public Observable<R> observe();

}

