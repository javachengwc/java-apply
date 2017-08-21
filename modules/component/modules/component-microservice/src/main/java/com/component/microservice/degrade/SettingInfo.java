package com.component.microservice.degrade;

import java.util.HashMap;
import java.util.Map;

/**
 * 降级设置信息
 */
public class SettingInfo {

    private Map<String, String> condtion = new HashMap<String, String>();

    private Map<String, Map<String, String>> strategy = new HashMap<String, Map<String, String>>();

    private String fallbackBean;

    public Map<String, String> getCondtion() {
        return condtion;
    }

    public void setCondtion(Map<String, String> condtion) {
        this.condtion = condtion;
    }

    public Map<String, Map<String, String>> getStrategy() {
        return strategy;
    }

    public void setStrategy(Map<String, Map<String, String>> strategy) {
        this.strategy = strategy;
    }

    public String getFallbackBean() {
        return fallbackBean;
    }

    public void setFallbackBean(String fallbackBean) {
        this.fallbackBean = fallbackBean;
    }
}
