package com.configcenter.zookeeper;

import org.apache.curator.framework.api.UnhandledErrorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常监听器
 */
public class BaseErrorListener implements UnhandledErrorListener {

    private static Logger logger = LoggerFactory.getLogger(BaseErrorListener.class);

    public void unhandledError(java.lang.String s, java.lang.Throwable throwable)
    {
        logger.info("...............BaseErrorListener  unhandledError "+s +",exception-->"+throwable.getClass().getName()+ " "+ throwable.getMessage() );
    }
}
