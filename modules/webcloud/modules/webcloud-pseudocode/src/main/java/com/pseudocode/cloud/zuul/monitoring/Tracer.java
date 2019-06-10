package com.pseudocode.cloud.zuul.monitoring;

public interface Tracer {

    void stopAndLog();

    void setName(String name);

}
