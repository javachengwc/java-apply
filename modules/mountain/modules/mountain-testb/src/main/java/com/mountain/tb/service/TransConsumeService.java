package com.mountain.tb.service;

import com.mountain.tb.finagle.trans.StringResult;
import com.mountain.tb.finagle.trans.TransDealService;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费服务
 */
public class TransConsumeService {

    private static Logger logger = LoggerFactory.getLogger(TransConsumeService.class);

    private TransDealService.ServiceIface transService;

    public void setTransService(TransDealService.ServiceIface transService) {
        this.transService = transService;
    }

    public TransConsumeService()
    {
        logger.info("TransConsumeService create ..........");
    }

    public void consume(int dealId)
    {
        logger.info("TransConsumeService consume start");

        ThreadUtil.sleep(2000l);
        try {
            StringResult result = transService.transDeal(dealId).toJavaFuture().get();
            System.out.println("--------------transDeal result:");
            System.out.println(result);

        }catch(Exception e)
        {
            logger.info("TransConsumeService transDeal error,dealId="+dealId,e);
        }
        ThreadUtil.sleep(2000l);
        logger.info("TransConsumeService consume end");
    }
}
