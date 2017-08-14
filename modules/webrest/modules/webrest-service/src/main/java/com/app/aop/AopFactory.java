package com.app.aop;

import com.app.service.RecordService;
import com.util.aop.AopProxy;

public class AopFactory {

    public static RecordService wrapRecordService(RecordService targetBean) {
        RecordService recordService =(RecordService) AopProxy.getProxyInstance(targetBean, new ServiceAop());
        return recordService;
    }
}
