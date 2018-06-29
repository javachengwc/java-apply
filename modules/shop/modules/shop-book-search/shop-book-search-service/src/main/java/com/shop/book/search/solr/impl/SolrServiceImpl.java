package com.shop.book.search.solr.impl;

import com.shop.base.model.Page;
import com.shop.book.search.config.SolrConfig;
import com.shop.book.search.solr.SolrClientFactory;
import com.shop.book.search.solr.SolrService;
import com.shop.book.search.solr.SolrjOperator;
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

    private ConcurrentHashMap<String,SolrjOperator> solrjQueryMap = new ConcurrentHashMap<String,SolrjOperator>();

    public SolrjOperator gainSolrjQuery(String collectName) {
        logger.info("SolrServiceImpl gainSolrjQuery collectName={}",collectName);
        SolrjOperator solrjQuery = solrjQueryMap.get(collectName);
        if(solrjQuery==null) {
            String synStr=SOLR_COLLECT_SYN_PRE+collectName;
            synchronized (synStr.intern()) {
                solrjQuery = solrjQueryMap.get(collectName);
                if(solrjQuery==null) {
                    String url = solrConfig.getSolrUrlByCollect(collectName);
                    logger.info("SolrServiceImpl gainSolrjQuery collectName={},url={}",collectName,url);
                    SolrClient solrClient = SolrClientFactory.getSolrSolrClientInstance(url);
                    solrjQuery = new SolrjOperator(solrClient);
                    solrjQueryMap.put(collectName,solrjQuery);
                }
            }
        }
        return solrjQuery;
    }

    public Page<Object> querySolrPage(String collectName, Map<String, Object> queryMap, Map<String, String> sortMap,
                                      Long start, Long pageSize) throws Exception {
        SolrjOperator solrjQuery =gainSolrjQuery(collectName);
        SolrDocumentList docList = solrjQuery.query(queryMap, sortMap,start, pageSize);
        Integer docSize = docList==null?0:docList.size();
        List<Object> resultList = new ArrayList<Object>();
        if(docSize>0) {
            for (int i = 0; i < docSize; i++) {
                resultList.add(docList.get(i));
            }
        }
        Long totalCount=docList==null?zeroLong:docList.getNumFound();
        Page<Object> page = new Page<Object>();
        page.setList(resultList);
        page.setTotalCount(totalCount.intValue());
        return page;
    }

    public <T> Page<T> querySolrBeanPage(String collectName,Map<String, Object> queryMap,Map<String, String> sortMap,
                                          Long start, Long pageSize, Class<T> clazz) throws Exception {
        SolrjOperator solrjQuery =gainSolrjQuery(collectName);
        SolrDocumentList docList = solrjQuery.query(queryMap, sortMap,start, pageSize);
        Integer docSize = docList==null?0:docList.size();
        Long totalCount=docList==null?zeroLong:docList.getNumFound();
        List<T> resultList=new ArrayList<T>();
        if(docSize>0) {
            DocumentObjectBinder binder = new DocumentObjectBinder();
            resultList=binder.getBeans(clazz, docList);
        }
        Page<T> page = new Page<T>();
        page.setList(resultList);
        page.setTotalCount(totalCount.intValue());
        return page;
    }

    public int addOrUptDocument(String collectName,Map<String, Object> dataMap) throws Exception {
        SolrjOperator solrjOperator = gainSolrjQuery(collectName);
        int rt = solrjOperator.addDocument(dataMap);
        return rt;
    }

    public int deleteDocument(String collectName,String id) throws Exception {
        SolrjOperator solrjOperator = gainSolrjQuery(collectName);
        int rt = solrjOperator.deleteDocument(id);
        return rt;
    }
}
