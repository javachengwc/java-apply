package com.pseudocode.netflix.ribbon.ribbon;

import rx.Observable;

import java.util.concurrent.Future;

public interface RequestWithMetaData<T> {

    Observable<RibbonResponse<Observable<T>>> observe();

    Observable<RibbonResponse<Observable<T>>> toObservable();

    Future<RibbonResponse<T>> queue();

    RibbonResponse<T> execute();
}
