package com.app.service;


import com.app.aop.ServiceAop;
import com.app.util.SpringContextUtils;
import com.util.aop.AopProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceFactory {

    private static Logger logger= LoggerFactory.getLogger(ServiceFactory.class);

    private static  ConcurrentHashMap<String,Object> serviceMap = new ConcurrentHashMap<String, Object>();

    //默认是不公平锁
    private static Lock lock = new ReentrantLock();

    public static RecordService getRecordService( ) {
        String serviceName=RecordService.class.getSimpleName();
        Object obj= serviceMap.get(serviceName);
        if(obj!=null) {
            return (RecordService)obj;
        }
        try{
            lock.lock();
            logger.info("ServiceFactory begin gen porxy recordService");
            if(serviceMap.get(serviceName)==null) {
                RecordService bean = (RecordService)SpringContextUtils.getBean(RecordService.class);
                if(bean==null) {

                }
                RecordService recordService =(RecordService) AopProxy.getProxyInstance(bean, new ServiceAop());
                serviceMap.putIfAbsent(serviceName,recordService);
            }
            logger.info("ServiceFactory end gen porxy recordService");
        } catch (Exception e) {
            logger.info("ServiceFactory gen porxy recordService error,",e);
        } finally {
            lock.unlock();
        }
        obj= serviceMap.get(serviceName);
        if(obj!=null) {
            return (RecordService)obj;
        }
        return null;
    }

    public static Long getServiceIncokeCount() {

        return ServiceAop.getServiceIncokeCount();
    }
}
