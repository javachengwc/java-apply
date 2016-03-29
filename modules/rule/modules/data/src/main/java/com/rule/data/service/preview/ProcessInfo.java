package com.rule.data.service.preview;

import com.rule.data.model.vo.InvokeNode;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashSet;
import java.util.Set;

public class ProcessInfo {

    private Set<String> serviceNames = new HashSet<String>();

    private Set<String> paramRequired = new HashSet<String>();

    private Set<String> paramROptional = new HashSet<String>();

    private InvokeNode root;


    public Set<String> getParamROptional() {
        return paramROptional;
    }

    public void setParamROptional(Set<String> paramROptional) {
        this.paramROptional = paramROptional;
    }

    public Set<String> getParamRequired() {
        return paramRequired;
    }

    public void setParamRequired(Set<String> paramRequired) {
        this.paramRequired = paramRequired;
    }

    public InvokeNode getRoot() {
        return root;
    }

    public void setRoot(InvokeNode root) {
        this.root = root;
    }

    public Set<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(Set<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
