package com.esearch6.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class AggResult {

    private Long totalCnt=0L;

    //第组的分组map
    private Map<String,Long> groupMap= new HashMap<String,Long>();

    //把每个组项组合展平后的map
    private Map<String,Long> aggMap;

    //内嵌的分组map
    private Map<String,AggResult> innerMap;

    public Long getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(Long totalCnt) {
        this.totalCnt = totalCnt;
    }

    public Map<String, Long> getAggMap() {
        return aggMap;
    }

    public void setAggMap(Map<String, Long> aggMap) {
        this.aggMap = aggMap;
    }

    public Map<String, Long> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, Long> groupMap) {
        this.groupMap = groupMap;
    }

    public Map<String, AggResult> getInnerMap() {
        return innerMap;
    }

    public void setInnerMap(Map<String, AggResult> innerMap) {
        this.innerMap = innerMap;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static AggResult zeroResult() {
        AggResult result = new AggResult();
        result.setTotalCnt(0L);
        result.setAggMap(new HashMap<String,Long>());
        return result;
    }
}
