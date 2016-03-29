package com.rule.data.model.vo;


import com.rule.data.model.SerColumn;
import com.rule.data.model.SerService;

import java.util.HashMap;
import java.util.Map;

public class CalInfo {

    public  SerService servicePo;

    public  Map<String, Object> param;

    public  Map<String, Map<Object, Object>> cache4_S = new HashMap<String, Map<Object, Object>>();

    public  int callLayer;

    public D2Data curD2data;

    public SerColumn curColumnPo;

    public String serviceName;

    public String columnName;

    public int curRow;

    public int curColumn;

    public int maxRow;

    public int getCurColumn() {
        return curColumn;
    }

    public int getCurRow() {
        return curRow;
    }

    public Map<Object, Object> getCache(String name) {
        Map<Object, Object> result = cache4_S.get(name);

        if (result == null) {
            result = new HashMap<Object, Object>();
            cache4_S.put(name, result);
        }

        return result;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public void setCurD2data(D2Data curD2data) {
        this.curD2data = curD2data;
    }

    public CalInfo(int callLayer, Map<String, Object> param, SerService servicePo) {

        if (param == null || servicePo == null) {
            throw new NullPointerException("param or servicePo");
        }

        this.callLayer = callLayer;
        this.param = param;
        this.servicePo = servicePo;
        this.serviceName = servicePo.getName();
    }

    public int getCallLayer() {
        return callLayer;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public SerService getServicePo() {
        return servicePo;
    }
}
