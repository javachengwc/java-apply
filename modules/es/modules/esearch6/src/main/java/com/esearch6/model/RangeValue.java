package com.esearch6.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RangeValue {

    private String key;

    private Integer min;

    private Integer max;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
