package com.rule.data.exception;

import com.rule.data.model.vo.CalInfo;
import com.rule.data.util.DataUtil;

import java.util.LinkedList;

public class RengineException extends Exception {

    private LinkedList<String> invokeInfos = new LinkedList<String>();

    private String serviceName;

    private String msg;

    public RengineException(String serviceName, String msg) {
        this.serviceName = serviceName;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        if (!invokeInfos.isEmpty()) {

            return (serviceName == null ? (msg) : (serviceName + ": " + msg)) + " 调用路径" + invokeInfos;
        }

        return (serviceName == null ? (msg) : (serviceName + ": " + msg));
    }

    public void addInvoke(CalInfo calInfo) {
        final String serviceName = calInfo.getServiceName();
        final String columnName = calInfo.getColumnName();

        invokeInfos.addFirst(serviceName + (columnName == null ? "":" - " + DataUtil.extract(calInfo.getCurColumn()) + " - " + columnName));
    }

    public String getServiceName() {
        return serviceName;
    }

}
