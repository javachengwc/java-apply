package com.rule.data.util;

import com.rule.data.exception.RengineException;

public class DebugUtil {

    private static final boolean debugFlag = ConfigUtil.getDebugFlag();

    public static void debugNull(String serviceName, String content, String desc) throws RengineException {
        if (debugFlag) {
            if (serviceName == null || "".equals(serviceName.trim())) {
                if (desc != null) {
                    throw new RengineException(serviceName, "数据源未找到;" + desc + ",content:" + content);
                }else {
                    throw new RengineException(serviceName, "数据源未找到;content:" + content);
                }
            }
        }
    }

    public static void debugJSON(String message, String content) throws RengineException {
        if (debugFlag) {
            throw new RengineException(null, "JSON解析错误, " + message + ";content:" + content);
        }
    }
}
