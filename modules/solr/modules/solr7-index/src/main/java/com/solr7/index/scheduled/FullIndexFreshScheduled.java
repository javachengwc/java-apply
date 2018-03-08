package com.solr7.index.scheduled;

import com.solr7.index.config.SolrConfig;
import com.solr7.index.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FullIndexFreshScheduled {

    private static Logger logger= LoggerFactory.getLogger(FullIndexFreshScheduled.class);

    private static String IMPORT_PATH_SUFFIX="/dataimport?command=full-import&clean=true&commit=true&verbose=false";

    @Autowired
    private SolrConfig solrConfig;

    //每天晚上0点钟执行
    @Scheduled(cron="0 0 0 * * ?")
    public void freshIndex() {
        logger.info("FullIndexFreshScheduled freshIndex start,now={}",System.currentTimeMillis());
        try {
            String collectName =solrConfig.getDemoCollectName();
            String url = solrConfig.getSolrUrlByName(collectName);
            logger.info("FullIndexFreshScheduled freshIndex collectName={},url={}",collectName,url);
            String [] urls  = url.split(",");
            for(String perUrl :urls) {
                if(perUrl.endsWith("/")) {
                    perUrl= perUrl.substring(0,perUrl.length()-1);
                }
                String importUrl = perUrl+IMPORT_PATH_SUFFIX;
                HttpClientUtil.get(importUrl,null);
            }
            logger.info("FullIndexFreshScheduled freshIndex finish, collectName={}",collectName);
        } catch (Throwable e) {
            logger.error("FullIndexFreshScheduled freshIndex error,",e);
        }
    }
}
