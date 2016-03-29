package com.solr.service.impl;

import com.solr.model.Price;
import com.solr.model.context.MyContext;
import com.solr.initialize.*;
import com.solr.core.DefaultSolrOperator;
import com.solr.core.SolrjOperator;
import com.solr.core.SolrjQuery;
import com.solr.util.ServiceCfg;
import com.solr.util.SolrServerFactory;
import com.solr.model.ChannelProduct;
import com.solr.service.ResultHandler;
import com.solr.service.SolrDataService;
import com.solr.service.SolrService;
import com.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;

public class DefaultSolrService implements SolrService {

	private static final Logger	logger	= LoggerFactory.getLogger(DefaultSolrService.class);
	protected UrlService urlService;
	/** solr查询的Server */
	protected SolrServer		server;
	// 对结果数据做处理
	protected SolrDataService solrDataService;
	// 对结果进行输出
	protected ResultHandler resultHandler;
	private static String		serverName;

    /**
	 * 创建服务对象，每一个service必须有一个处理url的urlService
	 */
	public DefaultSolrService(UrlService urlServie, SolrDataService solrDataService, ResultHandler resultHandler) {
		this.urlService = urlServie;
		serverName = urlService.getServiceName();
		// 构建solrServer对象
		String solrRequestUrl = ServiceCfg.getServiceParameterByName(serverName).get("solrRequestUrl");
		server = SolrServerFactory.getSolrServerInstance(solrRequestUrl);
		this.solrDataService = solrDataService;
		this.resultHandler = resultHandler;
	}

	/**
	 * 默认的service实现
	 */
	public boolean dealSolrService(HttpServletRequest request, HttpServletResponse response) {
		// url是否正确
		if (!urlService.isValidateUrl(request.getRequestURI())) {
			return false;
		}
		
		// 设置url里面的查询关键字
		String keywords = request.getParameter("keywords");
		if (StringUtils.isNotEmpty(keywords)) {
			keywords = keywords.replaceAll("(\".*)|(\'.*)|(\\<.*)|(\\>.*)","");
			//如果keywords是分类的拼音的话,把拼音转换为汉字
			String catId = Cats.getIdByPinyin(keywords);
			//String catName=Cats.getCatNameById(catId);
			if (StringUtils.isNotEmpty(catId) && StringUtils.isNotEmpty(Cats.getCatNameById(catId))) {
			    urlService.setSearchKeywords(Cats.getCatNameById(catId));
			}else{
			    urlService.setSearchKeywords(keywords);
			}
			
		}
		//加入城市维度的查询条件
		String cityId=request.getParameter("level");
		// 判断是否是否品牌页面点击进来的
		String brandId = request.getParameter("brandId");
		if (StringUtils.isNotEmpty(brandId)) {
			urlService.setBrandId(brandId);
		}
		// 对url进行填充处理
		String requestUrl = urlService.getAndformatUrl(request, true);
		urlService.setRequestUrl(requestUrl);
		// 构建url参数
		urlService.parseUrlParameter(requestUrl);

		// 对category进行特殊处理
		urlService.buildCategoryId();
		// 构建facet参数信息
		urlService.buildFacetParameter();
		// 构建分页基础数据
		int pageSize = Integer.parseInt(ServiceCfg.getServiceParameterByName(serverName).get("pageSize"));
		urlService.buildPageSizeToParameter(pageSize);
		MyContext context = new MyContext();
		// 处理数据查询
		try {
			// 处理url参数，获取请求参数，获取solrQuery的参数
			Map<String, String> solrQueryParameter = urlService.getSolrQueryParameter();
			//处理城市维度的查询条件
//			if(StringUtil.isNotEmpty(cityId)){
//				buildCityParameter(cityId,solrQueryParameter);
//			}
			// 处理url参数，获取solr排序的参数
			Map<String, String> solrSortParameter = urlService.buildSolrSortParameter(cityId);
			// 处理url参数，fact参数
			Map<String, Object> solrFactParameter = urlService.getSolrFacetParameter(dealSpecialSolrQuertParameter(solrQueryParameter,cityId));
			Map<String, String> rtParameter       = urlService.getRtParameter();
			// 得到Solr查询处理对象
			SolrjOperator solrOprator = getSolrOprator();
			// 处理数据查询
			int resultSize=dealSolrQuery(solrQueryParameter, solrSortParameter, solrOprator, context,cityId);
			DefalutSolrDataService defultDataService = ((DefalutSolrDataService) solrDataService);
			//第一次搜索失败，进行分词搜索
			//Set<String> analyseValues = IKDictionary.getAnalyseValues(solrQueryParameter.get("keywords"));
			//没有产品时不显示推荐
			boolean displayCommend=true;
			if(resultSize<=0 && solrQueryParameter.get("keywords")!=null && solrQueryParameter.size()==1){
				displayCommend=false;
				dealAnalyseKeywordSearch(context, solrQueryParameter, solrOprator,cityId);
			}else{
				//去除参数里面的city参数
				//solrQueryParameter.remove("cityQuery");
				// 数据查询完毕, 重置url页码, 确保页面上除翻页链接以外的其他链接上的页码标识正确
				urlService.setRequestUrl(urlService.getRequestUrl().replaceAll("page\\-\\d{1,}", "page-1"));
				// 处理fact查询
				dealCatFacetQuery(solrQueryParameter.get("cat_id"), solrFactParameter, solrOprator, context);
				dealFacetQuery(solrQueryParameter, solrFactParameter, solrOprator, context);
				// 处理排序数据
				dealSorlSort(urlService.getSolrSortParameter(), context);
				// // 处理分页参数
				dealSolrPage(solrQueryParameter, urlService.getSolrPageParameter(), solrOprator, context);
				// //价格数据,因为需要和其他数据做排序处理，所以合并价格数据到facet输一局
				//defultDataService.dealPriceData(solrQueryParameter.get("cat_id"),context);
				// 处理省市等数据
				defultDataService.dealProvinceData(context);
				// 限时达 仅限时达承诺范围
				defultDataService.dealDeliveredInTimeData(rtParameter, context);
				// 处理搜索关键字
				defultDataService.dealKeyWords(solrQueryParameter, context);
				// 处理相关搜索
				defultDataService.dealRelatedSearch(solrQueryParameter, context);
				// 处理活动数据
				defultDataService.dealActivityData(context);
				// 处理配送信息
				defultDataService.dealDeliveryData(context);

			}

			//处理热门索引分类数据
			defultDataService.dealHotIndex(solrQueryParameter,context);

            // 处理title,描述一等信息
			defultDataService.dealRelatedSearchAndSeoData(solrQueryParameter, context);
			// 处理其他参数
			defultDataService.dealOtherParameter(solrQueryParameter, context, urlService.getRequestUrl());
			// 对结果数据进行渲染处理
			resultHandler.handlerResultData(response, context);
			// 清除使用的内存数据，方便垃圾回收
			context.setValueMapNull();
			context = null;
			solrOprator = null;
			this.urlService = null;
			this.server = null;
			defultDataService = null;
			this.solrDataService = null;
			this.resultHandler = null;
			return true;

		} catch (Exception e) {
			//e.printStackTrace();;
			logger.error(requestUrl + "发生异常");
			logger.error(e.getMessage(),e);
		}

		return false;
	}

	private void buildCityParameter(String cityId, Map<String, String> solrQueryParameter) {
		//加入在全国显示的城市
		solrQueryParameter.put("cityQuery","((recommendCity:0 AND excludeCity:0) OR (recommendCity:*,"+cityId+",*)" +
				                           " OR (-excludeCity:*,"+cityId+",* and -excludeCity:0))");
		
	}

	private void dealAnalyseKeywordSearch(MyContext context, Map<String, String> solrQueryParameter, SolrjOperator solrOprator,String cityId)
			throws Exception {
		String searchKeywords=solrQueryParameter.get("keywords");
		if(StringUtils.isNotEmpty(searchKeywords)){
		  Set<String> analyseValues = IKDictionary.getAnalyseValues(searchKeywords);
		  //如果分词后是多个词,就用分词去查询
		    List<Object> analyseReslutList=new ArrayList<Object>();
			 if(analyseValues.size()>1){
				 //用分词结果进行查询
				 //List<Object> analyseReslutList=new ArrayList<Object>();
				 for(String value:analyseValues){
					 Map<String, String> analyseSolrQueryParameter=new HashMap<String, String>();
					 String catId=Cats.getCatIdByName(value);
					 Map<String, String> solrSortMap=null;
					 if(StringUtils.isNotEmpty(catId)){
						 analyseSolrQueryParameter.put("cat_id",catId);
						 solrSortMap = new LinkedHashMap<String, String>();
						 solrSortMap.put("first_page", "asc");
						 solrSortMap.put("cat_sort", "asc");
					 }else{
					     analyseSolrQueryParameter.put("keywords", value);
					 }
					 List<Object> keywordGoodsList=dealAnalyseSolrQuery(analyseSolrQueryParameter,solrSortMap,solrOprator, context,cityId);
					 long resulrCount=solrOprator.querySolrResultCount();
					 //只有在有搜索结果时才把结果集放入返回数据里面
					 if(resulrCount>0){
						 Map<String,Object> keywordResultMap=new HashMap<String,Object>();
						 keywordResultMap.put("goodsList", keywordGoodsList);
						 keywordResultMap.put("searchKeyword", value);
						 //long totalSize = solrOprator.querySolrResultCount();
						 keywordResultMap.put("totalGoodCount",resulrCount);
						 keywordResultMap.put("viewAllGoodsUrl","/category-9999/mcat0-scat0-b0-max0-min0-attr-page-1-sort-sort_order-order-asc.html" +
						 		"?keywords="+URLEncoder.encode(value,"UTF-8"));
						 analyseReslutList.add(keywordResultMap);
					 }
				 }
				 context.set("analyseReslutList", analyseReslutList);
				 context.set("originalKyeword", StringUtil.html2Text(searchKeywords));
				 context.set("analyseKeyWord", analyseValues);
				 context.set("isAnalyResult",1);
			 }else{
				 context.set("isAnalyResult",1);
				 context.set("originalKyeword",StringUtil.html2Text(searchKeywords));
				 context.set("analyseKeyWord", analyseValues);
				 context.set("analyseReslutList", analyseReslutList);
			 }
		}
	}

	/**
	 * 处理分页数据
	 */
	protected void dealSolrPage(Map<String, String> solrQueryParameter, Map<String, String> solrPageParameter, SolrjOperator solrOprator,
			MyContext context) throws Exception {
		long totalSize = solrOprator.querySolrResultCount();
		solrDataService.dealSolrPage(totalSize, solrPageParameter, context);
	}

	// 处理分页结果数据
	protected void dealSorlSort(Map<String, String> solrSortParameter, MyContext context) {
		solrDataService.dealSorlSort(solrSortParameter, context);
	}

	/**
	 * 单独请求分类数据
	 * @param cat_id
	 * @param solrFacetParameter
	 * @param solrOprator
	 * @param context
	 */
	private void dealCatFacetQuery(String cat_id, Map<String, Object> solrFacetParameter, SolrjOperator solrOprator, MyContext context)
			throws Exception {
		// 获得facet的数据结构。dataService进行数据处理
		String facet_query = String.valueOf(solrFacetParameter.get("facet.query"));
		if (StringUtils.isNotBlank(cat_id) && !Cats.getIsParentById(cat_id)) {
			facet_query = facet_query.replaceAll("cat_id:[0-9]+", "parent_id:" + Cats.getParentIdById(cat_id));
		}
		String facetField = "cdname";
		StringBuffer facetQuery = new StringBuffer();
		if (StringUtils.isNotBlank(facet_query)) {
			facetQuery.append(facet_query);
		} else {
			facetQuery.append("*:*");
		}
		List<FacetField> facetResult = solrOprator.factCatQuery(facetQuery.toString(), facetField);
		// context.set("facdet_list", facetResult);
		solrDataService.dealCatFacetData(context, facetResult);
		facetResult = null;
	}

	/**
	 * 留给子类做覆盖,扩展fact查询
	 */
	@SuppressWarnings("unchecked")
	protected void dealFacetQuery(Map<String, String> solrQueryParameter, Map<String, Object> solrFactParameter, 
			SolrjOperator solrOprator, MyContext context) throws Exception {
		
		List<FacetField> facetResult = null;
		//获得cat_id
		String cat_id = solrQueryParameter.get("cat_id");
		if (StringUtils.isEmpty(cat_id)) {
			facetResult = solrOprator.factQuery(solrFactParameter);
		} else {
			String hitAttr = "";
			ArrayList<String> oldFacetField = (ArrayList<String>) solrFactParameter.get("facet.field");
			List<String> newFacetField = new ArrayList<String>();
			List<String> justAttrFacetField = new ArrayList<String>();;
			
			for (int i = 0; i < oldFacetField.size(); i++) {
				String temp = cat_id + "_" + oldFacetField.get(i);						
				if (InitFacetDao.getIsShowNavValue(temp) == 1) {
					hitAttr = oldFacetField.get(i);
					justAttrFacetField.add(hitAttr);
					continue;
				}
				newFacetField.add(oldFacetField.get(i));
			}
			
			if (justAttrFacetField.size() == 0) {
				facetResult = solrOprator.factQuery(solrFactParameter);
				newFacetField = null;
			} else {
			    //处理一些动态属性，显示隐藏商品的动态属性，例如有些床设置为不显示，但是床的尺寸需要在列表页显示
				//构造一个facet.field中除去attr_db_name(例如：ar_113 规格)字段的map 
				Map<String, Object> withoutAttrParameter = new HashMap<String, Object>();
				withoutAttrParameter.putAll(solrFactParameter);
				withoutAttrParameter.remove("facet.field");
				withoutAttrParameter.put("facet.field", newFacetField);
				//构造一个facet.field仅有attr_db_name字段且facet.query中除去is_show_web:true的map
				Map<String, Object> justAttrParameter = new HashMap<String, Object>();
				justAttrParameter.putAll(solrFactParameter);
				justAttrParameter.remove("facet.query");
				String facetQuery = (String) solrFactParameter.get("facet.query");
				justAttrParameter.put("facet.query", facetQuery.replace("AND is_show_web:true", ""));
				justAttrParameter.remove("facet.field");
				justAttrParameter.put("facet.field", justAttrFacetField);
				//将两者的factQuery结果合并
				facetResult = solrOprator.factQuery(withoutAttrParameter);
				List<FacetField> justArrFacetResult = solrOprator.factQuery(justAttrParameter);
				facetResult.addAll(justArrFacetResult);
			}
		}
		//对价格区间单独做处理
		//对价格区间单独做处理
        Map<String, Object> priceFacetParameter = new HashMap<String, Object>();
        priceFacetParameter.putAll(solrFactParameter);
        priceFacetParameter.remove("facet.field");
        List<Price> priceList =getPriceList(cat_id);;
        //priceFacetParameter.put("facet.priceQuery",priceList);
        Map<String,Integer> priceQueryResult=solrOprator.factQuery(priceFacetParameter,priceList);
        context.set("priceQueryResult", priceQueryResult);

		//对结果进行处理
		solrDataService.dealFacetData(solrFactParameter, context, facetResult);
		facetResult = null;
		
	
	}

    private List<Price>  getPriceList(String catId) {
        List<Price> priceList=null;
        if (StringUtils.isNotEmpty(catId)) {
            priceList = InitDao.getPriceListByCatId(catId);
        }
        // 如果价格为空,就用默认的价格属性。后台设置的价格为空，包括没有catId和有catId时后台没有设置
        if (priceList == null) {
            priceList = ServiceCfg.getPriceListByName("default");
        }
        return priceList;
    }

	/**
	 * 创建solr的查询对象
	 */
	public SolrjOperator getSolrOprator() {
		SolrjQuery solrjQuery = new SolrjQuery(server);
		SolrjOperator solrOprator = new DefaultSolrOperator(solrjQuery);
		return solrOprator;
	}

	public boolean dealIsShowWebParameter(Map<String, String> solrQueryParameter) {
		
		boolean flag = false;
		if (solrQueryParameter.get("cat_id") == null) {
			return flag;
		}
		for (String queryParameter : solrQueryParameter.keySet()) {
			
			String queryAttr = solrQueryParameter.get("cat_id") + "_" + queryParameter 
			+ "_" + solrQueryParameter.get(queryParameter);
			if (InitFacetDao.getIsShowWebValue(queryAttr) == 1) {				
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	private Map<String, String> dealSpecialSolrQuertParameter(Map<String, String> solrQueryParameter,String cityId) {
		//对keyword进行处理
		Map<String, String> queryParameter = new HashMap<String, String>();
		queryParameter.putAll(solrQueryParameter);
		if(StringUtils.isNotEmpty(cityId)){
			queryParameter.put("cityQuery",String.format("(recommendCity:0 OR recommendCity:*,%s,* OR -excludeCity:*,%s,*)",cityId,cityId));
		}
		String keywords = queryParameter.get("keywords");
		boolean isGoodsSN = false;
		if (StringUtils.isNotEmpty(keywords)) {
			//如果是根据编号查询，就不会走分词查询，直接安装原来的查询方式进行查询
			String regex = "[0-9a-zA-Z#-]+";
			if (keywords.matches(regex)) {
				// 过滤英文品牌干扰
				if (!InitOtherDao.getAllBrand().contains(keywords)) {
					isGoodsSN = true;
				}
			}else {
				queryParameter.put("flag", "main");
			}
		}
		if (!isGoodsSN && !dealIsShowWebParameter(solrQueryParameter)) {
			queryParameter.put("is_show_web", "true");
		}
		String cat_id = queryParameter.get("cat_id");
		if (StringUtils.isNotEmpty(cat_id) && Cats.getIsParentById(cat_id)) {
			queryParameter.put("parent_id", cat_id);
			queryParameter.remove("cat_id");
		}
		return queryParameter;
	}

	protected int dealSolrQuery(Map<String, String> solrQueryParameter, Map<String, String> solrSortParameter, SolrjOperator solrOprator,
			MyContext context,String cityId) throws Exception {

		Long startIndex = Long.parseLong((String) urlService.getSolrPageParameter().get("startPage"));
		Long pageSize = Long.parseLong((String) urlService.getSolrPageParameter().get("pageSize"));
		//对solr query里面的特殊参数做处理
		Map<String, String> queryParameter = dealSpecialSolrQuertParameter(solrQueryParameter,cityId);
		//将show_type放入context中,方便传向前段页面
		String showType = queryParameter.get("show_type");
		if (StringUtils.isNotBlank(showType)) {
			context.set("_show_type", showType);
		}
		List<Object> productList = solrOprator.querySolrBeanResult(queryParameter, solrSortParameter, (startIndex - 1) * pageSize,
				pageSize, ChannelProduct.class);
		// 如果是查询的现货，标示现货字段
		Map<String, String> rtParameter = urlService.getRtParameter();
		String districtId = rtParameter.get("city");
		List<String> deliveredInTimeAreaList    = InitDao.getDeliveredInTimeArea();
		boolean isDistrictInDeliveredInTimeArea = false;
		if (StringUtils.isNotBlank(districtId)) {
			isDistrictInDeliveredInTimeArea = deliveredInTimeAreaList.contains(districtId);
		}
		for (int i = 0; i < productList.size(); i++) {
			ChannelProduct product = (ChannelProduct) productList.get(i);
			if (product.getShop_id() > 1) {
				product.setIs_real_time(1);
			} else {
				if (StringUtils.isNotEmpty(districtId)) {
					String depotId = rtParameter.get("depotId");
					setIsRealTime(product, depotId);
				} else {
					if (StringUtils.isEmpty(product.getNew_goods_name())) {
						product.setNew_goods_name(product.getGoods_name());
					}
					//product.getUser_name();// 执行用户名过滤
					// 处理特殊的广告标签信息
					if (StringUtils.isNotEmpty(product.getGoods_sn())) {
					   product.setAdvertisementTag(InitOtherDao.getAdvertisementTag(product.getGoods_sn()));
					}
				}
				if ("预售".equals(product.getDesc())) {
					product.setIs_real_time(4);
				}
			}
			// 设置限时达信息
			if (isDistrictInDeliveredInTimeArea && 1 == product.getShop_id()) {
				int isRealTime = product.getIs_real_time();
				if (4 == isRealTime) {
					isRealTime = 0;
				}
				product.setIsDeliveredInTime(isRealTime);
			}
			// 设置seo的位置
			product.setGoods_uri(product.getGoods_uri()+"?page="+String.valueOf(startIndex)+"&index="+String.valueOf(i+1));
			product.setIcon_img(InitDao.getIconImgByGoodsId(product.getId()));
		}
		// 处理现货是否选中
		if ("1".equals(rtParameter.get("rt"))) {
			context.set("is_spot_goods", "yes");
			if (productList.size() == 0) {
				context.set("have_spot", "false");
			} else {
				context.set("have_spot", "true");
			}
		}
		rtParameter = null;
		context.set("goods_list", productList);
		int resultSize = productList.size();
		productList = null;
		return resultSize;
	}

	private void setIsRealTime(ChannelProduct product, String depotId) {

			//product.getUser_name();//执行用户名过滤
			if (StringUtils.isEmpty(product.getNew_goods_name())) {
				product.setNew_goods_name(product.getGoods_name());
			}
			String[] allocate_city = product.getAllocate_city().split(",");
			String[] spot_city = product.getSpot_city().split(",");
			// 现货
			if (StringUtils.isNotEmpty(depotId)) {
				if (isContainValue(spot_city, depotId)) {
					product.setIs_real_time(1);
				} else if (isContainValue(allocate_city, depotId)) {
					// 调配
					product.setIs_real_time(2);
				} else {
					// 预约
					product.setIs_real_time(3);
				}
			} else {
				if (allocate_city != null && spot_city != null) {
					if (allocate_city.length > 0 && !allocate_city[0].equals("0")) {
						product.setIs_real_time(1);
					} else if (spot_city.length > 0 && !spot_city[0].equals("0")) {
						product.setIs_real_time(1);
					} else {
						product.setIs_real_time(3);
					}
				}
			}
			//处理特殊的广告标签信息
			if(StringUtils.isNotEmpty(product.getGoods_sn())) {
				product.setAdvertisementTag(InitOtherDao.getAdvertisementTag(product.getGoods_sn()));
			}
	}
	
	protected List<Object> dealAnalyseSolrQuery(Map<String, String> solrQueryParameter, Map<String, String> solrSortParameter, SolrjOperator solrOprator,
			MyContext context,String cityId) throws Exception {

		//Long startIndex = Long.parseLong((String) urlService.getSolrPageParameter().get("startPage"));
		//Long pageSize = Long.parseLong((String) urlService.getSolrPageParameter().get("pageSize"));
		//对solr query里面的特殊参数做处理
		Map<String, String> queryParameter = dealSpecialSolrQuertParameter(solrQueryParameter,cityId);
		List<Object> productList = solrOprator.querySolrBeanResult(queryParameter, solrSortParameter,0L,3L, ChannelProduct.class);
		//对一些特殊数据做处理
		for (int i = 0; i < productList.size(); i++) {
			ChannelProduct product = (ChannelProduct) productList.get(i);
			if(StringUtils.isEmpty(product.getNew_goods_name())){
				product.setNew_goods_name(product.getGoods_name());
			}
			//处理特殊的广告标签信息
			if(StringUtils.isNotEmpty(product.getGoods_sn())){
			   product.setAdvertisementTag(InitOtherDao.getAdvertisementTag(product.getGoods_sn()));
			}
		}
		return productList;
	}

	public boolean isContainValue(String[] sourceArray, String value) {
		for (String str : sourceArray) {
			if (str.equals(value)) {
				return true;
			}
		}
		return false;
	}
}
