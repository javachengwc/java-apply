package com.cloud.pseudocode.zuul.monitoring;

public interface Tracer {

    void stopAndLog();

    void setName(String name);

}
