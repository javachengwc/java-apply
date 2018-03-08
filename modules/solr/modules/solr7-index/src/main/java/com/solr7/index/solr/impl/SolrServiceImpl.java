package com.solr7.index.solr.impl;

import com.solr7.index.config.SolrConfig;
import com.solr7.index.solr.SolrClientFactory;
import com.solr7.index.solr.SolrOperate;
import com.solr7.index.solr.SolrService;
import com.util.page.Page;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SolrServiceImpl implements SolrService {

    private static Logger logger = LoggerFactory.getLogger(SolrServiceImpl.class);

    private static Long zeroLong =0L;

    private static String SOLR_COLLECT_SYN_PRE="solr_collect_";

    @Autowired
    private SolrConfig solrConfig;

    private ConcurrentHashMap<String,List<SolrOperate>> solrOperateMap = new ConcurrentHashMap<String,List<SolrOperate>>();

    public List<SolrOperate> gainSolrOperate(String collectName) {
        logger.info("SolrServiceImpl gainSolrOperate collectName={}",collectName);
        List<SolrOperate> solrOptList = solrOperateMap.get(collectName);
        if(solrOptList==null) {
            String synStr=SOLR_COLLECT_SYN_PRE+collectName;
            synchronized (synStr.intern()) {
                solrOptList = solrOperateMap.get(collectName);
                if(solrOptList==null) {
                    String url = solrConfig.getSolrUrlByName(collectName);
                    logger.info("SolrServiceImpl gainSolrOperate collectName={},url={}",collectName,url);
                    String [] urls  = url.split(",");
                    solrOptList = new ArrayList<SolrOperate>();
                    for(String perUrl :urls) {
                        SolrClient solrClient = SolrClientFactory.getSolrClient(perUrl);
                        SolrOperate solrOperate = new SolrOperate(solrClient);
                        solrOptList.add(solrOperate);
                    }
                    solrOperateMap.put(collectName,solrOptList);
                }
            }
        }
        return solrOptList;
    }

    public Page<Object> querySolrPage(String collectName, Map<String, String> queryMap, Map<String, String> sortMap,
                                      Long start, Long pageSize) throws Exception {
        List<SolrOperate> solrOperateList = gainSolrOperate(collectName);
        int optCnt = solrOperateList.size();
        int index  = (new Random().nextInt(optCnt))%optCnt;
        SolrOperate solrOperate =solrOperateList.get(index);

        SolrDocumentList docList = solrOperate.query(queryMap, sortMap,start, pageSize);
        Long totalCount=docList==null?zeroLong:docList.getNumFound();
        Integer docSize = docList==null?0:docList.size();
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

    public int addOrUptDocument(String collectName,Map<String, Object> dataMap) throws Exception {
        List<SolrOperate> solrOperateList = gainSolrOperate(collectName);
        int rt=0;
        for(SolrOperate solrOperate:solrOperateList) {
            rt = solrOperate.addDocument(dataMap);
        }
        return rt;
    }

    public int deleteDocument(String collectName,String id) throws Exception {
        List<SolrOperate> solrOperateList = gainSolrOperate(collectName);
        int rt=0;
        for(SolrOperate solrOperate:solrOperateList) {
            rt = solrOperate.deleteDocument(id);
        }
        return rt;
    }
}
