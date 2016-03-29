package com.solr.service.solr;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;

public interface ISolrService {
	
	/**
	 * 添加索引
	 * 
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public int addDocument(Map<String, String> field) throws Exception;

	/**
	 * 更新索引
	 * 
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public int addOrUpdateDocument(Map<String, String> field) throws Exception;
	
	/**
	 * 更新特定Field的索引
	 * @param params
	 * @param specField
	 * @return
	 */
	public int updateSpecFieldDocument(Map<String, String> params, Map<String, String> specField) throws Exception;
	
	/**
	 * 根据id删除索引
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteDocument(String id) throws Exception;

	/**
	 * commit
	 * 
	 * @param server
	 * @throws Exception
	 */
	public void commit(SolrServer server) throws Exception;
	
    /**
     * commit all
     * 
     * @throws Exception
     */
	public void commitAll() throws Exception;
	
	/**
	 * 搜索
	 * 
	 * @param params
	 * @param startIndex
	 * @param pageSize
	 * @param sortField
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> searchDocument(Map<String, String> params, int startIndex, int pageSize, String sortField) throws Exception;
}
