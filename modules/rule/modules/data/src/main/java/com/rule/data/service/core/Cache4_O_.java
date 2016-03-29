package com.rule.data.service.core;

import java.util.HashMap;
import java.util.Map;

public final class Cache4_O_ {

    private static final ThreadLocal<Map<String, Map<Map<String, Object>, Map<String, Map<Object, Object>>>>> cache
            = new ThreadLocal<Map<String, Map<Map<String, Object>, Map<String, Map<Object, Object>>>>>() {
        @Override
        protected Map<String, Map<Map<String, Object>, Map<String, Map<Object, Object>>>> initialValue() {
            return new HashMap<String, Map<Map<String, Object>, Map<String, Map<Object, Object>>>>();
        }
    };

    /**
     * 获取对应_O_的缓存，是TLS缓存，依据数据源名称、当前参数和对应函数名称
     *
     * @param serviceName
     * @param param
     * @param func
     * @return
     */
    public static Map<Object, Object> cache4_O_(String serviceName, Map<String, Object> param, String func) {
        if (serviceName == null || param == null || func == null) {
            return null;
        }

        Map<Map<String, Object>, Map<String, Map<Object, Object>>> cache4Service = cache.get().get(serviceName);
        if (cache4Service == null) {
            cache4Service = new HashMap<Map<String, Object>, Map<String, Map<Object, Object>>>();
            cache.get().put(serviceName, cache4Service);
        }

        Map<String, Map<Object, Object>> cache4Param = cache4Service.get(param);

        if (cache4Param == null) {
            cache4Param = new HashMap<String, Map<Object, Object>>();
            cache4Service.put(param, cache4Param);
        }


        Map<Object, Object> cache4_O_ = cache4Param.get(func);

        if (cache4_O_ == null) {
            cache4_O_ = new HashMap<Object, Object>();
            cache4Param.put(func, cache4_O_);
        }

        return cache4_O_;
    }


    public static void clear() {
        cache.get().clear();
    }

}
