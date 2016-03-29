package com.solr.core;

import com.solr.model.Price;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultSolrOperator implements SolrjOperator {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultSolrOperator.class);
	private SolrjQuery solrjQuery;
	private SolrDocumentList solrDocumentList;
	
	public DefaultSolrOperator(SolrjQuery solrjQuery) {
		this.solrjQuery = solrjQuery;
	}


	public void setSolrjQuery(SolrjQuery solrjQuery) {
		this.solrjQuery = solrjQuery;
	}

	
	public List<Object> querySolrResult(Map<String, String> propertyMap,
			Map<String, String> compositorMap, Long startIndex, Long pageSize)
			throws Exception {
		getSolrDocumentList(propertyMap, compositorMap, startIndex, pageSize);
		List<Object> resultList = new ArrayList<Object>();
		for (int i = 0; i < solrDocumentList.size(); i++) {
			resultList.add(solrDocumentList.get(i));
		}
		return resultList;
	}
	
	public long querySolrResultCount(Map<String, String> propertyMap,
			Map<String, String> compositorMap, Long startIndex, Long pageSize)
			throws Exception {
		getSolrDocumentList(propertyMap, compositorMap, startIndex, pageSize);
		List<Object> resultList = new ArrayList<Object>();
		for (int i = 0; i < solrDocumentList.size(); i++) {
			resultList.add(solrDocumentList.get(i));
		}
		long resultCount=solrDocumentList.getNumFound();
		solrDocumentList=null;
		return resultCount;
	}
	
	public List<Object> querySolrBeanResult(Map<String, String> propertyMap,
			Map<String, String> compositorMap, Long startIndex, Long pageSize,Class clazz)
			throws Exception {
		getSolrDocumentList(propertyMap, compositorMap, startIndex, pageSize);
		//初始化DocumentObjectBinder对象
        DocumentObjectBinder binder = new DocumentObjectBinder();
        //SolrDocumentList对象转化为 List<BlogsDO>对象
        List<Object> resultList=binder.getBeans(clazz, solrDocumentList);
        return resultList;
	}

	private SolrDocumentList getSolrDocumentList(Map<String, String> propertyMap, Map<String, String> compositorMap, Long startIndex, Long pageSize)
			throws Exception {
		 solrDocumentList = solrjQuery.query(propertyMap, compositorMap,startIndex, pageSize);
		return solrDocumentList;
	}

	public Long querySolrResultCount(){
		long numFound=solrDocumentList.getNumFound();
		solrDocumentList=null;
		return numFound;
	}
	
	public List<FacetField> factQuery(Map<String, Object> factParameterMap) throws Exception {
		return solrjQuery.factQuery(factParameterMap);
	}
	
	public QueryResponse factQuery(Map<String,String> solrQueryMap,List<String> facet_query, List<String> facetField)  throws Exception{
		return solrjQuery.factQuery(solrQueryMap,facet_query,facetField);
	}


	public List<FacetField> factCatQuery(String facetQuery,String facetField)throws Exception {
		return solrjQuery.factCatQuery(facetQuery,facetField);
	}


	public List<FacetField> factQuery(Map<String, String> solrQueryMap, Map<String, Object> factParameterMap) throws Exception {
		return solrjQuery.factQuery(solrQueryMap,factParameterMap);
	}


    /** 
     * @param priceFacetParameter
     */
    public  Map<String, Integer> factQuery(Map<String, Object> priceFacetParameter, List<Price> priceList) throws Exception {
        return solrjQuery.factQuery(priceFacetParameter,priceList);
    }
}
