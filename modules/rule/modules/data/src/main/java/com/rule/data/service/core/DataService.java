package com.rule.data.service.core;

import com.alibaba.fastjson.JSON;
import com.rule.data.exception.RengineException;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.util.ConfigUtil;
import com.rule.data.util.DataUtil;
import com.rule.data.util.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 数据查询服务类
 */
public class DataService {

    public static final int calTimeout = ConfigUtil.getCalTimeout();


    /**
     * 数据源计算入口类, 计算对应数据源+参数的结果
     *
     * @param info
     * @param param
     * @return
     * @throws com.rule.data.exception.RengineException
     */
    public static D2Data getD2Data(SerService info, Map<String, Object> param) throws RengineException {
        if (info == null) {
            return null;
        }

        if (param == null) {
            param = DataUtil.EMPTY;
        }

        final Integer serviceID = info.getServiceID();
        final String name = info.getName();
        param = checkParam(param, name);

        clearTLS();

        final String json = JSON.toJSONString(param);
        final long time = System.currentTimeMillis();
        try {
            D2Data data = Cache4D2Data.getD2Data(info, param, 0, null, null, "起始");
            return data;
        } finally {
            if (name != null) {
                AtomicLong atomicLong = DataUtil.serviceTimes.get(name);
                if (atomicLong == null) {
                    DataUtil.serviceTimes.put(name, new AtomicLong(1));
                } else {
                    atomicLong.incrementAndGet();
                }
            }

            final long lat = System.currentTimeMillis() - time;
            LogUtil.info(serviceID + "-" + name + " " + json + " latency:" + lat);

            if (lat > calTimeout) {
                LogMonitor.error(name, "数据源运算时间超过阀值(" + calTimeout + "), " + lat + ", 参数" + json);
            }

            Cache4_O_.clear();
            Cache4D2Data.clear();
        }
    }

    /**
     * 判断参数是否全为空，如果全为空则报错，map会认为是不为空
     *
     * @param param
     * @param name
     */
    private static Map<String, Object> checkParam(Map<String, Object> param, String name) throws RengineException {

        if (param.isEmpty()) {
            return param;
        }

        boolean isAllEmpty = true;
        int maxParamSize = 0;


        for (Map.Entry<String, Object> entry : param.entrySet()) {
            final Object value = entry.getValue();

            if (value == null) {
                continue;
            }

            if (value instanceof Map) {
                isAllEmpty = false;
                int size = ((Map) value).size();
                maxParamSize = size > maxParamSize ? size : maxParamSize;
            } else if (value.toString().length() != 0) {
                isAllEmpty = false;
            }
        }

        if (isAllEmpty) {
            throw new RengineException(name, "输入参数全为空");
        }

        if (param.get("datarow_need") == null) {
            param.put("datarow_need", maxParamSize);
        }

        param = DataUtil.convert2String(param);
        return param;
    }

    /**
     * 清除线程局部缓存, 包括跨表函数, 数据源局部缓存, 运算信息的缓存
     */
    private static void clearTLS() {
        if (LogUtil.isDebugEnabled())
        {
            LogUtil.debug("clear TLS");
        }
        Cache4_O_.clear();
        Cache4D2Data.clear();
        ProcessInfos.clear();
    }
}
