package com.pseudocode.netflix.feign.core;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pseudocode.netflix.feign.core.Param.Expander;

public final class MethodMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    private String configKey;
    private transient Type returnType;
    private Integer urlIndex;
    private Integer bodyIndex;
    private Integer headerMapIndex;
    private Integer queryMapIndex;
    private boolean queryMapEncoded;
    private transient Type bodyType;
    private RequestTemplate template = new RequestTemplate();
    private List<String> formParams = new ArrayList<String>();
    private Map<Integer, Collection<String>> indexToName = new LinkedHashMap<Integer, Collection<String>>();
    private Map<Integer, Class<? extends Expander>> indexToExpanderClass = new LinkedHashMap<Integer, Class<? extends Expander>>();
    private Map<Integer, Boolean> indexToEncoded = new LinkedHashMap<Integer, Boolean>();
    private transient Map<Integer, Expander> indexToExpander;

    MethodMetadata() {
    }

    public String configKey() {
        return configKey;
    }

    public MethodMetadata configKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public Type returnType() {
        return returnType;
    }

    public MethodMetadata returnType(Type returnType) {
        this.returnType = returnType;
        return this;
    }

    public Integer urlIndex() {
        return urlIndex;
    }

    public MethodMetadata urlIndex(Integer urlIndex) {
        this.urlIndex = urlIndex;
        return this;
    }

    public Integer bodyIndex() {
        return bodyIndex;
    }

    public MethodMetadata bodyIndex(Integer bodyIndex) {
        this.bodyIndex = bodyIndex;
        return this;
    }

    public Integer headerMapIndex() {
        return headerMapIndex;
    }

    public MethodMetadata headerMapIndex(Integer headerMapIndex) {
        this.headerMapIndex = headerMapIndex;
        return this;
    }

    public Integer queryMapIndex() {
        return queryMapIndex;
    }

    public MethodMetadata queryMapIndex(Integer queryMapIndex) {
        this.queryMapIndex = queryMapIndex;
        return this;
    }

    public boolean queryMapEncoded() {
        return queryMapEncoded;
    }

    public MethodMetadata queryMapEncoded(boolean queryMapEncoded) {
        this.queryMapEncoded = queryMapEncoded;
        return this;
    }

    public Type bodyType() {
        return bodyType;
    }

    public MethodMetadata bodyType(Type bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public RequestTemplate template() {
        return template;
    }

    public List<String> formParams() {
        return formParams;
    }

    public Map<Integer, Collection<String>> indexToName() {
        return indexToName;
    }

    public Map<Integer, Boolean> indexToEncoded() {
        return indexToEncoded;
    }

    public Map<Integer, Class<? extends Expander>> indexToExpanderClass() {
        return indexToExpanderClass;
    }

    public MethodMetadata indexToExpander(Map<Integer, Expander> indexToExpander) {
        this.indexToExpander = indexToExpander;
        return this;
    }

    public Map<Integer, Expander> indexToExpander() {
        return indexToExpander;
    }
}
