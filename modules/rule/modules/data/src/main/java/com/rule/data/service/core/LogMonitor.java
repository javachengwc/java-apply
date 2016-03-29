package com.rule.data.service.core;

import com.alibaba.fastjson.JSON;
import com.rule.data.handler.Gearman;
import com.rule.data.model.SerAlarm;
import com.rule.data.model.SerCallLog;
import com.rule.data.model.SerMonitor;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.query.ServiceReqInfo;
import com.rule.data.util.ConfigUtil;
import com.rule.data.util.DataUtil;
import com.rule.data.util.LogUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class LogMonitor {

    private static final int waiting_threshold = ConfigUtil.getWaitingThreshold();

    private static final ConcurrentLinkedQueue<SerAlarm> queue = new ConcurrentLinkedQueue<SerAlarm>();

    private static final ConcurrentLinkedQueue<SerCallLog> callQueue = new ConcurrentLinkedQueue<SerCallLog>();

    static class ServiceInvoke implements Comparable {
        public String key;
        public long time;

        ServiceInvoke(String key, long time) {
            this.key = key;
            this.time = time;
        }


        @Override
        public int compareTo(Object o) {
            return (int) (((ServiceInvoke) o).time - time);
        }
    }

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("rengine monitor");

                int times = 600;
                while (true) {
                    try {
                        if (times >= 600) {
                            times = 0;

                            SerMonitor po = new SerMonitor();

                            final ConcurrentHashMap<String, AtomicLong> serviceTimes = DataUtil.serviceTimes;
                            final List<ServiceInvoke> nodes = new ArrayList<ServiceInvoke>();

                            for (Map.Entry<String, AtomicLong> entry : serviceTimes.entrySet()) {
                                nodes.add(new ServiceInvoke(entry.getKey(), entry.getValue().get()));
                            }

                            Collections.sort(nodes);

                            int count = 0;
                            for (Map.Entry<String, ConcurrentHashMap<String, D2Data>> entry
                                    : Cache4BaseService.DATA_CACHE.entrySet()) {
                                final int size = entry.getValue().size();
                                count += size;
                            }

                            po.setHttptimes(Gearman.httpTimes.get());
                            po.setCachesum(count);
                            po.setServiceinvoke(JSON.toJSONString(nodes));

                            try {
                                com.rule.data.service.core.Services.client.insert("insertTServiceMonitor", po);
                            } catch (Exception e) {
                                //
                            }
                        }

                        SerAlarm err;
                        while ((err = queue.poll()) != null) {
                            try {
                                com.rule.data.service.core.Services.client.insert("insertTServiceAlarm", err);
                            } catch (Exception e) {
                                //
                            }
                        }

                        SerCallLog callLogPo;
                        while((callLogPo=callQueue.poll())!=null){
                            try{
                                ServiceReqInfo serviceReqInfo = JSON.parseObject(callLogPo.getParam(), ServiceReqInfo.class);
                                callLogPo.setServiceName(serviceReqInfo.getServiceName());
                                callLogPo.setParam(JSON.toJSONString(serviceReqInfo.getParam()));
                                callLogPo.setTime(new Date());
                                com.rule.data.service.core.Services.client.insert("insertTServiceCallLog",callLogPo);
                            }catch (Exception e){
                                //
                            }
                        }

                        final int waiting = Gearman.executor4G.getQueue().size();
                        if (waiting > waiting_threshold) {
                            err = new SerAlarm(
                                    "告警: 等待任务过多"
                                    , "等待任务数超过阀值(" + waiting_threshold + "), " + waiting);
                            try {
                                Services.client.insert("insertTServiceAlarm", err);
                            } catch (Exception e) {
                                //
                            }
                        }
                    } catch (Exception e) {
                        //
                    } finally {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            times++;
                        } catch (Exception e) {
                            //
                        }
                    }
                }
            }
        }).start();
    }

    public static void start() {
    }

    public static void error(String sname, String msg) {
        queue.offer(new SerAlarm(sname, LogUtil.str2OneLine(msg)));
    }

    public static void callLog(String ip , String content){
        try {
            SerCallLog callLogPo = new SerCallLog();
            callLogPo.setIp(ip);
            callLogPo.setParam(content);
            callQueue.offer(callLogPo);
        }catch(Exception e)
        {

        }
    }
}
