package com.solr7.index.solr;

import com.util.page.Page;

import java.util.Map;

public interface SolrService {

    public Page<Object> querySolrPage(String collectName, Map<String, String> queryMap, Map<String, String> sortMap,
                                      Long start, Long pageSize) throws Exception;

    public int addOrUptDocument(String collectName,Map<String, Object> dataMap) throws Exception;

    public int deleteDocument(String collectName,String id) throws Exception;
}
