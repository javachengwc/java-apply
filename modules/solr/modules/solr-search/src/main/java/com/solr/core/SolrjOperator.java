package com.solr.core;

import com.solr.model.Price;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;
import java.util.Map;

public interface SolrjOperator {

	/**
	 * 获得搜索结果
	 */
	public List<Object> querySolrResult(Map<String, String> propertyMap,
                                        Map<String, String> compositorMap, Long startIndex, Long pageSize)
			throws Exception;
	/**
	 * 把查询结果转化为对应的bean
	 */
	public List<Object> querySolrBeanResult(Map<String, String> propertyMap,
                                            Map<String, String> compositorMap, Long startIndex, Long pageSize, Class clazz)
			throws Exception;

	/**
	 * 获得搜索结果条数
	 */
	public Long querySolrResultCount() throws Exception;
	
	/**
	 * 处理facet查询
	 */
	public  List<FacetField> factQuery(Map<String, Object> factParameterMap) throws Exception;

    /**
	 * 分类facet查询
	 */
	public  List<FacetField> factCatQuery(String facetQuery, String facetField) throws Exception;

    /**
     * facet范围查询
     */
	public QueryResponse factQuery(Map<String, String> solrQueryMap, List<String> facet_query, List<String> facetField)  throws Exception;

    /**
	 * 带查询参数的facet查询
	 */
	public List<FacetField> factQuery(Map<String, String> solrQueryMap, Map<String, Object> factParameterMap) throws Exception;

    public  Map<String, Integer> factQuery(Map<String, Object> priceFacetParameter, List<Price> priceList) throws Exception;
}
