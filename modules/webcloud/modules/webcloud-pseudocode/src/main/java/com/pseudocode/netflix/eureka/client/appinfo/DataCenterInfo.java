package com.pseudocode.netflix.eureka.client.appinfo;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;


@JsonRootName("dataCenterInfo")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
//@JsonTypeIdResolver(DataCenterTypeInfoResolver.class)
public interface DataCenterInfo {
    enum Name {Netflix, Amazon, MyOwn}

    Name getName();
}

