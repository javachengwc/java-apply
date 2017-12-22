package com.component.rest.jersey.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestProp {

    @Value("${rest.connect.timeout.seconds:0}")
    private Integer restConnectTimeoutSec;

    @Value("${rest.read.timeout.seconds:0}")
    private Integer restReadTimeoutSec;

    public Integer getRestConnectTimeoutSec() {

        return restConnectTimeoutSec;
    }

    public Integer getRestReadTimeoutSec() {

        return restReadTimeoutSec;
    }
}
