package com.solr.service.ru;

import com.solr.initialize.InitRuDao;
import com.solr.model.Price;
import com.solr.model.context.MyContext;
import com.solr.util.ServiceCfg;
import com.solr.model.UsualTitleInfo;
import com.solr.service.SolrDataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;

import java.util.*;

public class RuSolrDataService implements SolrDataService {
	protected RuUrlService urlService;
	private Map<String,String> solrQueryParameter;
	private List<String> baseFacetField=null;

	//private static String FIRST_PAGE_NAME="Главная";//首页

	private static String SEARCH_NAME=StringUtils.capitalize("поиск");//搜索

	public RuSolrDataService(RuUrlService urlService){
		this.urlService=urlService;
		solrQueryParameter=this.urlService.getSolrQueryParameter();
		baseFacetField=new ArrayList<String>();
		baseFacetField.add("mdname");
		baseFacetField.add("sdname");
		baseFacetField.add("bdname");
	}

	/**
	 * 排序处理
	 */
	public void dealSorlSort(Map<String, String> solrSortParameter,MyContext context) {
		String order_total_sold_yes_count_url=null;
		if("total_sold_yes_count".equals(solrSortParameter.get("sort"))){
			if("desc".equals(solrSortParameter.get("order"))){
				order_total_sold_yes_count_url=urlService.build2ParamUrl(3,String.valueOf(1),4,String.valueOf(1));
			}else{
				order_total_sold_yes_count_url=urlService.build2ParamUrl(3,String.valueOf(1),4,String.valueOf(2));
			}
		}else{
			order_total_sold_yes_count_url=urlService.build2ParamUrl(3,String.valueOf(1),4,String.valueOf(1));
		}
		String order_effect_price_url=null;
		if("effect_price".equals(solrSortParameter.get("sort"))){
			if("desc".equals(solrSortParameter.get("order"))){
				order_effect_price_url=urlService.build2ParamUrl(3,String.valueOf(2),4,String.valueOf(1));
			}else{
				order_effect_price_url=urlService.build2ParamUrl(3,String.valueOf(2),4,String.valueOf(2));
			}
		}else{
			order_effect_price_url=urlService.build2ParamUrl(3,String.valueOf(2),4,String.valueOf(1));
		}
		String order_click_count_url=null;
		if("click_count".equals(solrSortParameter.get("sort"))){
			if("desc".equals(solrSortParameter.get("order"))){
				order_click_count_url=urlService.build2ParamUrl(3,String.valueOf(3),4,String.valueOf(1));
			}else{
				order_click_count_url=urlService.build2ParamUrl(3,String.valueOf(3),4,String.valueOf(2));
			}
		}else{
			order_click_count_url=urlService.build2ParamUrl(3,String.valueOf(3),4,String.valueOf(1));
		}
		String order_add_time_url=null;
		if("add_time".equals(solrSortParameter.get("sort"))){
			if("desc".equals(solrSortParameter.get("order"))){
				order_add_time_url=urlService.build2ParamUrl(3,String.valueOf(4),4,String.valueOf(1));
			}else{
				order_add_time_url=urlService.build2ParamUrl(3,String.valueOf(4),4,String.valueOf(2));
			}
		}else{
			order_add_time_url=urlService.build2ParamUrl(3,String.valueOf(4),4,String.valueOf(1));
		}
		Map<String,String> sort = new HashMap<String, String>();
		sort.put("order_total_sold_yes_count_url",order_total_sold_yes_count_url);
		sort.put("order_effect_price_url",order_effect_price_url);
		sort.put("order_click_count_url",order_click_count_url);
		sort.put("order_add_time_url",order_add_time_url);
		sort.put("order",solrSortParameter.get("order"));
		sort.put("sort",solrSortParameter.get("sort"));

		//重置
//		String cat_id = solrQueryParameter.get("cat_id");
//		String parent_id = solrQueryParameter.get("parent_id");
//		String re_select_url = null;

		context.set("sortArray", sort);
	}
    /**
     * 分页处理
     */
	public void dealSolrPage(long totalSize,Map<String, String> solrPageParameter, MyContext context) {
		Long startIndex=Long.parseLong((String) solrPageParameter.get("startPage"));
		Long pageSize=Long.parseLong((String) solrPageParameter.get("pageSize"));
		Map<String,Object> tempsMap =doPageDatas(totalSize, startIndex,String.valueOf(pageSize));
		Map<String, Object> pagerMap=new HashMap<String, Object>();
		Set<String> sets = tempsMap.keySet();
		for(String str : sets){
			pagerMap.put(str, tempsMap.get(str));
		}
		//context.set("num_founds", eachCount);
//		context.set("max_page", tempsMap.get("page_count"));
		int currentPage = Integer.parseInt(String.valueOf(tempsMap.get("page")));
		int pages = Integer.parseInt(String.valueOf(tempsMap.get("page_count")));
		int dopage_url_next = 0;
		int dopage_url_previous = 0;
		if(currentPage < pages){
			dopage_url_next = currentPage+1;
		}else{
			dopage_url_next = currentPage;
		}
		if(currentPage > 1){
			dopage_url_previous = currentPage - 1;
		}else{
			dopage_url_previous = currentPage;
		}
		List doPageUrlList = (ArrayList) tempsMap.get("array");
		for(int i = 0; i<doPageUrlList.size(); i++){

			Map<String,String> pageM = new HashMap<String, String>();
			pageM.put("url",urlService.buildNumUrl(5,String.valueOf(doPageUrlList.get(i))));
			pageM.put("page_num", String.valueOf(doPageUrlList.get(i)));
			doPageUrlList.set(i, pageM);
		}
		Map<String,Object> pager=new HashMap<String,Object>();
		pager.put("dopage_url_list", doPageUrlList);
		pager.put("current_page", tempsMap.get("page"));
		pager.put("dopage_url_next",urlService.buildNumUrl(5,String.valueOf(dopage_url_next)));
		pager.put("dopage_url_previous",urlService.buildNumUrl(5,String.valueOf(dopage_url_previous)));
		pager.put("dopage_url_first",urlService.buildNumUrl(5,String.valueOf(1)));
		pager.put("dopage_url_end", urlService.buildNumUrl(5,String.valueOf(pages)));
//		context.set("page_count", tempsMap.get("page_count"));
//		context.set("record_count", tempsMap.get("record_count"));
		//处理分页参数
		pager.put("record_count",tempsMap.get("record_count"));
		//pager.put("pageSize",String.valueOf(pageSize));
		//pager.put("page",tempsMap.get("page"));
		pager.put("page_count",tempsMap.get("page_count"));
		//pager.put("start",String.valueOf((currentPage-1)*pageSize));
		//pager.put("array", doPageUrlList);
		context.set("pager",pager);
		pager = null;
		tempsMap=null;
		doPageUrlList=null;
	}

	/**获取分页数据*/
	private static Map<String, Object> doPageDatas(long count,long startsPage,String rows){
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put("record_count", count);

		/**从多少条开始*/
		/**一页显示的条数*/
		int pageSize = Integer.parseInt(rows);
		pagerMap.put("start", startsPage*pageSize);
		pagerMap.put("pageSize", pageSize);

		/**计算一共多少页*/
		long pages = count%pageSize;
		if(pages == 0){
			pages = count/pageSize;
		}else{
			pages = count/pageSize+1;
		}

		pagerMap.put("page_count", pages);
		/**当前页*/
		pagerMap.put("page", startsPage);

		ArrayList pagesNumList = new ArrayList();

		int allPages = 9;
		long currentPage = startsPage;
		long lower = 0;
		long higher = 0;
		if(currentPage <= 5){
			if(pages <= allPages){
				lower = 1;
				higher = pages;
			}else{
				lower = 1;
				higher = allPages;
			}
		}else if(currentPage <= 100){
			if(pages-4 >= currentPage ){
				lower = currentPage - 4;
				higher = currentPage + 4;
			}else{
				lower = currentPage - 4;
				higher = pages;
			}
		}else{
			if(pages-3 >= currentPage ){
				lower = currentPage - 3;
				higher = currentPage + 3;
			}else{
				lower = currentPage - 3;
				higher = pages;
			}
		}
		for(long p=lower; p <= higher ; p++){
			pagesNumList.add(p);
		}
		pagerMap.put("array", pagesNumList);
		return pagerMap;
	}

	public void dealFacetData(Map<String, Object> solrFactParameter,MyContext context, List<FacetField> facetResult) {
		//do nothing...
	}

	public void dealCatFacetData(MyContext context, List<FacetField> facetResult) {
		// do nothing...
	}

	@SuppressWarnings("unchecked")
	public void dealOtherDatas(Map<String, String> solrQueryParameter, MyContext context) {

		String keywords =solrQueryParameter.get("keywords");
		if(!StringUtils.isBlank(keywords))
		{
			changeNav(SEARCH_NAME+" \""+keywords+"\" ","",context);
		}
		List<Map<String,String>> current_nav =(List<Map<String,String>>)context.getValueMap().get("current_nav");
		if(current_nav==null){
			context.set("current_nav", new ArrayList<Map<String,String>>());
		}
	}

	/**设置热门搜索关键词信息*/
	public void dealRelatedSearchAndSeoInfo(Map<String, String> solrQueryParameter, MyContext context) {


    	String cat_id = solrQueryParameter.get("cat_id");
    	String parent_id = solrQueryParameter.get("parent_id");
    	String searchKey =solrQueryParameter.get("keywords");

    	Map<String,String> map=null;

    	if(!StringUtils.isBlank(searchKey))
    	{
    		map = InitRuDao.getSearchSeoInfo();
    		if(map!=null)
    		{
    			String replaStr = InitRuDao.getSearchReplaChar();
    			String title = (map.get("title")==null)?"":map.get("title");
    			String description = (map.get("description")==null)?"":map.get("description");
    			String kwd =  (map.get("keywords")==null)?"":map.get("keywords");
    			title = title.replace(replaStr, searchKey);
    			description = description.replace(replaStr, searchKey);
    			kwd = kwd.replace(replaStr, searchKey);
    			map = new HashMap<String,String>();
    			map.put("title", title);
    			map.put("description", description);
    			map.put("keywords", kwd);

    		}

    	}else if(!StringUtils.isBlank(cat_id) && !"0".equals(cat_id))
    	{
    		map = InitRuDao.getSeoInfoByCat(cat_id);
    	}
    	else
    	{
    		if(StringUtils.isBlank(parent_id) || "0".equals(parent_id))
	    	{
	    		parent_id = InitRuDao.getTopCat().getCat_id();
	    	}
	    	if(!StringUtils.isBlank(parent_id) )
	    	{
	    		map = InitRuDao.getSeoInfoByCat(parent_id);
	    	}
	    }
    	if(map==null){

    		map = new HashMap<String,String>();
    	}
    	if(map.get("title")==null){ map.put("title", "");};
    	if(map.get("description")==null){ map.put("description", "");};
    	if(map.get("keywords")==null){ map.put("keywords", "");};

    	context.set("seoInfo", map);


	}
	/**
	 * 组装价格信息
	 * @param context
	 */
	public final void dealPrice(MyContext context) {
		List<Price> prices = ServiceCfg.getPriceListByName(RuUrlService.serviceName);
		List<Map<String,String>> priceList = new ArrayList<Map<String,String>>();
		Map<String,String> priceInfo = null;
	
		for(int i=0;i<prices.size();i++){
			priceInfo = new HashMap<String, String>();
			Price price = prices.get(i);
			if(price.getBegin()==urlService.getPriceBegin()&&price.getEnd()==urlService.getPriceEnd()){
				
				priceInfo.put("selected", "1");
				priceInfo.put("url", urlService.buildTabUrl(2, "0","0"));
				
			}else
			{
				priceInfo.put("selected", "0");
				priceInfo.put("url", urlService.buildTabUrl(2, String.valueOf(i+1),"0"));
			}
			priceInfo.put("id", String.valueOf(i+1));
			priceInfo.put("name", price.getName());
			
			priceList.add(priceInfo);
		}
		
		context.set("price_list", priceList);
	}
	/**
	 * 组装分类信息
	 * @param solrQueryParameter
	 * @param context
	 */
	public void dealCatList(Map<String, String> solrQueryParameter, MyContext context)
	{
		String cat_id=solrQueryParameter.get("cat_id");
		int selectedCatId=-1;
		if(!StringUtils.isBlank(cat_id))
		{
			selectedCatId=Integer.parseInt(cat_id);
		}
		List<UsualTitleInfo> list = InitRuDao.getCatList();
		List<Map<String,String>> catList = new ArrayList<Map<String,String>>();
		Map<String,String> catInfo = null;
		for(int i=0;i<list.size();i++){
			catInfo = new HashMap<String, String>();
			UsualTitleInfo info = list.get(i);
			catInfo.put("cat_id", info.getCat_id());
			catInfo.put("name", info.getCatName());
			
			if(!StringUtils.isBlank(info.getCat_id()) && Integer.parseInt(info.getCat_id())==selectedCatId){
				catInfo.put("selected", "1");
				catInfo.put("url", urlService.buildTabUrl(1, "0","0"));
				
				//面包屑
				UsualTitleInfo top = InitRuDao.getTopCat();
				changeNav(top.getCatName(),urlService.getPrefixRequestUrl()+"0-0-0-0-0-0.html",context);
				changeNav(info.getCatName(),"",context);
				
			}else
			{
				catInfo.put("selected", "0");
				catInfo.put("url", urlService.buildTabUrl(1, info.getCat_id(),"0"));
			}
			catList.add(catInfo);
		}
		context.set("cat_list", catList);
	}
	
	/**
	 * 改变面包屑
	 */
	@SuppressWarnings("unchecked")
	public void changeNav(String name,String url,MyContext context)
	{
		List<Map<String,String>> current_nav =(List<Map<String,String>>)context.getValueMap().get("current_nav");
		if(!StringUtils.isBlank(name))
		{
			Map<String,String> map = new HashMap<String,String>();
			map.put("name", name);
			map.put("url", url);
			
			if(current_nav==null)
			{
				current_nav = new LinkedList<Map<String,String>>();
				map.put("pos", "1");
			}else
			{
				int pos=0;
				for(Map<String,String> a:current_nav)
				{
					if(!StringUtils.isBlank(a.get("pos")) && Integer.parseInt(a.get("pos"))>pos )
					{
						pos =Integer.parseInt(a.get("pos"));
					}
				}
				pos +=1;
				map.put("pos", String.valueOf(pos));
			}
			current_nav.add(map);
			context.set("current_nav", current_nav);
		}
	}
}
