package com.shop.book.search.scheduled;

import com.shop.book.search.config.SolrConfig;
import com.util.http.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookIndexScheduled {

    private static Logger logger= LoggerFactory.getLogger(BookIndexScheduled.class);

    private static String BOOK_IMPORT_PATH_SUFFIX="/dataimport?command=full-import&clean=true&commit=true&verbose=false";

    @Autowired
    private SolrConfig solrConfig;

    //每天晚上3钟执行
    @Scheduled(cron="0 0 3 * * ?")
    public void freshIndex() {
        logger.info("BookIndexScheduled freshIndex start,now={}",System.currentTimeMillis());
        try {
            String collectName =solrConfig.getBookCollect();
            String url = solrConfig.getSolrUrlByCollect(collectName);
            logger.info("BookIndexScheduled freshIndex collectName={},url={}",collectName,url);
            String [] urls  = url.split(",");
            for(String perUrl :urls) {
                if(perUrl.endsWith("/")) {
                    perUrl= perUrl.substring(0,perUrl.length()-1);
                }
                String importUrl = perUrl+BOOK_IMPORT_PATH_SUFFIX;
                HttpClientUtil.get(importUrl,null);
            }
            logger.info("BookIndexScheduled freshIndex finish, now={}",System.currentTimeMillis());
        } catch (Throwable e) {
            logger.error("BookIndexScheduled freshIndex error,",e);
        }
    }
}
