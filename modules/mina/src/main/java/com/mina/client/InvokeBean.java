package com.mina.client;

import java.io.Serializable;
import java.util.List;

public class InvokeBean implements Serializable {

    private String beanName;

    private String className;

    private String methodName;

    private List<Object> paramters;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Object> getParamters() {
        return paramters;
    }

    public void setParamters(List<Object> paramters) {
        this.paramters = paramters;
    }
}
