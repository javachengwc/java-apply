package com.rule.data.service.core;

import com.rule.data.exception.RengineException;
import com.rule.data.model.SerColumn;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.model.vo.InvokeNode;
import com.rule.data.util.ConfigUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 有缓存的查询数据结果类
 */
public final class Cache4D2Data {

    /**
     * Map<String, Map<Map<String, Object>, D2Data>>
     * key                                   -->serviceName
     * Map<Map<String, Object>, D2Data>      -->对应服务的各条件以及结果数据
     * <Map<String, Object>                  -->查询条件
     * D2Data                                -->结果数据 表里面多行相应列数据
     */
    private static final ThreadLocal<Map<String, Map<Map<String, Object>, D2Data>>> cache = new ThreadLocal<Map<String, Map<Map<String, Object>, D2Data>>>() {
        @Override
        protected Map<String, Map<Map<String, Object>, D2Data>> initialValue() {
            return new HashMap<String, Map<Map<String, Object>, D2Data>>();
        }
    };

    private static final boolean tlsEnabled = ConfigUtil.getTlsEnabled();

    private static final D2Data EMPTY = new D2Data(new ArrayList<SerColumn>(0));

    /**
     * 可递归的计算入口, 会依据father判断是否构成环调用
     * att代表切入这个计算的参考信息
     * 函数在检测完环后, 根据数据源类型调用高级和基本类进行计算, 对结果依据配置参数进行缓存
     *
     * @param servicePo
     * @param param
     * @param callLayer
     * @param father
     * @param fatherParam
     * @param att
     * @return
     * @throws com.rule.data.exception.RengineException
     */
    public static D2Data getD2Data(SerService servicePo, Map<String, Object> param, int callLayer ,
                                   SerService father, Map<String, Object> fatherParam, String att) throws RengineException {

        if (servicePo == null || param == null) {
            throw new RengineException(null, "数据源配置信息 或者 参数 为null");
        }

        InvokeNode node = ProcessInfos.addLink(father, fatherParam, servicePo, param, att);

        final Map<String, Map<Map<String, Object>, D2Data>> cacheIN = cache.get();
        final String serviceName = servicePo.getName();

        Map<Map<String, Object>, D2Data> cacheInService = cacheIN.get(serviceName);

        if (cacheInService == null) {
            cacheInService = new HashMap<Map<String, Object>, D2Data>();
            cacheIN.put(serviceName, cacheInService);
        }

        D2Data d2Data = cacheInService.get(param);

        if (d2Data == null || !tlsEnabled) {

            long time = System.currentTimeMillis();
            if (servicePo.getType().equalsIgnoreCase("base")) {

                //基础数据的查询
                d2Data = Cache4BaseService.getD2Data(servicePo, param);
            } else {

                //扩展数据的查询
                d2Data = Cache4AdvanceService.getD2Data(servicePo, param, callLayer + 1);
            }

            if (node != null) {
                node.setTime(System.currentTimeMillis() - time);
                node.setLines(d2Data.getData().length);
            }

            if (tlsEnabled) {
                // 启用时存入
                cacheInService.put(param, d2Data);
            } else {
                // 未启用时仅存入占位符
                cacheInService.put(param, EMPTY);
            }
        }

        return d2Data;
    }

    /**
     * 获取对应参数的计算结果, 用于判断是不是有环调用
     *
     * @param serviceName
     * @param param
     * @return
     */
    static D2Data peek(String serviceName, Map<String, Object> param) {
        final Map<String, Map<Map<String, Object>, D2Data>> cacheIN = cache.get();
        Map<Map<String, Object>, D2Data> cacheInService = cacheIN.get(serviceName);

        if (cacheInService != null) {
            D2Data d2Data = cacheInService.get(param);
            return d2Data;
        }
        return null;
    }

    public static void clear() {
        cache.get().clear();
    }
}
