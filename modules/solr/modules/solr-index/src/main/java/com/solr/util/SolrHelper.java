package com.solr.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.util.*;


/**
 * Solrj 客户端铺助类  
 */
public class SolrHelper  
{
	
	/**
	 * 增加或更新索引文档
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	public static int addDocument(SolrServer server,Map<String,String> fields) throws Exception{
		
		SolrInputDocument sid = new SolrInputDocument();
		for(String key : fields.keySet())
		{
			sid.addField(key, fields.get(key));
		}
		UpdateResponse updateResponse = server.add(sid);
		 
		server.commit();
		
		return updateResponse.getStatus();
	}
	
	/**
	 * 删除索引文档
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static int deleteDocument(SolrServer server,String id) throws Exception{
		UpdateResponse updateResponse = server.deleteById(id);
		server.commit();
		return updateResponse.getStatus();
        
	}
	
	/**
	 * 分页查询索引文档
	 * @param params
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,String>> searchDocument(SolrServer server,Map<String,String> params, int startIndex, int pageSize,String sortField) throws Exception{
		StringBuilder queryString = new StringBuilder();
		for(String key : params.keySet())
		{
			String queryField = key;
			String queryValue = params.get(key);
			if(queryString.length() > 0){
				queryString.append(" OR ");
			}
			queryString.append(queryField+":\""+ DataFilter.stringFilter(queryValue)+"\"");
		}
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString.toString());
		if(!StringUtils.isBlank(sortField)){
			query.addSortField(sortField, SolrQuery.ORDER.desc );
		}
		query.setStart(startIndex);
		query.setRows(pageSize);  
		QueryResponse response = server.query(query);
		SolrDocumentList list = response.getResults();
		List<Map<String,String>> results = new ArrayList<Map<String,String>>();
		for(int i = 0; i < list.size(); i++)
		{
			SolrDocument d = list.get(i);
			Map<String,String> p = new HashMap<String,String>();
			for(String key : d.keySet())
			{
				Object value = d.get(key);
				if(value == null){
					p.put(key, "");
				}else{
					if(value instanceof Date){
						p.put(key, String.valueOf(((Date)value).getTime()));
					}else{
						p.put(key, value.toString());
					}
				}
			}	
			results.add(p);
		}
		return results;
	}
		
	/**
	 * 分页查询索引文档
	 * @param params 查询条件key-value对
	 * @param startIndex 开始索引
	 * @param pageSize 返回页的大小
	 * @param startIndex
	 * @param sortField 排序字段
	 * @param extendsCondition 查询附加条件 ,key-value对
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,String>> searchDocument(SolrServer server,Map<String,String> params, int startIndex, int pageSize, String sortField,Map<String,String> extendsCondition) throws Exception{
		String searchCondition = "";
		StringBuilder queryString = new StringBuilder();
		for(String key : params.keySet())
		{
			String queryField = key;
			String queryValue = params.get(key);
			if(queryString.length() > 0){
				queryString.append(" OR ");
			}
			queryString.append(queryField+":\""+DataFilter.stringFilter(queryValue)+"\"");
		}
		if(null !=extendsCondition && extendsCondition.size() > 0){
			StringBuilder extendsQueryString = new StringBuilder();
			for(String key : extendsCondition.keySet())
			{
				String queryField = key;
				String queryValue = extendsCondition.get(key);
				if(extendsQueryString.length() > 0){
					extendsQueryString.append(" AND ");
				}
				extendsQueryString.append(queryField+":\""+DataFilter.stringFilter(queryValue)+"\"");
			}
			searchCondition = "( " + queryString.toString()+ " )" + " AND ( " + extendsQueryString.toString() + " ) ";
		}else{
			searchCondition = queryString.toString();
		}
		SolrQuery query = new SolrQuery();
		//query.setQuery(queryString.toString());
		query.setQuery(searchCondition);
		if(!StringUtils.isBlank(sortField)){
			query.addSortField(sortField, SolrQuery.ORDER.desc );
		}
		query.setStart(startIndex);
		query.setRows(pageSize);  
		QueryResponse response = server.query(query);
		SolrDocumentList list = response.getResults();
		List<Map<String,String>> results = new ArrayList<Map<String,String>>();
		for(int i = 0; i < list.size(); i++)
		{
			SolrDocument d = list.get(i);
			Map<String,String> p = new HashMap<String,String>();
			for(String key : d.keySet())
			{
				Object value = d.get(key);
				if(value == null){
					p.put(key, "");
				}else{
					if(value instanceof Date){
						p.put(key, String.valueOf(((Date)value).getTime()));
					}else{
						p.put(key, value.toString());
					}
				}
			}
			results.add(p);
		}
		return results;
	}
}

