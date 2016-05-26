package com.shopstat.executor.aftersale;

import com.shopstat.dao.ext.aftersale.SafeguardStatDao;
import com.shopstat.executor.IExecutor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 维权统计类
 */
@Service
public class SafeguardStatExecutor  implements IExecutor{

    private Logger logger = LoggerFactory.getLogger(SafeguardStatExecutor.class);

    @Autowired
    private SafeguardStatDao safeguardStatDao;

    public void exec(DateTime dateTime)
    {
        logger.info("SafeguardStatExecutor exec begin,dateTime ="+dateTime);

        //采集数据
        genDemoData(dateTime);

        //根据采集的数据按照各维度来生成任意维度(包括多维度组合)为全部的汇总数据


        logger.info("SafeguardStatExecutor exec end,dateTime="+dateTime);

    }

    //产生假数据
    public void genDemoData(DateTime dateTime)
    {

    }
}
