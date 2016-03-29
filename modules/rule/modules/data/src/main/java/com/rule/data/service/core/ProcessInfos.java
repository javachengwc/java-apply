package com.rule.data.service.core;

import com.rule.data.exception.RengineException;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.InvokeNode;

import java.util.Map;

public final class ProcessInfos {

    private static ThreadLocal<InvokeInfo> invokeInfo = new ThreadLocal<InvokeInfo>() {
        @Override
        protected InvokeInfo initialValue() {
            return new InvokeInfo();
        }
    };


    public static void clear() {
        invokeInfo.remove();
    }

    static InvokeNode addLink(SerService father, Map<String, Object> fatherParam,
                              SerService child, Map<String, Object> childrParam, String attr) throws RengineException {
        return invokeInfo.get().addEdge(father, fatherParam, child, childrParam, attr);
    }

    public static InvokeNode getInvokeNode() {
        return invokeInfo.get().root;
    }
}
