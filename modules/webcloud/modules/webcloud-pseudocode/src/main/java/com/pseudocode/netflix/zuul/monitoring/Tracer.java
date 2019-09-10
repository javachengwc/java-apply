package com.pseudocode.netflix.zuul.monitoring;

public interface Tracer {

    void stopAndLog();

    void setName(String name);

}
