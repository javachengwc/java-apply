package com.shop.book.search.solr;

import com.shop.base.model.Page;

import java.util.Map;

public interface SolrService {

    public Page<Object> querySolrPage(String collectName, Map<String, Object> queryMap, Map<String, String> sortMap,
                                      Long start, Long pageSize) throws Exception;

    public <T> Page<T> querySolrBeanPage(String collectName, Map<String, Object> queryMap, Map<String, String> sortMap,
                                               Long start, Long pageSize, Class<T> clazz) throws Exception;

    public int addOrUptDocument(String collectName, Map<String, Object> dataMap) throws Exception;

    public int deleteDocument(String collectName, String id) throws Exception;
}
