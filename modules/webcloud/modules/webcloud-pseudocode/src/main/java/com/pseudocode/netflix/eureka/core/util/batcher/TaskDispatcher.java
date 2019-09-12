package com.pseudocode.netflix.eureka.core.util.batcher;

public interface TaskDispatcher<ID, T> {

    void process(ID id, T task, long expiryTime);

    void shutdown();
}

