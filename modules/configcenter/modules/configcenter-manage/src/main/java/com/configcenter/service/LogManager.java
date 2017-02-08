package com.configcenter.service;

import com.configcenter.model.OperateRecord;
import com.configcenter.util.SpringContextUtils;
import com.configcenter.vo.OnlineUser;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 业务日志管理器
 */
public class LogManager {

    private static Logger logger = LoggerFactory.getLogger(LogManager.class);

    //每隔10秒处理队列日志数据入库
    private static long perTimes =10*1000l;

    private static final ConcurrentLinkedQueue<OperateRecord> queue = new ConcurrentLinkedQueue<OperateRecord>();

    public static void log(OnlineUser onlineUser,String operate){

        if(onlineUser==null)
        {
            logger.warn("LogManager log onlineUser is null,operate"+operate);
            return;
        }
        OperateRecord operateRecord = new OperateRecord();
        operateRecord.setOperatorId(onlineUser.getId());
        operateRecord.setOperatorName(onlineUser.getName());
        operateRecord.setOperateTime(new Date());
        operateRecord.setOperation(operate);

        boolean rt =queue.offer(operateRecord);
        if(!rt)
        {
            logger.warn("LogManager log queue 队列已满,operateRecord="+operateRecord);
        }
    }

    //开启记录业务日志线程
    static
    {
        OperateRecordService operateRecordService = SpringContextUtils.getBean("operateRecordService",OperateRecordService.class);
        //记录
        new Thread(new LogRunnable(operateRecordService),"operate-log-thread").start();

    }

    private static class LogRunnable implements  Runnable
    {
        private OperateRecordService operateRecordService;

        public LogRunnable(OperateRecordService operateRecordService)
        {
            this.operateRecordService = operateRecordService;
        }

        public void run()
        {

            try{
                logger.info(Thread.currentThread().getName()+" LogManager save operate log thread start,");

                long times=0l;

                while(true)
                {
                    long dealTime =0l;
                    while(times>=perTimes) {
                        times = 0l;
                        OperateRecord operateRecord;
                        long startTime=System.currentTimeMillis();
                        while ((operateRecord = queue.poll()) != null) {
                            operateRecordService.add(operateRecord);
                        }
                        dealTime= System.currentTimeMillis()-startTime;
                    }
                    times+=dealTime;
                    ThreadUtil.sleep(1000);
                    times+=1000;
                }

            }catch(Exception e)
            {
                logger.error("LogManager save operate log  error,",e);
            }
        }
    }

}
