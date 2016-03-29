package com.solr.service.qwg;

import com.solr.model.context.MyContext;
import com.solr.initialize.InitQwgSolrDao;
import com.solr.core.DefaultSolrOperator;
import com.solr.core.SolrjOperator;
import com.solr.core.SolrjQuery;
import com.solr.util.*;
import com.solr.model.FacetVo;
import com.solr.service.ResultHandler;
import com.solr.service.SolrService;
import com.util.NumberUtil;
import com.util.Pinyin4jUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QwgSolrService implements SolrService
{
	
	private	final static Logger logger = LoggerFactory.getLogger(QwgSolrService.class);
	
	public static String serverName="qwg";
	
	private SolrServer server;
	
	private ResultHandler resultHandler;
	
	public static String DEFTITLE = "qwg频道";
	//{风格}{户型}{预算}
	public static String seoTitle = "${0}${1}${2}${3}家具【套装】推荐-xxx设计师服务频道";
	public static String seoKeywords = "${0}${1}${2}${3} ${0}${1}${2}${3}家具 ${0}${1}${2}${3}家具套装";
	public static String seoDescription = "${0}${1}${2}${3}家具套装推荐，是由xxx家居网设计师为消费者提供方便快捷的${0}${1}${2}${3}空间搭配体验及选购方案.选购${0}${1}${2}${3}家具套装就上xxx设计师服务频道，享受打折优惠活动:选购越多，省得更多！";

	public static String[] originalSeo = new String[]{seoTitle,seoKeywords,seoDescription};
	
	public static String channelSeoTitle = "家具,建材,家纺饰品搭配选购省心、省力、省钱-xxx设计师服务频道";
	public static String channelSeoKeywords = "设计师服务 家具搭配 建材选购 家纺家饰";
	public static String channelSeoDescription = "xxx设计师服务频道，为消费者提供方便快捷的空间搭配体验及方案。全网家具,建材以及家纺饰品，成为您搭配房屋空间的帮手，同时提供打折优惠活动:选购越多，省得更多！";

	//样板间列表页seo
	public static String ybjChengDuTitle ="欢迎参观xxx成都样板间，报名样板间计划，全屋家具免费送_xxx家居网";
	public static String ybjChengDuKeywords="成都样板间 成都装修样板间 xxx成都家装样板间 业主样板间 参观样板间 样板间参观";
	public static String ybjChengDuDescription="xxx家居网成都全城征集装修样板间，报名样板间计划，签约满2年免费送全屋家具，专业设计师提供免费家具搭配服务，装修公司、风格、家具由你定。同时组织成都业主参观xxx样板间，专车VIP接待，各种优惠折扣任你选！- xxx家居网";
	
	public static String[] channelSeo = new String[]{channelSeoTitle,channelSeoKeywords,channelSeoDescription};
	
	/**分区楼盘列表**/
	private static List<FacetVo> districtFloorList = new LinkedList<FacetVo>();
	
	/**去除重复的"////" **/
	private static Pattern urlPattern =Pattern.compile("^/{2,}");
	
	/**请求的url*/
	private String queryUrl;
	
	/**请求的参数数组*/
	private List<String> queryParameters= new LinkedList<String>();
	
	/**请求前缀*/
	private static String queryPre="/quanwugou/q-";
	
	private static String queryPreNoParam="/quanwugou";
	
	private static String specQueryPre="/ybj_chengdu/q-";
	
	private static String specQueryNoParam="/ybj_chengdu";
	
	/**默认排序前缀**/
	public static final String DEFALUT_SORT_PRE="sort_";
	
	/**单选筛选，--360全景图**/
	public static final String radioChoose []={"只看360°全景图"};
	
	/**以前的url正则,为了做兼容用到**/
	private static final Pattern oldUrlParamerPattern = Pattern.compile("q-(\\d+)-(\\d+)-(\\d+)-(\\d+)-("+DEFALUT_SORT_PRE+"[^-]*)-(\\d+)");
	/**
	 * 解析参数的正则表达式
	 * 可以解析出每一位数据,但个数是固定的
	 */
	private static final Pattern urlParamerPattern = Pattern.compile("q-(\\d+)-(\\d+)-(\\d+)-(\\d+)-(\\d+)-(\\d+)-(\\d+)-(\\d+)-([^-]*)-(\\d+)");
	
	/**兼容老版本,url转换需要的规则**/
	private static final Pattern transOldUrlPattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)-(\\d+)-"+DEFALUT_SORT_PRE);
	/**兼容老版本,url匹配的部分替换成**/
	private static final String oldUrlReplaceStr= DEFALUT_SORT_PRE;
	
	/**选中的维度名称*/
	private Map<Integer,String> selectedNames = new HashMap<Integer,String>();
	
	/**选中的维度值*/
	private Map<Integer,String> selectedValues = new HashMap<Integer,String>();
	
	/**第一页*/
	private static final long firstPage =1l;
	
	/**分页默认数量**/
	private long pageSize = 10l;
	
	/**分页开始页**/
	private long startIndex = 1L;
	
	/**排序字段**/
	private Map<String, String> sortMap =new HashMap<String,String>();
	
	/**搜索维度名称列表**/
	public static String searchNames [] ={"style","price","type","space","city","floor","district","radio"};
	
	public static String searchShowNames [] ={"风格","预算","户型","空间","城市","楼盘","区域","单选项"};
	
	/**排序字段可能值**/
	public static String sortNames [] ={"recommend","most"};
	
	public static String sortShowNames []={"设计师推荐","网友购买最多"};
	
	/**默认排序升序**/
	private static String DEFAULT_SORT="0";//desc
	
	/**表示只按一种排序排,即DEFAULT_SORT*/
	private static boolean SORT_ONLY=true;
	
	/**默认排序字段**/
	private static String DEFAULT_SORT_FIELD=sortNames[0];
	
	/**后台真实的排序字段**/
	public static String sortRealNames [] ={"add_time","click_num"};
	
	public static String DEFALUT_SORT_STR=DEFALUT_SORT_PRE+DEFAULT_SORT_FIELD+DEFAULT_SORT;
	
	/**默认查询占位字段*/
	public static String DEFAULT_QUERY_NUM="0";
	//quanwugou/q-0-0-0-0-0-0-0-0-sort_-1
	
	/**查询字段**/
	private Map<String, String> queryMap =new HashMap<String,String>();
	
	/**选中的空间id**/
	private String selectedSpaceId="";
	
	/**pageSize是否传参**/
	private boolean paramPageSize=false;
	
	/**是否特殊城市，特殊城市 列表页数据特殊处理**/
	public boolean isSpecCity=false;
	
	private Map<String, List<String>> factParameterMap = new HashMap<String, List<String>>();
	
    private String queryPreStr=queryPre;
	
	private String queryPreNoParamStr=queryPreNoParam;
	
	private boolean isOldUrl=false;
	
	//当前参数值
	private List<String> curValues = new LinkedList<String>();
	
	private static List<Map<String, String>> areaList = new LinkedList<Map<String, String>>();
	
	/**请求参数个数*/
	private static int paramCount =10;
	
	public QwgSolrService(ResultHandler resultHandler){
		this.resultHandler = resultHandler;
		this.pageSize= this.getPracticalPageSize(pageSize);
	}
	
	public boolean dealSolrService(HttpServletRequest request,HttpServletResponse response) {
		
		parseRequetUrl(request);
		
		//处理请求前缀
		dealQueryPre();
		
		getQueryParameterFromUrl();
		
		//从请求参数总获取参数
		getQueryParam(request);
		
		//构建face参数
		//buildFacetParameter();
		//从请求中获取 列表页中pageSize
		String ps = request.getParameter("pageSize");
		if (NumberUtil.isNumeric(ps)) {
			this.pageSize = Long.parseLong(ps);
			paramPageSize= true;
		}
		// 数据保存context
		MyContext context = new MyContext();
		
		try{
            
			buildQueryAndSortParameter();
            
			DefaultSolrOperator solrOprator = getSolrOprator();
			
			// 处理查询
			dealSolrQuery(queryMap, sortMap, solrOprator, context);
			
			// 处理分页参数
			dealSolrPageAndSort(queryMap, sortMap, solrOprator, context);
			
			if(isSpecCity)
			{
				// 构建facet参数信息  ,之前的 作废
//				Map<String,String> factQueryMap = new HashMap<String,String>();
//				String [] queryParamNames =InitQuanWuGouSolrDao.getQueryParamNames();
//				if(queryParamNames!=null && queryParamNames.length>4)
//				{
//				   factQueryMap.put(queryParamNames[4],String.valueOf(InitQuanWuGouSolrDao.getSpecCity()));
//				}
//				dealParamFacetPivotQuery(factQueryMap, context);
				
				String curDistrict = getCurrentSelectValue(6,DEFAULT_QUERY_NUM);
				if(StringUtils.isBlank(curDistrict) || DEFAULT_QUERY_NUM.equals(curDistrict) )
				{
					dealSearchFloorTabsData(context);
				}else
				{
					Map<String,Object> factQueryMap = getSolrFacetParameter();
					dealParamFacetQuery(factQueryMap,solrOprator, context);
				}
				
			}
			
			// 处理title，keyword等seo信息
			dealSeoSolrAndLinkData(queryMap,context);
			
			// 处理选择维度数据
			dealSearchTabsData(solrOprator,context);
			
			// 处理排序数据
			dealSortData(context);
	
			//查询状态信息
			dealCurrentStatus(context);
			
			resultHandler.handlerResultData(response, context);
			
			clear();
			
			return true;
			
		}catch(Exception e)
		{
			logger.error("QuanWuGouSolrService dealSolrService exception,queryUrl="+queryUrl ,e);
		}
		finally{
			
		}
		return false;
	}

	/**
	 * 请求url预处理
	 */
	public void parseRequetUrl(HttpServletRequest req)
	{
		//quanwugou/q-0-0-0-0-0-0-0-sort_recommend0-1/
		queryUrl=req.getRequestURI();
		try {
			queryUrl =URLDecoder.decode(queryUrl,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		queryUrl=queryUrl.replaceAll("(\".*)|(\'.*)|(\\<.*)|(\\>.*)","");
		Matcher m =urlPattern.matcher(queryUrl);
		if(m.find())
		{
			queryUrl=queryUrl.replace(m.group(), "/");
		}
		if(queryUrl.contains(specQueryNoParam))
		{
			isSpecCity=true;
		}
		if(queryUrl.contains(queryPreNoParam))
		{
			isOldUrl=true;
		}
    }
	
    public void dealQueryPre()
    {
    	if(isSpecCity)
    	{
    		queryPreStr=specQueryPre;
    		queryPreNoParamStr=specQueryNoParam;
    	}
    }
	
	/**
	 * 从请求中获取固定参数
	 */
	private void getQueryParameterFromUrl() {
		
		queryParameters.clear();
		
		if(queryUrl.indexOf(queryPreStr)<0)
		{
			for(int i=0;i<paramCount-2;i++)
			{
				queryParameters.add(DEFAULT_QUERY_NUM);
			}
			queryParameters.add(DEFALUT_SORT_STR);
			queryParameters.add(String.valueOf(firstPage));
			return;
		}	

		//兼容
		Matcher oldMch = oldUrlParamerPattern.matcher(queryUrl);
		if(oldMch.find())
		{
			isOldUrl=true;
			for(int i=1;i<=paramCount-4;i++)
			{
				queryParameters.add(oldMch.group(i));
			}
		}
		if(isOldUrl && queryParameters.size()<paramCount )
		{
			queryParameters.add(4,DEFAULT_QUERY_NUM);
			queryParameters.add(5,DEFAULT_QUERY_NUM);
			queryParameters.add(6,DEFAULT_QUERY_NUM);
			queryParameters.add(7,DEFAULT_QUERY_NUM);
			return;
		}
		
		Matcher m = urlParamerPattern.matcher(queryUrl);
		
	    if(m.find())
		{
			for(int i=1;i<=paramCount;i++)
    		{
    			queryParameters.add(m.group(i));
    		}
		}
		
	}
	
	private void getQueryParam(HttpServletRequest request)
	{
		String floorName = request.getParameter("floorName");
		
		if(isSpecCity)
		{
			if(queryParameters.size()-1>=4)
			{
			    queryParameters.remove(4);
				queryParameters.add(4,String.valueOf(InitQwgSolrDao.getSpecCity()));
			}
			if(StringUtils.isBlank(floorName))
			{
				floorName=DEFAULT_QUERY_NUM;
			}else
			{
				try{
				    floorName = URLDecoder.decode(floorName,"utf-8");
				}catch(Exception e)
				{
					logger.error("QuanWuGouSolrService decode "+floorName+" error,",e);
				}
				if("不限".equalsIgnoreCase(floorName))
				{
					floorName=DEFAULT_QUERY_NUM;
				}
			}
			if(queryParameters.size()-1>=5)
			{
			    queryParameters.remove(5);
			    queryParameters.add(5,floorName);
			}
		}
	}
	
	/**
	 * 另一种获取参数的规则
	 */
	private void getQueryParameterFromUrl2()
	{
        queryParameters.clear();
		
        int index =queryUrl.indexOf(queryPreStr);
		if(index<0)
		{
			return;
		}	
		String tmp = queryUrl.substring(index+queryPreStr.length());
		if(tmp.indexOf(".html")>=0)
		{
			tmp =tmp.replace(".html", "");
		}
		String args [] = tmp.split("-");
		queryParameters = Arrays.asList(args);
	}
	
	/**
	 * 解析查询字段即排序字段
	 */
	public void buildQueryAndSortParameter() {
		
		int queryCount =  queryParameters.size()-2;
		
		String paramNames [] = InitQwgSolrDao.getQueryParamNames();
		
		//单选项只占一位参数
		int radioCount=0;
		String radioParamNames [] = InitQwgSolrDao.getRadioParamNames();
		if(radioParamNames.length>0)
		{
			radioCount=1;
		}
		
		if(paramNames==null)
		{
			queryCount=0;
		}
		if((paramNames.length+radioCount)<queryCount)
		{
			queryCount=paramNames.length+radioCount;
		}
		int sortIndex=queryCount;
		int pageIndex =queryCount+1;
		
		for (int i=0;i<queryParameters.size();i++) {
			String parameter = queryParameters.get(i);
			if(i<queryCount)
			{
				//查询参数
				if (StringUtils.isEmpty(parameter) || DEFAULT_QUERY_NUM.equalsIgnoreCase(parameter) ) {
					continue;
				}else
				{
					
					Map<String,String> map =null; 
                    if(i==0)
                    {
                    	//风格
                    	map= InitQwgSolrDao.getStyleRecordByOrder(parameter);
                    }
                    if(i==1)
                    {
                    	//预算
                    	map= InitQwgSolrDao.getPriceIntlRecordByOrder(parameter);
                    }
					if(i==2)
					{
						//户型
						map= InitQwgSolrDao.getTypeRecordByOrder(parameter);
					}
					if(i==3)
					{
						//空间
						map= InitQwgSolrDao.getSpaceRecordByOrder(parameter);
						if(map!=null)
						{
						    selectedSpaceId=map.get("id");
						}
					}
					if(i==6)
					{
						//区域
						map= InitQwgSolrDao.getDistrictByOrder(parameter);
					}
					if(map!=null )
                	{
                	    selectedNames.put(i,map.get("name"));
                	    selectedValues.put(i,parameter);
                	    queryMap.put(paramNames[i],map.get("id"));
                	}else
                	{
                		if(i!=4 && i!=5 && i!=7)
                		{
                		    logger.error("QuanWuGouSolrService getXXByOrder return null ,i="+i+",parameter ="+parameter);
                		}
                	}
				    if(i==4 || i==5)
					{
						selectedNames.put(i,parameter);
                	    selectedValues.put(i,parameter);
                	    queryMap.put(paramNames[i],parameter);
                	    if(i==5)
                	    {
                	    	//楼盘全名查询
                	    	queryMap.put(paramNames[i],"\""+parameter+"\"");
                	    }
					}
				    //单选项的处理
				    if(i==7)
				    {
				    	int radioSelectedOrder = Integer.parseInt(parameter);
				    	int index = radioSelectedOrder -1;
				    	String queryRadio = InitQwgSolrDao.getRadioByIndex(index);
				    	if(!StringUtils.isBlank(queryRadio))
				    	{
				    		String radioName = "";
				    	    if(radioChoose.length>index)
				    	    {
				    	    	radioName= radioChoose[index];
				    	    }
				    		selectedNames.put(i,radioName);
	                	    selectedValues.put(i,parameter);
	                	    queryMap.put(queryRadio,"1");
				    	}
				    
				    }
					//String parameterName=parameter.replaceAll("[0-9]", "");
					//String parameterValue=parameter.replaceAll("[a-zA-Z]", "");
				}
			}
			//排序
			if(i==sortIndex)
			{
				if (StringUtils.isEmpty(parameter)) {
					continue;
				}else
				{
					dealSortCondition(parameter);
				}
			}
			//分页
			if(i==pageIndex)
			{
				if (!StringUtils.isEmpty(parameter)) {
					startIndex = Long.parseLong(parameter);
					if(startIndex<=0)
					{
						startIndex=1;
					}
				}
			}

//				if (parameter.matches("cat\\d{1,}")) {
//					//加入soncat特殊的查询查询
//					catId = parameter.replace("cat", "");
//					queryMap.put("queryValue","(cat:"+catId+" or soncat:"+catId+")");
//				} else {
//					queryMap.put(parameterName,parameterValue);
//				}
		}
		
		curValues.clear();
		
		for(int i=0;i<queryCount;i++)
		{
			String curV = getCurrentSelectValue(i,DEFAULT_QUERY_NUM);
		     //城市，楼盘
			if(i==4 || i==5)
			{
				curV= DEFAULT_QUERY_NUM;
			}	
			curValues.add(curV);
		}
	}
	
	
	/**
	 * 处理排序条件
	 * @param param
	 */
	public void dealSortCondition(String param)
	{
       
		String parameterName=param.replaceAll("[0-9]", "");
		parameterName = parameterName.replace(DEFALUT_SORT_PRE,"");
		if(StringUtils.isBlank(parameterName))
		{
			return;
		}
		
		String parameterValue=param.replaceAll("[a-zA-Z_]", "");
		int index =-1;
		for(int i=0;i<sortNames.length;i++)
		{
			if(sortNames[i].equalsIgnoreCase(parameterName))
			{
				index=i;
			}
		}
		if(index<0)
		{
			return;
		}
	   	
	   String s= sortRealNames[index];
	   sortMap.put("sort",s);
	   sortMap.put("sort_name",parameterName);
	   sortMap.put("select_sort",String.valueOf(index+1));
	   
	   //order1:升序，order2:降序
		if(StringUtils.isBlank(parameterValue) || "1".equals(parameterValue)){
		   sortMap.put("order","asc");
		}else{
			sortMap.put("order","desc");
		}
	}
	
	/**
	 * 获取solr查询器
	 * @return
	 */
	public DefaultSolrOperator getSolrOprator() {
		server = SolrServerFactory.getSolrServerInstance( ServiceCfg.getServiceParameterByName("qwg").get("solrRequestUrl"));
		SolrjQuery solrjQuery = new SolrjQuery(server);
		DefaultSolrOperator solrOprator = new DefaultSolrOperator(solrjQuery);
		return solrOprator;
	}
    
	/**获取配置中分页数，如取不到，用默认值**/
	public long getPracticalPageSize(long def)
	{
		long ps =def;
		try{
			String psStr= ServiceCfg.getServiceParameterByName(serverName).get("pageSize");
			if(!StringUtils.isBlank(psStr))
			{
		      ps= Long.parseLong(psStr);
			}
		}catch(Exception e)
		{
			ps = def;
			logger.error("QuanWuGouSolrService invoke XmlReadeTool pageSize serverName= "+serverName +" error,"+e.getMessage());
		}
		return ps;
	}
	
    /**
     * 实际的查询实现
     */
	private void dealSolrQuery(Map<String, String> solrQueryMap, Map<String, String> sorMap, DefaultSolrOperator solrOprator, MyContext context)
			throws Exception {
        Map<String,String> querySortMap=new HashMap<String,String>();
        //logger.error("query sort "+sorMap.get("sort")+","+sorMap.get("order"));
        querySortMap.put(sorMap.get("sort"),sorMap.get("order"));
		List<Object> resultList = solrOprator.querySolrResult(solrQueryMap, querySortMap, (startIndex - 1)  * pageSize, pageSize);
		//选了空间特殊处理
	
		List<Object> wrapedList =new ArrayList<Object>();
		for(Object obj:resultList)
		{
			JSONObject json = JSONObject.fromObject(obj);
			String space_imgs =(json.has("space_imgs")?json.getString("space_imgs"):"");
			String space_types = (json.has("space_types")?json.getString("space_types"):"");
			String img = (json.has("img")?json.getString("img"):"");
			String img2 = (json.has("img2")?json.getString("img2"):"");
			String space_name = (json.has("space_name")?json.getString("space_name"):"");
			//实拍图(倒叙存放)
			String figure_photos = (json.has("figure_photos")?json.getString("figure_photos"):"");
			
			json.put("space_imgs", space_imgs);
			json.put("space_types", space_types);
			json.put("img", img);
			json.put("img2", img2);
			json.put("space_name",space_name);
			json.put("figure_photos",figure_photos);
			
			if(StringUtils.isBlank(space_imgs)||StringUtils.isBlank(space_types) )
			{
				json.put("show_img", "");
				//wrapedList.add(json);
				//continue;
			}
			
			String arrs [] = space_types.split(" ");
			int idx=-1;
			for(int i=0;i<arrs.length;i++)
			{
				if(!StringUtils.isBlank(selectedSpaceId) && selectedSpaceId.equalsIgnoreCase(arrs[i]))
				{
					idx=i;
				}
			}
			String images [] = space_imgs.split(",");
			
			//处理当前选中的空间 
			if(idx<0 || idx>(images.length-1))
			{
				json.put("show_img", "");
				
			}else
			{
				json.put("show_img", images[idx]);
			}
			
			//处理第一空间，第二空间效果图
			if(StringUtils.isBlank(img) ||StringUtils.isBlank(img2) )
			{
				//此记录的空间名数组
				String spNames [] = space_name.split(" ");
				
				//处理空间排序
				List<String> orderSpaces= new LinkedList<String>();
				List<Map<String, String>> spaceDatas = InitQwgSolrDao.getSuitSpaceList();
				if(spaceDatas!=null && spaceDatas.size()>0)
				{
					
					for(Map<String,String> m: spaceDatas)
					{
						if("不限".equalsIgnoreCase(m.get("name")))
						{
							continue;
						}
						orderSpaces.add(m.get("name"));
					}
					
				}else
				{
					//前两个空间
					orderSpaces.add("客厅");
					orderSpaces.add("卧室");
				}
				for(String sp:spNames)
				{
					if(!orderSpaces.contains(sp))
					{
						orderSpaces.add(sp);
					}
				}
				//空间排序处理完
				
				//组合空间图
				Map<String,List<String>> spaceImgs = new HashMap<String,List<String>>();
				int imgsCount=0;
				String firstImg="";
				for(int i=0;i<spNames.length && i<images.length;i++)
				{
					String key =spNames[i];
					String mg=images[i];
					if(StringUtils.isBlank(key) || StringUtils.isBlank(mg))
					{
						continue;
					}
					List<String> vls = spaceImgs.get(key);
					if(vls==null)
					{
						vls=new ArrayList<String>();
						vls.add(mg);
						spaceImgs.put(key, vls);
					}
					else
					{
						vls.add(mg);
					}
					imgsCount++;
					if(imgsCount==1)
					{
						firstImg=mg;
					}
				}
				/////组合完//////
				
				//取合适的值填充
				if(StringUtils.isBlank(img))
				{
					String signSpace = orderSpaces.get(0);
					String imgVu=getPadImg(orderSpaces,spaceImgs,figure_photos,imgsCount,firstImg,signSpace,img2);
					json.put("img", ((imgVu==null)?"":imgVu));
					img = (json.has("img")?json.getString("img"):"");
				}
				
				if(StringUtils.isBlank(img2))
				{
					String signSpace = orderSpaces.get(0);
					if(orderSpaces.size()>=2)
					{
						signSpace=orderSpaces.get(1);
					}
					String img2Vu=getPadImg(orderSpaces,spaceImgs,figure_photos,imgsCount,firstImg,signSpace,img);
					json.put("img2", ((img2Vu==null)?"":img2Vu));
				}
			}
			
			wrapedList.add(json);
		}
		context.set("data_list", wrapedList);
		
	}

	/**
	 * 获取填补的图片
	 */
	public String getPadImg(List<String> orderSpaces,Map<String,List<String>> spaceImgs,
			               String figure_photos,int imgsCount,String firstImg,String signSpace,String img2)
	{
		String imgVu="";
		if(imgsCount==1)
		{
			imgVu=firstImg;
		}else
		{
			imgVu=getSuitableImg(orderSpaces,spaceImgs,signSpace,img2);
		}
		if(StringUtils.isBlank(imgVu) || imgVu.equalsIgnoreCase(img2))
		{
			String figurePic = getSuitableImg(figure_photos,img2);
			if(!StringUtils.isBlank(figurePic))
			{
				imgVu=figurePic;
			}
		}
		return imgVu;
	}
	/**
	 * 获取合适的效果图
	 * @return
	 */
	public String getSuitableImg(List<String> orderSpaces,Map<String,List<String>> spaceImgs,String signSpace,String img2)
	{
		String imgVu="";
		List<String> tarImgs= spaceImgs.get(signSpace);
		if(tarImgs!=null && tarImgs.size()>0 )
		{
			imgVu= tarImgs.get(0);
		}
		if(StringUtils.isBlank(imgVu))
		{
			for(String sp:orderSpaces)
			{
				List<String> tImgs= spaceImgs.get(sp);
				if(tImgs!=null && tImgs.size()>0 )
				{
					boolean fand=false;
					for(String tImg:tImgs)
					{
						if(!StringUtils.isBlank(img2) && tImg.equalsIgnoreCase(img2))
						{
							continue;
						}
						fand=true;
						imgVu= tImg;
						break;
					}
					if(fand)
					{
						break;
					}
					
				}
			}
		}
		return imgVu;
	}
	
	/**
	 * 获取合适的实拍图
	 * @return
	 */
	public String getSuitableImg(String figurePhotos,String img2)
	{
		String imgVu="";
		
		if(StringUtils.isBlank(figurePhotos))
		{
			return imgVu;
		}
		String imgs [] = figurePhotos.split(",");
		int len = imgs.length;
		for(int i=0;i<=len-1;i++)
		{
			String im =imgs[i];
			if(StringUtils.isBlank(im) || StringUtils.isBlank(im.trim()))
			{
				continue;
			}
			if(StringUtils.isBlank(img2) || !im.trim().equalsIgnoreCase(img2))
			{
				imgVu= im.trim();
				break;
			}
		}
		return imgVu;
	}
	
	/**
	 * 处理分页跟排序
	 */
	@SuppressWarnings("unchecked")
	private void dealSolrPageAndSort(Map<String, String> solrQueryMap, Map<String, String> sorMap, DefaultSolrOperator solrOprator, MyContext context)
			throws Exception {
		long totalSize = solrOprator.querySolrResultCount();
		//tempsMap 分页数据
		Map<String, Object> tempsMap = doPageDatas(totalSize, (startIndex-1) * pageSize, String.valueOf(pageSize));
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		Set<String> sets = tempsMap.keySet();
		for (String str : sets) {
			pagerMap.put(str, tempsMap.get(str));
		}
		//获取页面信息
		context.set("max_page", tempsMap.get("page_count"));
		//当前页
		int currentPage = Integer.parseInt(String.valueOf(tempsMap.get("page")));
		//页总数
		int pages = Integer.parseInt(String.valueOf(tempsMap.get("page_count")));
		//下一页
		int dopage_url_next = 0;
		//上一页
		int dopage_url_previous = 0;
		if (currentPage < pages) {
			dopage_url_next = currentPage + 1;
		} else {
			dopage_url_next = currentPage;
		}
		if (currentPage > 1) {
			dopage_url_previous = currentPage - 1;
		} else {
			dopage_url_previous = currentPage;
		}
		//处理每一个分页的url
		List<Object> doPageUrlList = (ArrayList<Object>) tempsMap.get("array");
		for (int i = 0; i < doPageUrlList.size(); i++) {
			Map<String, String> pageM = new HashMap<String, String>();
            //页数 点击跳转url
			pageM.put("url", buildPageUrl(Long.parseLong(String.valueOf(doPageUrlList.get(i)))));
			pageM.put("page_num", String.valueOf(doPageUrlList.get(i)));
			doPageUrlList.set(i, pageM);
		}
        //分页可见的每一页的url
		context.set("dopage_url_list", doPageUrlList);
		//当前页
		context.set("current_page", tempsMap.get("page"));
		//下一页url
		context.set("dopage_url_next", buildPageUrl(dopage_url_next));
		//上一页url
		context.set("dopage_url_previous", buildPageUrl(dopage_url_previous));
		//第一页url
		context.set("dopage_url_first", buildPageUrl(1));
		//最后一页url
		context.set("dopage_url_end", buildPageUrl(pages));
		//页数
		context.set("page_count", tempsMap.get("page_count"));
		//记录数
		context.set("num_founds", tempsMap.get("record_count"));
		//处理排序数据
		String currentSort =sortMap.get("order");
		if(StringUtils.isBlank(currentSort))
		{
			currentSort=DEFAULT_SORT;
		}else
		{
			if("asc".equals(currentSort))
			{
				currentSort="1";
			}else
			{
				currentSort="0";
			}
		}
		//currentSort 1--升序  0--降序
		//推荐排序url
		String recommend_order_url=buildSortUrl("1",currentSort);
		//最多排序url
		String most_order_url=buildSortUrl("2",currentSort);
		context.set("recommend_order_url", recommend_order_url);
		context.set("most_order_url", most_order_url);
		//设置当前的sor
		context.set("select_sort",sortMap.get("select_sort"));
		context.set("select_sort_name",sortMap.get("sort_name"));
		context.set("select_current_sort",currentSort);
		
     }
	
	/**
	 * 根据分页构建url
	 */
	private String buildPageUrl(long pages) {
		
		String pageUrl = queryUrl.endsWith("/") ? queryUrl.substring(0, queryUrl.length() - 1) : queryUrl;
		String url = "";
		
		if(pageUrl.indexOf(queryPreStr)<0 )
		{
			url =getQueryOnlyPage(pages);
		}
		else
		{
			url = pageUrl.substring(0,(pageUrl.lastIndexOf("-")+1))+String.valueOf(pages);
		}
		
		url = ((url.endsWith("/")) ? url : url+"/");
		
		if(isSpecCity)
		{
			
			url =specJointUrl(url);
		}
		
		if(paramPageSize)
		{
			
			url +=getlinkParamStr(url)+"pageSize="+this.pageSize;
		}
		if(isOldUrl)
		{
			url =transOldUrl(url);
		}
		return url;
	}
	
	public String transOldUrl(String url)
	{
		Matcher a = urlParamerPattern.matcher(url);
		if(!a.find())
		{
			return url;
		}
		Matcher m= transOldUrlPattern.matcher(url);
		if(m.find())
		{
			
			String str = m.group();
			url = url.replace(str, oldUrlReplaceStr);
		}
		return url;
	}
	
	public String specJointUrl(String url)
	{
		String fValue=null;
		if( !StringUtils.isBlank(selectedValues.get(5)) && !DEFAULT_QUERY_NUM.equals(selectedValues.get(5)) )
		{
			fValue = selectedValues.get(5);
		}
		if(StringUtils.isBlank(fValue))
		{
			return url;
		}
		return specJointUrl(url,fValue);
	}
	
	public String specJointUrl(String url,String str)
	{
		String fValue =str;
		if(StringUtils.isBlank(fValue))
		{
			return url;
		}
		try{
			fValue = URLEncoder.encode(fValue,"utf-8");
		}catch(Exception e)
		{
			logger.error("QuanWuGouSolrService encode "+ fValue +" error,",e);
		}
		String tmp ="floorName="+fValue;
		url +=getlinkParamStr(url)+tmp;
		return url;
	}
	
	public String getlinkParamStr(String url)
	{
		if(url.indexOf("?")>=0)
		{
			return "&";
		}else
		{
			return "?";
		}
	}
	
	/**
	 * 根据排序构建url
	 * @param sort  排序列index 1---表示recommend 2---表示most
	 * @param oder  当前排序  1--升序   0--降序
	 * @return
	 */
	private String buildSortUrl(String sort, String oder) {
		String sortBaseUrl = queryUrl.endsWith("/") ? queryUrl.substring(0, queryUrl.length() - 1) : queryUrl;
		String url = "";
		String baseOrder=oder;
		//排序字段
		String baseSort=sortNames[Integer.parseInt(sort)-1];
		//如果当前点击了排序,就把order修改为相反的order
		
		if(baseSort.equals(sortMap.get("sort_name")) && !SORT_ONLY){
			baseOrder=oder.equals("1")?"0":"1";
		}
		
		if(sortBaseUrl.indexOf(queryPreStr)<0 )
		{
			url =getQueryOnlyPage(firstPage);
			url = url.replace(DEFALUT_SORT_STR, DEFALUT_SORT_PRE+baseSort+baseOrder);
		}
		else
		{
			
			//sortWords是之前的排序字段
			String sortWords = sortBaseUrl.substring(sortBaseUrl.indexOf(DEFALUT_SORT_PRE));
			sortWords = sortWords.substring(0,sortWords.indexOf("-"));
			url = sortBaseUrl.replace(sortWords, DEFALUT_SORT_PRE+baseSort+baseOrder);	
			
//			String matchUrl = "-"+DEFALUT_SORT_PRE+baseSort+"\\d*";
//			Matcher m = Pattern.compile(matchUrl).matcher(sortBaseUrl);
//			if (m.find()) {
//				url = sortBaseUrl.replaceAll(matchUrl,"-"+DEFALUT_SORT_PRE+baseSort+baseOrder);
//	
//			} 
		}
		
		url = ((url.endsWith("/")) ? url : url+"/");
		
		if(paramPageSize)
		{
			
			url +=getlinkParamStr(url)+"pageSize="+this.pageSize;
		}
		
		if(isSpecCity)
		{
			
			url =specJointUrl(url);
		}
		if(isOldUrl)
		{
			url =transOldUrl(url);
		}
		return url;
		
	}
	
	/** 获取分页数据 */
	private static Map<String, Object> doPageDatas(long count, long starts, String rows) {
		
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		
		/** 总数 */
	    pagerMap.put("record_count", count);

		/** 从多少条开始 下标 0.1.2....*/
		pagerMap.put("start", starts);

		/** 一页显示的条数 */
		int pageSize = Integer.parseInt(rows);
		pagerMap.put("pageSize", pageSize);

		/** 计算一共多少页 */
		long pages = count % pageSize;
		if (pages == 0) {
			pages = count / pageSize;
		} else {
			pages = count / pageSize + 1;
		}
        /** 页数 */
		pagerMap.put("page_count", pages);
		/** 当前页 */
		pagerMap.put("page", starts / pageSize + 1);

		ArrayList<Long> pagesNumList = new ArrayList<Long>();

		int allPages = 9;
		long currentPage = starts / pageSize + 1;
		long lower = 0;
		long higher = 0;
		if (currentPage <= 5) {
			//个位数页全显示
			if (pages <= allPages) {
				lower = 1;
				higher = pages;
			} else {
				lower = 1;
				higher = allPages;
			}
		} else if (currentPage <= 100) {
			//当前页前后4页
			if (pages - 4 >= currentPage) {
				lower = currentPage - 4;
				higher = currentPage + 4;
			} else {
				lower = currentPage - 4;
				higher = pages;
			}
		} else {
			//当前页前后3页
			if (pages - 3 >= currentPage) {
				lower = currentPage - 3;
				higher = currentPage + 3;
			} else {
				lower = currentPage - 3;
				higher = pages;
			}
		}
		for (long p = lower; p <= higher; p++) {
			pagesNumList.add(p);
		}
		pagerMap.put("array", pagesNumList);
		return pagerMap;
	}
	
	private void dealSpecSeoData(MyContext context)
	{
		context.set("title", ybjChengDuTitle);
		context.set("keywords", ybjChengDuKeywords);
		context.set("description", ybjChengDuDescription);
	}
	
	/**
	 * 处理seo等信息
	 * @param queryMap 
	 */
	private void dealSeoSolrAndLinkData(Map<String, String> queryMap, MyContext context) {
		
		if(isSpecCity)
		{
			dealSpecSeoData(context);
			return;
		}
		
		String[] seo =originalSeo;
		
		if(queryUrl.indexOf(queryPre)<0)
		{
			seo=channelSeo;
			context.set("title", seo[0]);
			context.set("keywords", seo[1]);
			context.set("description", seo[2]);
			return;
		}	
		
		int size =selectedNames.size();
		String title = new String(seoTitle);
		String keywords = new String(seoKeywords);
		String description = new String(seoDescription);
		
		String replaStyle="",replaType="",replaPrice="",replaSpace="";
		if(size>0)
		{
			for(int i=0;i<paramCount-2;i++)
			{
				String relace = ((selectedNames.get(i)==null)?"": selectedNames.get(i));
				if(i==0)
				{
					replaStyle=relace;
				}
				if(i==1)
				{
					replaPrice=relace;
				}
				if(i==2)
				{
					replaType=relace;
				}
				if(i==3)
				{
					replaSpace=relace;
				}
			}
		}
		title=title.replace("${0}", replaStyle).replace("${1}", replaType).replace("${2}", replaPrice).replace("${3}",replaSpace);
		keywords=keywords.replace("${0}", replaStyle).replace("${1}", replaType).replace("${2}", replaPrice).replace("${3}",replaSpace);
		description=description.replace("${0}", replaStyle).replace("${1}", replaType).replace("${2}", replaPrice).replace("${3}",replaSpace);
	
		context.set("title", title);
		context.set("keywords", keywords);
		context.set("description", description);
	}
	
	/**
	 * 处理当前搜索状态信息
	 * @param context
	 */
	public void dealCurrentStatus(MyContext context)
	{
		context.set("current_style_order", getCurrentSelectValue(0,""));
		context.set("current_style_name", getCurrentSelectName(0,""));
		context.set("current_type_order", getCurrentSelectValue(2,""));
		context.set("current_type_name", getCurrentSelectName(2,""));
		context.set("current_price_order", getCurrentSelectValue(1,""));
		context.set("current_price_name", getCurrentSelectName(1,""));
		context.set("current_space_order", getCurrentSelectValue(3,""));
		context.set("current_space_name", getCurrentSelectName(3,""));
		if(isSpecCity)
		{
			context.set("current_city_id", getCurrentSelectValue(4,""));
			context.set("current_floor_name", getCurrentSelectName(5,""));
		}
	}
	
	/**
	 *  总的处理搜索选项数据
	 */
	public void dealSearchTabsData(SolrjOperator solrOprator, MyContext context) throws Exception
	{
		if(!isSpecCity)
		{
			dealGeneralSearchTabsData(context);
		}else
		{
			dealSpecSearchTabsData(solrOprator,context);
			dealRadioTabsData(context);
		}
	}
	
	/**
	 *  处理特殊的搜索选项数据以前的
	 */
	@SuppressWarnings("unchecked")
	public void dealSpecSearchTabsDataOld(MyContext context)
	{
        List<Map<String, String>> districtDatas = InitQwgSolrDao.getSuitChengDuDistrictList();
		
        List<Map<String,Object>> resultMap =new ArrayList<Map<String,Object>>();
        
        List<String> copeList =copyList(curValues);
        
		dealPerSearchData(districtDatas,true,6 ,resultMap,copeList);
		
		Object obj =context.getValueMap().get("select_list");
		if(obj!=null)
		{
			 List<Map<String,Object>> rm = (List<Map<String,Object>>)obj;
			 resultMap.addAll(rm);
		}
		context.set("select_list", resultMap);
	}
	
	
	/**
	 *  处理特殊的搜索选项数据
	 */
	@SuppressWarnings("unchecked")
	public void dealSpecSearchTabsData(SolrjOperator solrOprator,MyContext context) throws Exception
	{
		if(InitQwgSolrDao.isNeedFreshDistrictFloorFlag())
		{
			Map<String,Object> factQueryMap = getSolrFacetAreaParameter();
			areaList = queryParamFacetArea(factQueryMap,solrOprator);
			
			InitQwgSolrDao.setNeedFreshDistrictFloorFlag(false);
		}
		//追加到context的数据
		List<Map<String,Object>> resultMap =new ArrayList<Map<String,Object>>();
		  
        List<String> copeList =copyList(curValues);
        
		dealPerSearchData(areaList,true,6 ,resultMap,copeList);
		
		Object obj =context.getValueMap().get("select_list");
		if(obj!=null)
		{
			 List<Map<String,Object>> rm = (List<Map<String,Object>>)obj;
			 resultMap.addAll(rm);
		}
		context.set("select_list", resultMap);
	}
	
	/**
	 * 准备facet区域参数数据
	 */
	public Map<String, Object> getSolrFacetAreaParameter() {
		
		Map<String, Object> solrFacetField = new HashMap<String, Object>();
		
		StringBuffer sb = new StringBuffer();
		String queryName ="city_id";
		String [] queryParamNames = InitQwgSolrDao.getQueryParamNames();
		if(queryParamNames!=null && queryParamNames.length>4)
		{
		   queryName = queryParamNames[4];
		}
		String curCity = String.valueOf(InitQwgSolrDao.getSpecCity());
		sb.append(queryName+":").append(curCity);
		
		solrFacetField.put("facet.query", sb.toString());
		    
		String [] facetFieldNames = InitQwgSolrDao.getFacetFieldsAreaNames();
		List<String> list = new LinkedList<String>();
		if(facetFieldNames!=null && facetFieldNames.length>0)
		{
			for(String p:facetFieldNames)
			{
				list.add(p);
			}
		}
		solrFacetField.put("facet.field", list);
		
		return solrFacetField;
	}
	
	/**
	 * facet查询
	 */
	public List<Map<String, String>> queryParamFacetArea(Map<String, Object> solrFactParameter,SolrjOperator solrOprator) throws Exception
	{
		List<FacetField> facetResult = solrOprator.factQuery(solrFactParameter);
		//包装url后的数据
	    List<Map<String, String>> list= new ArrayList<Map<String,String>>();
		
		long totalCount =0;
		
		for(FacetField fecetField:facetResult){
			List<Count> facetCount=fecetField.getValues();
			if(facetCount==null || facetCount.size()==0){continue;}
			
			Map<String,String> paramsMap = null;
			
			for(Count count:facetCount){
				String info = count.getName();//西昌市;15482
				if(StringUtils.isBlank(info))
				{ 
					continue;
				}
				String ays []= info.split(";");
				String name = ays[0];
				
				String id="0";
				if(ays.length>1)
				{
					id= ays[1];
				}
				paramsMap=new HashMap<String,String>();
				paramsMap.put("count",String.valueOf(count.getCount()));
				paramsMap.put("name", name);
				paramsMap.put("id", id);
				paramsMap.put("order", id);
				String pinyin = Pinyin4jUtil.getPinYin(name.substring(0, 1));
				if(!StringUtils.isBlank(pinyin))
				{
					paramsMap.put("pinyin",pinyin);
				}
				list.add(paramsMap);
				totalCount += count.getCount();
			}
		}
		//id排序
		if(list.size()>0)
		{
			Collections.sort(list, new Comparator<Map<String, String>>(){
				
				 public int compare(Map<String, String> v1, Map<String, String> v2)
				 {
					String c1 = v1.get("id");
					String c2 = v2.get("id");
					int id1=0;
					int id2=0;
					if(NumberUtil.isNumeric(c1))
					{
						id1 =Integer.parseInt(c1);
					}
					if(NumberUtil.isNumeric(c2))
					{
						id2 = Integer.parseInt(c2);
					}
					
	    			if(id1<id2)
	    			{
	    				 return -1;
	    			}
	    			if(id1>id2)
	    			{
	    				return 1;
	    			}
	    			return 0;
				 }
			} );
		}
		Map<String,String> paramsMap = new HashMap<String,String>();
		paramsMap.put("name","不限");
		paramsMap.put("count",""+totalCount);
		paramsMap.put("order","0");
		paramsMap.put("id","0");
		list.add(0, paramsMap);
		
		facetResult = null;
		return list;
	}
	
	/**
	 * 热门推荐楼盘tab数据
	 */
	@SuppressWarnings("unchecked")
	public void dealSearchFloorTabsData(MyContext context)
	{
		
        List<String> floorList = InitQwgSolrDao.getSuitChengDuFloorList();
		
        List<Map<String,Object>> resultMap =new ArrayList<Map<String,Object>>();
        
        List<Map<String, String>> list= new ArrayList<Map<String,String>>();
       
        for(String p:floorList)
        {
        	Map<String,String> map =wrapSpecUrlWithCurValue(p,true);
        	list.add(map);
        }
        
        int index=5;
        Map<String,Object> floorMap = new HashMap<String,Object>();
        floorMap.put("tab", getTabNameByIndex(index));
       // floorMap.put("tab_name", getTabShowNameByIndex(index));
        floorMap.put("tab_name","热门楼盘推荐");
        
        List<Map<String,Object>> finalList =new ArrayList<Map<String,Object>>();
        Map<String,Object> wrapMap = new HashMap<String,Object>();
        wrapMap.put("zm", "");
        wrapMap.put("list", list);
		finalList.add(wrapMap);
        
        floorMap.put("data", finalList);
		
        floorMap.put("selected_order", getCurrentSelectValue(index,floorList.get(0)));
        floorMap.put("selected_name", getCurrentSelectName(index,floorList.get(0)));
		
//        String currentFloor = getCurrentSelectValue(index,"");
//        
//        if(!StringUtils.isBlank(currentFloor) && !floorList.contains(currentFloor))
//        {
//        	Map<String,String> map =wrapSpecUrl(currentFloor,true);
//        	if(list.size()>0)
//        	{
//        		list.add(1, map);
//        	}else
//        	{
//        		list.add(map);
//        	}
//        }
//        
        resultMap.add(floorMap);
        
        Object obj =context.getValueMap().get("select_list");
		if(obj!=null)
		{
			 List<Map<String,Object>> rm = (List<Map<String,Object>>)obj;
			 resultMap.addAll(rm);
		}
		context.set("select_list", resultMap);
	}
	
	/**
	 * 组装 特殊url 
	 * @param floor
	 * @param senseCurrent  对当前的查询值 是否敏感
	 * @return
	 */
	private Map<String,String> wrapSpecUrlWithCurValue(String floor,boolean senseCurrent)
	{
		
		return wrapSpecUrlWithCurValue(floor,floor,senseCurrent);
	}
	
	/**
	 * 组装 特殊url 
	 * @param name
	 * @param order
	 * @param senseCurrent  对当前的查询值 是否敏感
	 * @return
	 */
	private Map<String,String> wrapSpecUrlWithCurValue(String name,String order,boolean senseCurrent)
	{
		
		Map<String,String> map = new HashMap<String,String>();
    	map.put("name", name);
    	map.put("order", order);
    	
    	String newUrl =getSelectTabUrl(curValues,firstPage);
		if(newUrl.indexOf(getQueryNoParam())>=0)
		{
			newUrl=queryPreNoParamStr+"/";
		}
    	
    	if(paramPageSize)
		{
			
			newUrl +=getlinkParamStr(newUrl)+"pageSize="+this.pageSize;
		}
		if(!senseCurrent)
		{
			newUrl =specJointUrl(newUrl,name);
		}else if(!"不限".equals(name) && !name.equalsIgnoreCase(selectedValues.get(5)))
		{
			newUrl =specJointUrl(newUrl,name);
		}
		if(isOldUrl)
		{
			newUrl =transOldUrl(newUrl);
		}
    	map.put("url", newUrl);
    	return map;
	}
	
	/**
	 * 组装 特殊url 
	 * @param floor
	 * @param senseCurrent  对当前的查询值 是否敏感
	 * @return
	 */
	private Map<String,String> wrapSpecUrl(String floor,boolean senseCurrent)
	{
		Map<String,String> map = new HashMap<String,String>();
    	map.put("name", floor);
    	map.put("order", floor);
    	
    	String newUrl=queryPreNoParamStr+"/";
		
    	if(paramPageSize)
		{
			
			newUrl +=getlinkParamStr(newUrl)+"pageSize="+this.pageSize;
		}
		if(!senseCurrent)
		{
			newUrl =specJointUrl(newUrl,floor);
		}else if(!"不限".equals(floor) && !floor.equalsIgnoreCase(selectedValues.get(5)))
		{
			newUrl =specJointUrl(newUrl,floor);
		}
		if(isOldUrl)
		{
			newUrl =transOldUrl(newUrl);
		}
    	map.put("url", newUrl);
    	return map;
	}
	
	/**
	 *  处理一般的搜索选项数据
	 */
	public void dealGeneralSearchTabsData(MyContext context)
	{
		boolean isSuitStyleShow = InitQwgSolrDao.isSuitStyleShow();
		boolean isSuitPriceIntlShow   = InitQwgSolrDao.isSuitPriceIntlShow();
		boolean isSuitTypeShow = InitQwgSolrDao.isSuitTypeShow();
		boolean isSuitSpaceShow = InitQwgSolrDao.isSuitSpaceShow();
		
		List<Map<String, String>> styleDatas = InitQwgSolrDao.getSuitStyleList();
		List<Map<String, String>> priceDatas = InitQwgSolrDao.getSuitPriceIntlList();
		List<Map<String, String>> typeDatas = InitQwgSolrDao.getSuitTypeList();
		List<Map<String, String>> spaceDatas = InitQwgSolrDao.getSuitSpaceList();
		
		List<Map<String,Object>> resultMap =new ArrayList<Map<String,Object>>();
		
		List<String> copeList =copyList(curValues);
		List<String> copeList1 =copyList(curValues);
		List<String> copeList2 =copyList(curValues);
		List<String> copeList3 =copyList(curValues);
		
		dealPerSearchData(styleDatas,isSuitStyleShow,0 ,resultMap,copeList);
		dealPerSearchData(priceDatas,isSuitPriceIntlShow,1 ,resultMap,copeList1);
		dealPerSearchData(typeDatas,isSuitTypeShow,2 ,resultMap,copeList2);
		dealPerSearchData(spaceDatas,isSuitSpaceShow,3 ,resultMap,copeList3);
		
		context.set("select_list", resultMap);
	}
	
	/**
	 *  处理排序列表数据
	 */
	public void dealSortData(MyContext context)
	{
		List<Map<String,String>> resultMap =new ArrayList<Map<String,String>>();
		
		int size = sortShowNames.length;
		
		for(int i=0;i<size;i++)
		{
			Map<String,String> map = new HashMap<String,String>();
			map.put("show_name", sortShowNames[i]);
			map.put("name", sortNames[i]);
			map.put("sort_id", (i+1)+"");
			map.put("selected", "0"); //莫选中
			if(i==0)
			{
			    map.put("url", String.valueOf(context.getValueMap().get("recommend_order_url")));
			}
			if(i==1)
			{
				map.put("url", String.valueOf(context.getValueMap().get("most_order_url")));
			}
			Object o =context.getValueMap().get("select_sort_name");
			if(o!=null && sortNames[i].equalsIgnoreCase(String.valueOf(o)) )
			{
				map.put("selected", "1");//选中
			}
			
			resultMap.add(map);
		}
		context.set("sort_list", resultMap);
	}
	
	/**
	 * 处理每一维度搜索数据
	 * list ---数据节点
	 * isShow ---是否展示
	 * index ---哪下标数据
	 * resultMap ---保存结果字段
	 * curValues ---当前参数值
	 */
	public void dealPerSearchData(List<Map<String, String>> list,boolean isShow,int index ,List<Map<String,Object>> resultMap,List<String> curValues)
	{
		dealPerSearchData(list,isShow,index ,resultMap,curValues,false);
	}
	
	/**
	 * 处理每一维度搜索数据
	 * list ---数据节点
	 * isShow ---是否展示
	 * index ---哪下标数据
	 * resultMap ---保存结果字段
	 * curValues ---当前参数值
	 * needFloor 
	 */
	public void dealPerSearchData(List<Map<String, String>> list,boolean isShow,int index ,List<Map<String,Object>> resultMap,List<String> curValues,boolean needFloor)
	{
		if(isShow && (list!=null && list.size()>0))
		{
			String defOrder = "";
			String defName ="";
			for(Map<String,String> map:list)
			{
				if(!StringUtils.isBlank(map.get("url")))
				{
				    map.remove("url");
				}
				
				String assembType = map.get("order");
				String curValue ="0";
				if(curValues!=null && curValues.size()>index)
				{
					//logger.info("curValues index "+index+",value="+ curValues.get(index)+",assembType="+assembType);
					curValue = curValues.get(index);
				}
				if(curValue.equalsIgnoreCase(assembType) && assembType.equals("0"))
				{
					defOrder=assembType;
					defName= map.get("name");
				}
				if(curValue.equalsIgnoreCase(assembType))
				{
					assembType="0";
				}
				curValues.set(index, assembType);
				String newUrl =getSelectTabUrl(curValues,firstPage);
				curValues.set(index, curValue);
				if(newUrl.indexOf(getQueryNoParam())>=0)
				{
					newUrl=queryPreNoParamStr+"/";
				}
				
				if(paramPageSize)
				{
					
					newUrl +=getlinkParamStr(newUrl)+"pageSize="+this.pageSize;
				}
				if(needFloor)
				{
					
					newUrl =specJointUrl(newUrl);
				}
				if(isOldUrl)
				{
					newUrl =transOldUrl(newUrl);
				}
				map.put("url", newUrl);
			}

			Map<String,Object> typeMap = new HashMap<String,Object>();
			typeMap.put("tab", getTabNameByIndex(index));
			typeMap.put("tab_name", getTabShowNameByIndex(index));
			typeMap.put("data", list);
			
			typeMap.put("selected_order", getCurrentSelectValue(index,defOrder));
			typeMap.put("selected_name", getCurrentSelectName(index,defName));
			
			resultMap.add(typeMap);
		}
	}
	
	/**
	 * 获取搜索条件维度名称
	 * @return
	 */
	public String getTabNameByIndex(int index)
	{
		if(index>=searchNames.length)
		{
			return "";
		}
		return searchNames[index];
	}
	
	/**
	 * 获取搜索条件维度展示名
	 * @return
	 */
	public String getTabShowNameByIndex(int index)
	{
		if(index>=searchShowNames.length)
		{
			return "";
		}
		return searchShowNames[index];
	}
	
	/**
	 * 深度复制列表
	 */
	public List<String> copyList(List<String> curValues)
	{
		if(curValues==null)
		{
			return null;
		}
		List<String> copeList =new LinkedList<String>();
		CollectionUtils.addAll(copeList,  new  Object[curValues.size()]);
		Collections.copy(copeList, curValues);
		
		return copeList;
	}
	
	/**
	 * 获取url前缀,貌似返回空
	 * @return
	 */
	private String getQueryUrlPre()
	{
		return queryUrl.substring(0,queryUrl.indexOf(queryPreNoParamStr));
	}
	
	/**
	 * 获取带可扩展条件带分页的url
	 * @param list  LinkedList 必须排序
	 * @param page   页数
	 * @return
	 */
	private String getSelectTabUrl(List<String> list,long page)
	{
		StringBuffer urlBuf = new StringBuffer();
		urlBuf.append(getQueryUrlPre());
		urlBuf.append(queryPreStr);
		for(String p:list)
		{
			urlBuf.append(p).append("-");
		}
		urlBuf.append(DEFALUT_SORT_STR).append("-");
		urlBuf.append(page);
		urlBuf.append("/");
		return urlBuf.toString();
	}
	
	/**
	 * 获取当前状态值
	 * @param pos  位置 0---风格中的具体某个类型值  1----价格区间中的具体某个类型值  2----户型中的具体某个类型值
	 * @param def  默认值
	 * @return
	 */
	private String getCurrentSelectValue(int pos,String def)
	{
		String curValue = ((selectedValues.get(pos)==null)?def:selectedValues.get(pos));
		return curValue;
	}
	
	/**
	 * 获取当前状态展示名称值
	 * @param pos  位置 0---风格中的具体某个类型名  1----价格区间中的具体某个类型名  2----户型中的具体某个类型名
	 * @param def  默认值
	 * @return
	 */
	private String getCurrentSelectName(int pos,String def)
	{
		String curValue = ((selectedNames.get(pos)==null)?def:selectedNames.get(pos));
		return curValue;
	}
	
	/**
	 * 清除数据，释放资源
	 */
	public void clear()
	{
		this.server = null;
		this.resultHandler = null;
	}
	
	public void buildFacetParameter()
	{
		String [] facetParamNames = InitQwgSolrDao.getFacetParamNames();
		List<String> list = new LinkedList<String>();
		if(facetParamNames!=null && facetParamNames.length>0)
		{
			for(String p:facetParamNames)
			{
				list.add(p);
			}
		}
		factParameterMap.put("facet.pivot", list);
	}
	
	/**
	 * 处理区域楼盘
	 */
	public void dealParamFacetPivotQuery(Map<String, String> solrQueryMap, MyContext context) throws Exception
	{
		if(InitQwgSolrDao.isNeedFreshDistrictFloorFlag())
		{
			queryFacetPivotDate(solrQueryMap);
			InitQwgSolrDao.setNeedFreshDistrictFloorFlag(false);
		}
		context.set("propertyList", districtFloorList);
		
	}
	
	/**
	 * 准备facet参数数据
	 */
	public Map<String, Object> getSolrFacetParameter() {
		
		Map<String, Object> solrFacetField = new HashMap<String, Object>();
		
		StringBuffer sb = new StringBuffer();
		String queryName ="district";
		String [] queryParamNames = InitQwgSolrDao.getQueryParamNames();
		if(queryParamNames!=null && queryParamNames.length>6)
		{
		   queryName = queryParamNames[6];
		}
		String curDistrict = getCurrentSelectValue(6,DEFAULT_QUERY_NUM);
		sb.append(queryName+":").append(curDistrict);
		
		solrFacetField.put("facet.query", sb.toString());
		    
		String [] facetFieldNames = InitQwgSolrDao.getFacetFieldsNames();
		List<String> list = new LinkedList<String>();
		if(facetFieldNames!=null && facetFieldNames.length>0)
		{
			for(String p:facetFieldNames)
			{
				list.add(p);
			}
		}
		solrFacetField.put("facet.field", list);
		
		return solrFacetField;
	}
	
	/**
	 * facet查询
	 */
	public void dealParamFacetQuery(Map<String, Object> solrFactParameter,SolrjOperator solrOprator,MyContext context) throws Exception
	{
		List<FacetField> facetResult = solrOprator.factQuery(solrFactParameter);
		dealFacetData(solrFactParameter, context, facetResult);
		facetResult = null;
	}
	
    /**
     * 处理facet查询结果
     */
	@SuppressWarnings("unchecked")
	public void dealFacetData(Map<String, Object> solrFactParameter,MyContext context, List<FacetField> facetResult) {
		
		//追加到context的数据
		List<Map<String,Object>> resultMap =new ArrayList<Map<String,Object>>();
		
		//facet查询出来的数据
		List<Map<String, String>> floorList =new ArrayList<Map<String, String>>();
		
		List<Map<String,Object>> finalList = new LinkedList<Map<String,Object>>();
		//包装url后的数据
	    //List<Map<String, String>> list= new ArrayList<Map<String,String>>();
	    
	    //楼盘tab项数据
		Map<String,Object> floorMap=new HashMap<String,Object>();
		
		long totalCount =0;
		
		for(FacetField fecetField:facetResult){
			List<Count> facetCount=fecetField.getValues();
			if(facetCount==null || facetCount.size()==0){continue;}
			
			Map<String,String> paramsMap = null;
			
			for(Count count:facetCount){
				String name = count.getName();
				if(StringUtils.isBlank(name))
				{ 
					continue;
				}
				paramsMap=new HashMap<String,String>();
				paramsMap.put("count",String.valueOf(count.getCount()));
				paramsMap.put("name", name);
				String pinyin = Pinyin4jUtil.getPinYin(name.substring(0,1));
				if(!StringUtils.isBlank(pinyin))
				{
					paramsMap.put("pinyin",pinyin);
				}
				floorList.add(paramsMap);
				totalCount += count.getCount();
			}
		}
		//按照字母排序
		if(floorList.size()>0)
		{
			Collections.sort(floorList, new Comparator<Map<String, String>>(){
				
				 public int compare(Map<String, String> v1, Map<String, String> v2)
				 {
					String c1 = v1.get("pinyin");
					String c2 = v2.get("pinyin");
					if(StringUtils.isBlank(c1))
					{
						c1 = "zzz";
					}
					if(StringUtils.isBlank(c2))
					{
						c2 = "zzz";
					}
					char a = c1.toCharArray()[0];
	    			char b = c2.toCharArray()[0];
	    			if(a<b)
	    			{
	    				 return -1;
	    			}
	    			if(a>b)
	    			{
	    				return 1;
	    			}
	    			return 0;
				 }
			} );
		}
		Map<String,String> paramsMap = new HashMap<String,String>();
		paramsMap.put("name","不限");
		paramsMap.put("count",""+totalCount);
		floorList.add(0, paramsMap);
		
		String firstChar="";
		List<Map<String, String>> nullFirstCharList= new ArrayList<Map<String, String>>();
		List<Map<String,String>> tmpList =null;
		for(Map<String, String> p:floorList)
        {
        	Map<String,String> map =wrapSpecUrlWithCurValue(p.get("name"),true);
        	map.put("count", p.get("count"));
        	String pinyin = p.get("pinyin");
        	if(!StringUtils.isBlank(pinyin))
        	{
        		String py=pinyin.substring(0, 1);
        		if(!firstChar.equals(py))
        		{
        			if(!StringUtils.isBlank(firstChar))
        			{
        				if(tmpList!=null)
        				{
        					Map<String,Object> obj = new HashMap<String,Object>();
        					obj.put("zm", firstChar.toUpperCase());
        					obj.put("list", tmpList);
        					finalList.add(obj);
        				}
        			}
        			firstChar= py;
        			//map.put("name", py.toUpperCase()+" "+map.get("name"));
        			tmpList =  new ArrayList<Map<String, String>>();
        		}
        		tmpList.add(map);	
        		
        	}else
        	{
        		nullFirstCharList.add(map);
        	}
        }
		if(tmpList!=null && tmpList.size()>0)
		{
			Map<String,Object> obj = new HashMap<String,Object>();
			obj.put("zm", firstChar.toUpperCase());
			obj.put("list", tmpList);
			finalList.add(obj);
		}
		if(nullFirstCharList!=null && nullFirstCharList.size()>0)
		{
			Map<String,Object> obj = new HashMap<String,Object>();
			obj.put("zm", "");
			obj.put("list", nullFirstCharList);
			finalList.add(0,obj);
		}
		
		int index=5;
		floorMap.put("tab", getTabNameByIndex(index));
	    floorMap.put("tab_name",getCurrentSelectName(6,"")+"楼盘");
	    floorMap.put("data", finalList);
			
	    floorMap.put("selected_order", getCurrentSelectValue(index,floorList.get(0).get("name")));
	    floorMap.put("selected_name", getCurrentSelectName(index,floorList.get(0).get("name")));
			
		resultMap.add(floorMap);
        
        Object obj =context.getValueMap().get("select_list");
		if(obj!=null)
		{
			 List<Map<String,Object>> rm = (List<Map<String,Object>>)obj;
			 resultMap.addAll(rm);
		}
		context.set("select_list", resultMap);
	}
	
	private void queryFacetPivotDate(Map<String, String> solrQueryMap) throws Exception {
		// 获得facet的数据结构。
		QueryResponse resp = facetPivotQuery(solrQueryMap);
		if(resp==null)
		{
			districtFloorList =new ArrayList<FacetVo>();
			return;
		}
		districtFloorList =getFacetPivotData(resp);
	}
	
	/**查询FacetPivot**/
	private  QueryResponse facetPivotQuery(Map<String, String> solrQueryMap){
		
		String SOLR_SEPARATOR		= " AND ";
		StringBuffer sb = new StringBuffer();
		for (String o : solrQueryMap.keySet()) {
			String value = solrQueryMap.get(o);
			if (StringUtils.isNotEmpty(value)) {
				
				sb.append(o).append(":").append(value).append(SOLR_SEPARATOR);
			}
		}
        
		if (sb.toString().endsWith(SOLR_SEPARATOR)) {
			sb.delete(sb.toString().length() - SOLR_SEPARATOR.length(), sb.toString().length());
		}
		String queryStr = sb.toString();
		
		List<String> facetList =factParameterMap.get("facet.pivot");
		
		String facetStr="";
		if(facetList!=null && facetList.size()>0)
		{
			for(String f:facetList)
			{
				facetStr +=f+",";
			}
			if(facetStr.endsWith(","));
			{
				facetStr = facetStr.substring(0,facetStr.length()-1);
			}
		}
		else
		{
			return null;
		}
		
		try {
			SolrQuery query = new SolrQuery();
			query.setQuery(queryStr);
			query.setRows(0);
			query.setFacet(true);
			query.setParam("facet.pivot", facetStr);
			query.setFacetLimit(-1);
			QueryResponse resp = server.query(query);
			return resp;
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<FacetVo> getFacetPivotData(QueryResponse resp ) {
		
		List<FacetVo> tmpList = new LinkedList<FacetVo>();
		NamedList<List<PivotField>> result = resp.getFacetPivot();
		if(result==null)
		{
			return tmpList;
		}
		for(int i = 0;i<result.size();i++){
			
			List<PivotField> list1 = result.getVal(i);
			if(list1==null)
			{
				continue;
			}
			for(int j=0;j<list1.size();j++){//第一层
				PivotField pf1 = list1.get(j);
				fetchNode(tmpList,pf1);
			}
		}
		Collections.sort(tmpList, new Comparator<FacetVo>(){
			
			 public int compare(FacetVo v1, FacetVo v2)
			 {
				String c1 = v1.getId();
				String c2 = v2.getId();
				if(StringUtils.isBlank(c1))
				{
					c1 = String.valueOf(Integer.MAX_VALUE);
				}
				if(StringUtils.isBlank(c2))
				{
					c2 = String.valueOf(Integer.MAX_VALUE);
				}
				int n1 =Integer.parseInt(c1);
				int n2 =Integer.parseInt(c2);
				if(n1<n2)
				{
					return -1;
				}
				if(n1>n2)
				{
					return 1;
				}
				return 0; 
			 }
			
		} );
		return tmpList;
		
	}
	
	
	
	/**设置分类信息**/
	private void fetchNode(List<FacetVo> clist,PivotField pf1) {
		
		List<PivotField> list2 = pf1.getPivot();
		if(list2==null || list2.size()<=0)
		{
			return;
		}
		boolean canAdd=false;
		
		FacetVo vo = new FacetVo();
		String value = pf1.getValue().toString();
		String vs [] = value.split(";");
		
		vo.setName(vs[0]);
		vo.setId( ((vs.length>1)?vs[1]:String.valueOf(Integer.MAX_VALUE))  );
		vo.setCount(String.valueOf(pf1.getCount()));
		
		for(int k = 0;k<list2.size();k++){//第二层
			PivotField pf2 = list2.get(k);
			FacetVo childVo = new FacetVo();
			String cldValue = pf2.getValue().toString();
			if(StringUtils.isBlank(cldValue))
			{
				continue;
			}	
			String cvs [] = cldValue.split(";");
			
			childVo.setName(cvs[0]);
			childVo.setId( ((cvs.length>1)?cvs[1]:String.valueOf(Integer.MAX_VALUE))  );
			childVo.setCount(String.valueOf(pf2.getCount()));
			Map<String,String> map =wrapSpecUrl(childVo.getName(),false);
			childVo.setUrl(map.get("url"));
			vo.addFacet(childVo);
			canAdd=true;
		}
		if(canAdd)
		{
			if(vo.getList()!=null && vo.getList().size()>1)
			{
				List<FacetVo> childList = vo.getList();
				for(FacetVo vv: childList)
				{
					if(!StringUtils.isBlank(vv.getName()))
					{
						String pinyin = Pinyin4jUtil.getPinYin(vv.getName().substring(0,1));
						if(!StringUtils.isBlank(pinyin))
						{
						   vv.setId(pinyin);
						}
					}
				}
				Collections.sort(childList, new Comparator<FacetVo>(){
					
					 public int compare(FacetVo v1, FacetVo v2)
					 {
						String c1 = v1.getId();
						String c2 = v2.getId();
						if(StringUtils.isBlank(c1))
						{
							c1 = "zzz";
						}
						if(StringUtils.isBlank(c2))
						{
							c2 = "zzz";
						}
						char a = c1.toCharArray()[0];
		    			char b = c2.toCharArray()[0];
		    			if(a<b)
		    			{
		    				 return -1;
		    			}
		    			if(a>b)
		    			{
		    				return 1;
		    			}
		    			return 0;
					 }
				} );
			}
			
		    clist.add(vo);
		}
	}
	
	public static void main(String args [])
	{
		
		String aaa="wfehfiwhf/fwfwf";
		Pattern p =Pattern.compile("^/{2,}");
		Matcher m =p.matcher(aaa);
		if(m.find())
		{
			aaa=aaa.replace(m.group(), "/");
		}
		System.out.println(aaa);
		
		
		String parameterValue="sort_1".replaceAll("[a-zA-Z_]", "");
		System.out.println(parameterValue+"yyy");
		String curStyle = "1";
		String curPrice = "2";
		String curType = "3";
		List<String> curValues = new LinkedList<String>();
		curValues.add(curStyle);
		curValues.add(curPrice);
		curValues.add(curType);
		
		List<String> copeList =new LinkedList<String>();
		CollectionUtils.addAll(copeList,  new  Object[curValues.size()]);
		Collections.copy(copeList, curValues);
		
		System.out.println(copeList.size());
		System.out.println(copeList.getClass().getName());
		for(String t:copeList)
		{
			System.out.println(t);
			
		}
	}
	
	public String getQueryNoParam()
	{
		StringBuffer buf = new StringBuffer(queryPreStr);
		for(int i=0;i<paramCount-2;i++)
		{
			buf.append(DEFAULT_QUERY_NUM).append("-");
		}
		buf.append(DEFALUT_SORT_STR).append("-").append(firstPage).append("/");
		return buf.toString();
	}
	
	public String getQueryOnlyPage(long page)
	{
		StringBuffer buf = new StringBuffer(queryPreStr);
		for(int i=0;i<paramCount-2;i++)
		{
			buf.append(DEFAULT_QUERY_NUM).append("-");
		}
		buf.append(DEFALUT_SORT_STR).append("-").append(page).append("/");
		return buf.toString();
	}
	
	/**处理单选项相关tab数据**/
	@SuppressWarnings("unchecked")
	public void dealRadioTabsData(MyContext context)
	{
	
		List<Map<String, String>> radioList = new LinkedList<Map<String,String>>();

        for(int i=0;i<radioChoose.length;i++)
        {
        	Map<String,String> map = new HashMap<String,String>();
        	map.put("name", radioChoose[i]);
        	map.put("order",""+(i+1));
        	radioList.add(map);
        }
		//追加到context的数据
		List<Map<String,Object>> resultMap =new ArrayList<Map<String,Object>>();
		  
        List<String> copeList =copyList(curValues);
        
		dealPerSearchData(radioList,true,7 ,resultMap,copeList,true);
		
		Object obj =context.getValueMap().get("select_list");
		if(obj!=null)
		{
			 List<Map<String,Object>> rm = (List<Map<String,Object>>)obj;
			 resultMap.addAll(rm);
		}
		context.set("select_list", resultMap);
    }
}
