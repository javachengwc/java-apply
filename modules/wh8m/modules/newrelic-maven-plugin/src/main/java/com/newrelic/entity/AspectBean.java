package com.newrelic.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

/**
 * 一个类的切入信息
 */
public class AspectBean {

    //实现类名
    private String classImpl;

    //接口名
    private String face;

    //方法
    private Map<String,List<String>> methods;

    public String getClassImpl() {
        return classImpl;
    }

    public void setClassImpl(String classImpl) {
        this.classImpl = classImpl;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Map<String, List<String>> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, List<String>> methods) {
        this.methods = methods;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
