package com.solr.service.ru;

import com.solr.model.RuProduct;
import com.solr.model.context.MyContext;
import com.solr.core.DefaultSolrOperator;
import com.solr.core.SolrjOperator;
import com.solr.core.SolrjQuery;
import com.solr.util.ServiceCfg;
import com.solr.util.SolrServerFactory;
import com.solr.service.ResultHandler;
import com.solr.service.SolrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class RuSolrService implements SolrService {

	private SolrServer server;
	private ResultHandler resultHandler;
	private RuUrlService urlService;
	private RuSolrDataService dataService;
	
	public RuSolrService(RuUrlService urlService, RuSolrDataService dataService, ResultHandler resultHandler){
		this.urlService = urlService;
		this.dataService = dataService;
		this.resultHandler = resultHandler;
		String solrRequestUrl = ServiceCfg.getServiceParameterByName(RuUrlService.serviceName).get("solrRequestUrl");
		this.server = SolrServerFactory.getSolrServerInstance(solrRequestUrl);
	}
	
	public boolean dealSolrService(HttpServletRequest request,HttpServletResponse response) {
		urlService.genRealServerName(request);
		
		if (!urlService.isValidateUrl(request.getRequestURI())) {
			return false;
		}
		String keywords = request.getParameter("q");
		if (StringUtils.isNotEmpty(keywords)) {
			urlService.setSearchKeywords(keywords);
		}
		String requestUrl = urlService.getAndformatUrl(request,urlService.getServiceName(), 6, true);
		// /category/0-853-0-0-0-0.html
		//去掉serviceName
		String pre ="/"+ RuUrlService.serviceName;
		if(!StringUtils.isBlank(requestUrl) && requestUrl.startsWith(pre))
		{
			int len = pre.length();
			requestUrl =requestUrl.substring(len);
		}
		urlService.setRequestUrl(requestUrl);
		urlService.parseUrlParameter(requestUrl);
		urlService.buildFacetParameter();
		MyContext context = new MyContext();
		try{
			// 处理url参数，获取请求参数，获取solrQuery的参数
			Map<String, String> solrQueryParameter = urlService.getSolrQueryParameter();
			// 处理url参数，获取solr排序的参数
			Map<String, String> solrSortParameter = urlService.buildSolrSortParameter();
			// 得到Solr查询处理对象
			SolrjQuery solrjQuery = new SolrjQuery(server);
			SolrjOperator solrOprator = new DefaultSolrOperator(solrjQuery);
			// 处理数据查询
			dealSolrQuery(solrQueryParameter, solrSortParameter, solrOprator, context);
			// 处理排序数据
			dealSorlSort(urlService.getSolrSortParameter(), context);
			// // 处理分页参数
			dealSolrPage(solrQueryParameter, urlService.getSolrPageParameter(), solrOprator, context);
			//处理分类tab
			dealCatList(solrQueryParameter,context);
			//处理价格tab
			dataService.dealPrice(context);
			
			dataService.dealRelatedSearchAndSeoInfo(solrQueryParameter, context);
			//处理关联文章以及导航
			dataService.dealOtherDatas(solrQueryParameter,context);
			
			resultHandler.handlerResultData(response, context);
		}catch(Exception e){e.printStackTrace();}finally{
			
		}
		return false;
	}

	private void dealSolrQuery(Map<String, String> solrQueryParameter,
			Map<String, String> solrSortParameter, SolrjOperator solrOprator,
			MyContext context) throws Exception {
		Long startIndex = Long.parseLong((String) urlService.getSolrPageParameter().get("startPage"));
		Long pageSize = Long.parseLong((String) urlService.getSolrPageParameter().get("pageSize"));
		List<Object> productList = solrOprator.querySolrBeanResult(solrQueryParameter, solrSortParameter, (startIndex - 1) * pageSize, pageSize,
				RuProduct.class);
		context.set("goods_list", productList);
	}

	private void dealSorlSort(Map<String, String> solrSortParameter,MyContext context) {
		dataService.dealSorlSort(solrSortParameter, context);
	}

	private void dealSolrPage(Map<String, String> solrQueryParameter,
			Map<String, String> solrPageParameter, SolrjOperator solrOprator,
			MyContext context) throws Exception {
		long totalSize = solrOprator.querySolrResultCount();
		dataService.dealSolrPage(totalSize, solrPageParameter, context);
	}
	
	private void dealCatList(Map<String, String> solrQueryParameter, MyContext context)
	{
		dataService.dealCatList(solrQueryParameter, context);
	}
}
