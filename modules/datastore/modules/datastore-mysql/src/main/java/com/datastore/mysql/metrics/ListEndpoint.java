package com.datastore.mysql.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.stereotype.Component;

import org.springframework.boot.actuate.endpoint.Endpoint;
import java.util.List;

//列举所有端点
//访问url-->http://ip:port/allEndpoint
@Component
public class ListEndpoint  extends AbstractEndpoint<List<Endpoint>> {

    private List<Endpoint> endpoints;

    @Autowired
    public ListEndpoint(List<Endpoint> endpoints) {
        super("allEndpoint");
        this.endpoints = endpoints;
    }

    public List<Endpoint> invoke() {
        return this.endpoints;
    }
}