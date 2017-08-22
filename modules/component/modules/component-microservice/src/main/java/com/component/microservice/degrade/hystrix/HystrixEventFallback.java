package com.component.microservice.degrade.hystrix;

import com.netflix.hystrix.HystrixEventType;

import java.util.HashMap;
import java.util.Map;

public class HystrixEventFallback {

    private static Map<HystrixEventType,Boolean> eventFallbackMap= new HashMap<HystrixEventType,Boolean>();

    static {
        eventFallbackMap.put(HystrixEventType.EMIT,true);
        eventFallbackMap.put(HystrixEventType.FAILURE,true);
        eventFallbackMap.put(HystrixEventType.TIMEOUT,true);
        eventFallbackMap.put(HystrixEventType.SHORT_CIRCUITED,true);
        eventFallbackMap.put(HystrixEventType.THREAD_POOL_REJECTED,true);
        eventFallbackMap.put(HystrixEventType.SEMAPHORE_REJECTED,true);
        eventFallbackMap.put(HystrixEventType.FALLBACK_EMIT,true);
        eventFallbackMap.put(HystrixEventType.EXCEPTION_THROWN,true);
        eventFallbackMap.put(HystrixEventType.COLLAPSED,true);
        eventFallbackMap.put(HystrixEventType.SUCCESS,false);
        eventFallbackMap.put(HystrixEventType.BAD_REQUEST,false);
        eventFallbackMap.put(HystrixEventType.FALLBACK_SUCCESS,false);
        eventFallbackMap.put(HystrixEventType.FALLBACK_FAILURE,false);
        eventFallbackMap.put(HystrixEventType.FALLBACK_REJECTION,false);
        eventFallbackMap.put(HystrixEventType.FALLBACK_MISSING,false);
        eventFallbackMap.put(HystrixEventType.RESPONSE_FROM_CACHE,false);
    }

    public static Boolean getEventFallback(HystrixEventType type) {
        Boolean rt= eventFallbackMap.get(type);
        if(rt==null) {
            rt=false;
        }
        return rt;
    }
}
