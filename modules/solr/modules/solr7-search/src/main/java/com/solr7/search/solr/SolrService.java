package com.solr7.search.solr;

import com.util.page.Page;

import java.util.Map;

public interface SolrService {

    public Page<Object> querySolrPage(String collectName, Map<String, String> queryMap, Map<String, String> sortMap,
                                      Long start, Long pageSize) throws Exception;

    public <T> Page<T> querySolrEntityPage(String collectName,Map<String, String> queryMap,Map<String, String> sortMap,
                                          Long start, Long pageSize, Class<T> clazz) throws Exception;
}
