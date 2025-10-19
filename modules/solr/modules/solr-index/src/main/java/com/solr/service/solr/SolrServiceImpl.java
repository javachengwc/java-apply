package com.solr.service.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.solr.util.SolrHelper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * solr服务类
 */
@Service
public class SolrServiceImpl implements ISolrService{

	private static Logger m_logger = LoggerFactory.getLogger(SolrServiceImpl.class);

	private List<SolrServer> servers = new ArrayList<SolrServer>();
	
	public List<SolrServer> getServers() {
		return servers;
	}

	public void setServers(List<SolrServer> servers) {
		this.servers = servers;
	}

	public SolrServiceImpl()
	{
		init();
	}
	
	public void init()
	{
		m_logger.info("SolrServiceImpl init start.");
		String servers = SolrConfig.getServers();
		int maxActive = Integer.parseInt(SolrConfig.getMaxActive());
		int connectTimeout = Integer.parseInt(SolrConfig.getConnectTimeout());
		int timeout = Integer.parseInt(SolrConfig.getTimeout());
		String[] svArry = servers.trim().split(",");
		for(int i = 0; i < svArry.length; i++)
		{
			String url = svArry[i];
			HttpSolrServer server = new HttpSolrServer(url);
			server.setConnectionTimeout(connectTimeout); 
			server.setDefaultMaxConnectionsPerHost(maxActive);
			server.setMaxTotalConnections(maxActive);
			server.setSoTimeout(timeout);
			this.servers.add(server);
		}
		m_logger.info("SolrServiceImpl init end.solrServer size ="+ this.servers.size());
	}
	
	/**
	 * 添加索引
	 * 
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public int addDocument(Map<String,String> field) throws Exception {
		int result=0;
		for(SolrServer server:servers)
		{
		    result = SolrHelper.addDocument(server, field);
		}
		return result;
	}

	/**
	 * 添加或更新索引
	 * 
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public int addOrUpdateDocument(Map<String,String> field) throws Exception {
		int result=0;
		for(SolrServer server:servers)
		{
		    result = SolrHelper.addDocument(server,field);
		}
		return result;
	}
	
	public int updateSpecFieldDocument(Map<String,String> params, Map<String,String> specField) throws Exception {
		
		List<Map<String,String>> list = searchDocument(params, 0, Integer.MAX_VALUE, "");
		int result =0;
		if (list != null && list.size() > 0) {
			for(Map<String,String> p: list)
			{
				p.putAll(specField);
				result =addOrUpdateDocument(p);
			}
		}
		return result;
	}


	/**
	 * 根据Id删除索引
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteDocument(String id) throws Exception {
		int result=0;
		for(SolrServer server:servers)
		{
		    result = SolrHelper.deleteDocument(server,id);
		}
		return result;
	}

	/**
	 * commit
	 * 
	 * @param server
	 * @throws Exception
	 */
	public void commit(SolrServer server) throws Exception
	{
		server.commit();
	}
	
    /**
     * commit all
     * 
     * @throws Exception
     */
	public void commitAll() throws Exception
	{
		for(SolrServer server:servers)
		{
		    server.commit();
		}
	}
	
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
	public List<Map<String,String>> searchDocument(Map<String,String> params, int startIndex,int pageSize, String sortField) throws Exception {
		
		
		SolrServer server = null;
		if(servers !=null && servers.size()>0)
		{
			int index  = (new Random().nextInt(servers.size()))%servers.size();
			server =servers.get(index);
		}else
		{
			return null;
		}
		List<Map<String,String>> result = SolrHelper.searchDocument(server,params, startIndex, pageSize, sortField);
		return result;
	}
	
	public static void main(String args [])
	{
		HttpSolrServer server = new HttpSolrServer("http://127.0.0.1:10001/solr/goods");
		server.setConnectionTimeout(5000); 
		server.setDefaultMaxConnectionsPerHost(200);
		server.setMaxTotalConnections(200);
		server.setSoTimeout(5000);
		Map<String,String> map =new HashMap<String,String>();
		map.put("id", "1");
        try{
			List<Map<String,String>> list =SolrHelper.searchDocument(server,map, 0, 1, "");
			
			System.out.println("list.size="+list==null?"0":list.size());
        }catch(Exception e)
        {
        	e.printStackTrace(System.out);
        }
	}

}
