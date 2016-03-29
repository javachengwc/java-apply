package com.solr.service;

import com.solr.model.context.MyContext;
import org.apache.solr.client.solrj.response.FacetField;

import java.util.List;
import java.util.Map;

public interface SolrDataService{

	public void dealSorlSort(Map<String, String> solrSortParameter, MyContext context);

	public void dealSolrPage(long totalSize, Map<String, String> solrSortParameter, MyContext context);

	public void dealFacetData(Map<String, Object> solrFactParameter, MyContext context, List<FacetField> facetResult);
	public void dealCatFacetData(MyContext context, List<FacetField> facetResult);

}
