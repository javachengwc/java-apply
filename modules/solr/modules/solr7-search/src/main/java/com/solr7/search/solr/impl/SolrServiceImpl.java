package com.solr7.search.solr.impl;

import com.solr7.search.config.SolrConfig;
import com.solr7.search.solr.SolrClientFactory;
import com.solr7.search.solr.SolrOperate;
import com.solr7.search.solr.SolrService;
import com.util.page.Page;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SolrServiceImpl implements SolrService {

    private static Logger logger = LoggerFactory.getLogger(SolrServiceImpl.class);

    private static Long zeroLong =0L;

    private static String SOLR_COLLECT_SYN_PRE="solr_collect_";

    @Autowired
    private SolrConfig solrConfig;

    private ConcurrentHashMap<String,SolrOperate> solrOperateMap = new ConcurrentHashMap<String,SolrOperate>();

    public SolrOperate gainSolrOperate(String collectName) {
        logger.info("SolrServiceImpl gainSolrOperate collectName={}",collectName);
        SolrOperate solrOperate = solrOperateMap.get(collectName);
        if(solrOperate==null) {
            String synStr=SOLR_COLLECT_SYN_PRE+collectName;
            synchronized (synStr.intern()) {
                solrOperate = solrOperateMap.get(collectName);
                if(solrOperate==null) {
                    String url = solrConfig.getSolrUrlByName(collectName);
                    SolrClient solrClient = SolrClientFactory.getSolrClient(url);
                    solrOperate = new SolrOperate(solrClient);
                    solrOperateMap.put(collectName,solrOperate);
                    logger.info("SolrServiceImpl gainSolrOperate collectName={},url={}",collectName,url);
                }
            }
        }
        return solrOperate;
    }

    public Page<Object> querySolrPage(String collectName, Map<String, String> queryMap, Map<String, String> sortMap,
                                      Long start, Long pageSize) throws Exception {
        SolrOperate solrOperate =gainSolrOperate(collectName);
        SolrDocumentList docList = solrOperate.query(queryMap, sortMap,start, pageSize);
        Integer docSize = docList==null?0:docList.size();
        Long totalCount=docList==null?zeroLong:docList.getNumFound();
        List<Object> resultList = new ArrayList<Object>();
        if(docSize>0) {
            for (int i = 0; i < docSize; i++) {
                resultList.add(docList.get(i));
            }
        }
        Page<Object> page = new Page<Object>();
        page.setResult(resultList);
        page.setTotalCount(totalCount.intValue());
        return page;
    }

    public <T> Page<T> querySolrEntityPage(String collectName,Map<String, String> queryMap,Map<String, String> sortMap,
                                          Long start, Long pageSize, Class<T> clazz) throws Exception {
        SolrOperate solrOperate =gainSolrOperate(collectName);
        SolrDocumentList docList = solrOperate.query(queryMap, sortMap,start, pageSize);
        Integer docSize = docList==null?0:docList.size();
        Long totalCount=docList==null?zeroLong:docList.getNumFound();
        List<T> resultList=new ArrayList<T>();
        if(docSize>0) {
            DocumentObjectBinder binder = new DocumentObjectBinder();
            resultList=binder.getBeans(clazz, docList);
        }
        Page<T> page = new Page<T>();
        page.setResult(resultList);
        page.setTotalCount(totalCount.intValue());
        return page;
    }
}
