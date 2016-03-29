package com.solr.initialize;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitQwgSolrDao extends InitDao{
    
    private	final static Logger logger = LoggerFactory.getLogger(InitQwgSolrDao.class);
    
    //参数名称
    private static String [] queryParamNames={"suit_style","suit_price_interval","suit_house_type","space_types","city_id","floor_name","district"};
	
    //facet列名 facet.pivot字段
    private static String [] facetParamNames={"district_all","floor_name"};
    
    //facet.field字段
    private static String [] facetFieldsNames ={"floor_name"};
    
    //facet.field 区域字段 
    private static String [] facetFieldsAreaNames= {"district_all"};
    
    //单选筛选参数名  全景图
    private static String [] radioQueryParamNames={"panoramagram"};
    
    //特殊城市
    private static int specCity=272;//成都
    
    //刷新区域楼盘的标记
    private static boolean needFreshDistrictFloorFlag=true;
    
    //不限
    private static Map<String,String> buxian = new HashMap<String,String>();

	private static String QUERY_SUIT_STYLE_SQL="select * from ecs_ad_type where parent_id =(select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_suitStyle' and is_valide =1 ) and is_valide =1 order by `order`";
	private static String QUERY_SUIT_PRICE_INTL_SQL="select * from ecs_ad_type where parent_id =(select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_suitPricerange' and is_valide =1 ) and is_valide =1 order by `order`";
	private static String QUERY_SUIT_TYPE_SQL="select * from ecs_ad_type where parent_id =(select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_houseType' and is_valide =1 ) and is_valide =1 order by `order`";
	private static String QUERY_SUIT_SPACE_SQL="select * from ecs_ad_type where parent_id =(select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_spaceType2' and is_valide =1 ) and is_valide =1 order by `order` ";
	
	private static String QUERY_SHOW_SUIT_STYLE_SQL="select * from ecs_cus_ad where cat_id =(select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_suitStyle' and is_valide =1)";
	private static String QUERY_SHOW_SUIT_PRICE_INTL_SQL="select * from ecs_cus_ad where cat_id =(select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_suitPricerange' and is_valide =1)";
	private static String QUERY_SHOW_SUIT_TYPE_SQL="select * from ecs_cus_ad where cat_id =(select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_houseType' and is_valide =1)";
	private static String QUERY_SHOW_SUIT_SPACE_SQL="select * from ecs_cus_ad where cat_id =(Select id from ecs_ad_type where priv_code = 'quanwugou_act' and ad_code = 'QWG_spaceType2' and is_valide =1)";
	
	private static String QUERY_SUIT_CHENGDU_FLOOR_SQL="select * from ecs_cus_ad where cat_id =(Select id from ecs_ad_type where ad_code = 'CDYBJ_pdy_hotestate' and is_valide =1)";
	private static String QUERY_SUIT_CHENGDU_DISTRICT_SQL="select region_id as id,region_name as name ,geo_id,is_main_area from ecs_region  where parent_id =272 and usable =1 order by region_id";
	
	static {
		buxian.clear();
		buxian.put("id","0");
		buxian.put("name","不限");
		buxian.put("order","0");
	}
	
	//风格列表
	private static List<Map<String,String>> suitStyleList = new LinkedList<Map<String,String>>();//属性

	//价格区间列表
	private static List<Map<String,String>> suitPriceIntlList = new LinkedList<Map<String,String>>();
	
	//户型列表
	private static List<Map<String,String>> suitTypeList = new LinkedList<Map<String,String>>();
	
	//空间列表
	private static List<Map<String,String>> suitSpaceList = new LinkedList<Map<String,String>>();
	
	//成都推荐的套装楼盘列表
	private static List<String> suitChengDuFloorList = new LinkedList<String>();
	
	//成都区域列表
	private static List<Map<String,String>> suitChengDuDistrictList= new LinkedList<Map<String,String>>();
	
	//风格是否展示
	private static boolean suitStyleShow=true;
	
	//户型是否展示
	private static boolean suitTypeShow=false;
	
	//价格区间是否显示
	private static boolean suitPriceIntlShow=false;
	
	//空间是否显示
	private static boolean suitSpaceShow=true;
	
	public static List<Map<String, String>> getSuitStyleList() {
		return suitStyleList;
	}

	public static void setSuitStyleList(List<Map<String, String>> suitStyleList) {
		InitQwgSolrDao.suitStyleList = suitStyleList;
	}

	public static List<Map<String, String>> getSuitPriceIntlList() {
		return suitPriceIntlList;
	}

	public static void setSuitPriceIntlList(
			List<Map<String, String>> suitPriceIntlList) {
		InitQwgSolrDao.suitPriceIntlList = suitPriceIntlList;
	}

	public static List<Map<String, String>> getSuitTypeList() {
		return suitTypeList;
	}

	public static void setSuitTypeList(List<Map<String, String>> suitTypeList) {
		InitQwgSolrDao.suitTypeList = suitTypeList;
	}

	public static boolean isSuitStyleShow() {
		return suitStyleShow;
	}

	public static void setSuitStyleShow(boolean suitStyleShow) {
		InitQwgSolrDao.suitStyleShow = suitStyleShow;
	}

	public static boolean isSuitTypeShow() {
		return suitTypeShow;
	}

	public static void setSuitTypeShow(boolean suitTypeShow) {
		InitQwgSolrDao.suitTypeShow = suitTypeShow;
	}

	public static boolean isSuitPriceIntlShow() {
		return suitPriceIntlShow;
	}

	public static void setSuitPriceIntlShow(boolean suitPriceIntlShow) {
		InitQwgSolrDao.suitPriceIntlShow = suitPriceIntlShow;
	}
	
	public static boolean isSuitSpaceShow() {
		return suitSpaceShow;
	}

	public static String [] getQueryParamNames()
	{
		return queryParamNames;
	}

	public static List<Map<String, String>> getSuitSpaceList() {
		return suitSpaceList;
	}

	public static void setSuitSpaceList(List<Map<String, String>> suitSpaceList) {
		InitQwgSolrDao.suitSpaceList = suitSpaceList;
	}

	public static List<String> getSuitChengDuFloorList() {
		return suitChengDuFloorList;
	}

	public static void setSuitChengDuFloorList(List<String> suitChengDuFloorList) {
		InitQwgSolrDao.suitChengDuFloorList = suitChengDuFloorList;
	}

	public static List<Map<String, String>> getSuitChengDuDistrictList() {
		return suitChengDuDistrictList;
	}

	public static void setSuitChengDuDistrictList(
			List<Map<String, String>> suitChengDuDistrictList) {
		InitQwgSolrDao.suitChengDuDistrictList = suitChengDuDistrictList;
	}

	/**
	 * @interface
	 * @return
	 */
	public static boolean initQwgSolrData() {
		/** 初始化连接 */
		initConnection();
		initShowStatus();
		initSelectParamsList();
		/** 关闭流 */
		close();
		needFreshDistrictFloorFlag=true;
		return true;
	}
	
	public static void initShowStatus()
	{
		initSuitStyleShow();
		initSuitPriceIntlShow();
		initSuitTypeShow();
		initSuitSpaceShow();
	}
	
	public static void initSelectParamsList()
	{
		if(suitStyleShow)
		{
		    initSuitStyleParams();
		}
		if(suitPriceIntlShow)
		{
		    initSuitPriceIntlParams();
		}
		if(suitTypeShow)
		{
		    initSuitTypeParams();
		}
		if(suitSpaceShow)
		{
		    initSuitSpaceParams();
		}
		initSuitChengDuFloorParams();
		initSuitChengDuDistrictParams();
		
	}
	
	private static void initSuitStyleShow()
	{
		String sql = QUERY_SHOW_SUIT_STYLE_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			if(rs.next())
			{
				String desc =rs.getString("desc");
				if("1".equalsIgnoreCase(desc))
				{
					suitStyleShow=true;
				}else
				{
					suitStyleShow=false;
				}
				logger.error("initSuitStyleShow suitStyleShow="+suitStyleShow+",desc="+desc);
			}else
			{
				suitStyleShow=false;
				logger.error("initSuitStyleShow suitStyleShow="+suitStyleShow+",结果集为null");
			}
			
		} catch (SQLException e) {
			logger.error("initSuitStyleShow error,",e);
			e.printStackTrace();
		}
	}
	
	private static void initSuitPriceIntlShow()
	{
		String sql = QUERY_SHOW_SUIT_PRICE_INTL_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			if(rs.next())
			{
				String desc =rs.getString("desc");
				if("1".equalsIgnoreCase(desc))
				{
					suitPriceIntlShow=true;
				}else
				{
					suitPriceIntlShow=false;
				}
				logger.error("initSuitPriceIntlShow suitPriceIntlShow="+suitPriceIntlShow+",desc="+desc);
			}else
			{
				suitPriceIntlShow=false;
				logger.error("initSuitPriceIntlShow suitPriceIntlShow="+suitPriceIntlShow+",结果集为null");
			}
			
		} catch (SQLException e) {
			logger.error("initSuitPriceIntlShow error,",e);
			e.printStackTrace();
		}
	}
	
	private static void initSuitTypeShow()
	{
		String sql = QUERY_SHOW_SUIT_TYPE_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			if(rs.next())
			{
				String desc =rs.getString("desc");
				if("1".equalsIgnoreCase(desc))
				{
					suitTypeShow=true;
				}else
				{
					suitTypeShow=false;
				}
				logger.error("initSuitTypeShow suitTypeShow="+suitTypeShow+",desc="+desc);
			}else
			{
				suitTypeShow=false;
				logger.error("initSuitTypeShow suitTypeShow="+suitTypeShow+",结果集为null");
			}
			
		} catch (SQLException e) {
			logger.error("initSuitTypeShow error,",e);
			e.printStackTrace();
		}
	}
	
	private static void initSuitSpaceShow()
	{
		String sql = QUERY_SHOW_SUIT_SPACE_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			if(rs.next())
			{
				String desc =rs.getString("desc");
				if("1".equalsIgnoreCase(desc))
				{
					suitSpaceShow=true;
				}else
				{
					suitSpaceShow=false;
				}
				logger.error("initSuitSpaceShow suitSpaceShow="+suitSpaceShow+",desc="+desc);
			}else
			{
				suitSpaceShow=false;
				logger.error("initSuitSpaceShow suitSpaceShow="+suitSpaceShow+",结果集为null");
			}
			
		} catch (SQLException e) {
			logger.error("initSuitSpaceShow error,",e);
			e.printStackTrace();
		}
	}
	
	
		
	private static void initSuitStyleParams() {
		String sql = QUERY_SUIT_STYLE_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			
			//suitStyleList.clear();
			List<Map<String,String>> styleList = new LinkedList<Map<String,String>>();
			
			while(rs.next()){
				Map<String,String> map = new HashMap<String,String>(3);
				map.put("id",  String.valueOf(rs.getInt("id")));
				map.put("name", String.valueOf(rs.getString("name")));
				map.put("order",String.valueOf(rs.getInt("order")));
				styleList.add(map);
			}
			styleList.add(0, new HashMap<String,String>(buxian));
			suitStyleList= styleList;
			logger.error("initSuitStyleParams suitStyleList.size="+suitStyleList.size());
			
		} catch (SQLException e) {
			logger.error("initSuitStyleParams error,",e);
			e.printStackTrace();
		}
	}
	
	private static void initSuitPriceIntlParams() {
		String sql =QUERY_SUIT_PRICE_INTL_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			
			//suitPriceIntlList.clear();
			List<Map<String,String>> priceList = new LinkedList<Map<String,String>>();
			
			while(rs.next()){
				Map<String,String> map = new HashMap<String,String>(3);
				map.put("id",  String.valueOf(rs.getInt("id")));
				map.put("name", String.valueOf(rs.getString("name")));
				map.put("order",String.valueOf(rs.getInt("order")));
				priceList.add(map);
			}
			priceList.add(0, new HashMap<String,String>(buxian));
			suitPriceIntlList= priceList;
			logger.error("initSuitPriceIntlParams suitPriceIntlList.size="+suitPriceIntlList.size());
		} catch (SQLException e) {
			logger.error("initSuitPriceIntlParams error,",e);
			e.printStackTrace();
		}
	}
	
	private static void initSuitTypeParams() {
		String sql = QUERY_SUIT_TYPE_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			
			//suitTypeList.clear();
			List<Map<String,String>> typeList = new LinkedList<Map<String,String>>();
			while(rs.next()){
				Map<String,String> map = new HashMap<String,String>(3);
				map.put("id",  String.valueOf(rs.getInt("id")));
				map.put("name", String.valueOf(rs.getString("name")));
				map.put("order",String.valueOf(rs.getInt("order")));
				typeList.add(map);
			}
			typeList.add(0, new HashMap<String,String>(buxian));
			suitTypeList=typeList;
			logger.error("initSuitTypeParams suitTypeList.size="+suitTypeList.size());
		} catch (SQLException e) {
			logger.error("initSuitTypeParams error,",e);
			e.printStackTrace();
		}
	}
	
	private static void initSuitSpaceParams()
	{
		String sql = QUERY_SUIT_SPACE_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			
			List<Map<String,String>> spaceList = new LinkedList<Map<String,String>>();
			while(rs.next()){
				Map<String,String> map = new HashMap<String,String>(3);
				map.put("id",  String.valueOf(rs.getInt("id")));
				map.put("name", String.valueOf(rs.getString("name")));
				map.put("order",String.valueOf(rs.getInt("order")));
				spaceList.add(map);
			}
			spaceList.add(0, new HashMap<String,String>(buxian));
			suitSpaceList=spaceList;
			logger.error("initSuitSpaceParams suitSpaceList.size="+suitSpaceList.size());
		} catch (SQLException e) {
			logger.error("initSuitSpaceParams error,",e);
			e.printStackTrace();
		}
	}
	
	public static void initSuitChengDuFloorParams()
	{
		String sql = QUERY_SUIT_CHENGDU_FLOOR_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			
			List<String> floorList = new LinkedList<String>();
			floorList.add("不限");
			while(rs.next()){
				String desc =rs.getString("desc");
                if(!StringUtils.isBlank(desc))
                {
                	String ps [] = desc.split(",");
                	for(String p:ps)
                	{
                		if(!StringUtils.isBlank(p))
                		{
                			floorList.add(p);
                		}
                	}
                }
			}
			suitChengDuFloorList=floorList;
			logger.error("initSuitChengDuFloorParams suitChengDuFloorList.size="+suitChengDuFloorList.size());
		} catch (SQLException e) {
			logger.error("initSuitChengDuFloorParams error,",e);
			e.printStackTrace();
		}
	}
	
	public static void initSuitChengDuDistrictParams()
	{
		String sql = QUERY_SUIT_CHENGDU_DISTRICT_SQL;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			
			List<Map<String,String>> districtList = new LinkedList<Map<String,String>>();
			while(rs.next()){
				Map<String,String> map = new HashMap<String,String>(3);
				String id =  String.valueOf(rs.getInt("id"));
				map.put("id", id);
				map.put("name", String.valueOf(rs.getString("name")));
				map.put("order",id);
				districtList.add(map);
			}
			districtList.add(0, new HashMap<String,String>(buxian));
			suitChengDuDistrictList=districtList;
			logger.error("initSuitChengDuDistrictParams suitChengDuDistrictList.size="+suitChengDuDistrictList.size());
		} catch (SQLException e) {
			logger.error("initSuitChengDuDistrictParams error,",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @interface 维度信息
	 */
	public static Map<String,List<Map<String,String>>> getSelectParams()
	{
		Map<String,List<Map<String,String>>> map= new HashMap<String,List<Map<String,String>>>();
		map.put("style", suitStyleList);
		map.put("priceIntl", suitPriceIntlList);
		map.put("type", suitTypeList);
		return map;
	}
	
	/**
	 * 根据order 拿style记录
	 */
	public static Map<String,String> getStyleRecordByOrder(String order)
	{
		for(Map<String,String> map:suitStyleList)
		{
			if(order.equalsIgnoreCase(map.get("order")))
			{
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 根据order 拿type记录
	 */
	public static Map<String,String> getTypeRecordByOrder(String order)
	{
		for(Map<String,String> map:suitTypeList)
		{
			if(order.equalsIgnoreCase(map.get("order")))
			{
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 根据order 拿PriceIntl记录
	 */
	public static Map<String,String> getPriceIntlRecordByOrder(String order)
	{
		for(Map<String,String> map:suitPriceIntlList)
		{
			if(order.equalsIgnoreCase(map.get("order")))
			{
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 根据order 拿空间列表记录
	 */
	public static Map<String,String> getSpaceRecordByOrder(String order)
	{
		for(Map<String,String> map:suitSpaceList)
		{
			if(order.equalsIgnoreCase(map.get("order")))
			{
				return map;
			}
		}
		return null;
	}
	
	public static int getSpecCity() {
		return specCity;
	}

	public static String[] getFacetParamNames() {
		return facetParamNames;
	}

	public static String[] getFacetFieldsNames()
	{
		return facetFieldsNames;
	}
	
	public static String [] getFacetFieldsAreaNames()
	{
		return facetFieldsAreaNames;
	}
	
	public static boolean isNeedFreshDistrictFloorFlag() {
		return needFreshDistrictFloorFlag;
	}

	public static void setNeedFreshDistrictFloorFlag(
			boolean needFreshDistrictFloorFlag) {
		InitQwgSolrDao.needFreshDistrictFloorFlag = needFreshDistrictFloorFlag;
	}

	/**
	 * 根据order 拿区域列表记录
	 */
	public static Map<String,String> getDistrictByOrder(String order)
	{
		for(Map<String,String> map:suitChengDuDistrictList)
		{
			if(order.equalsIgnoreCase(map.get("order")))
			{
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 根据下表获取单选项
	 */
	public static  String getRadioByIndex(int index)
	{
		if(radioQueryParamNames.length>index)
		{
			return radioQueryParamNames[index];
		}
		return null;
	}
	
	public static String [] getRadioParamNames()
	{
		return radioQueryParamNames;
	}
	
	public static void main(String args []) 
	{
	    
	    Pattern urlParamerPattern = Pattern.compile("q-(\\d+?)-(\\d+?)-(\\d+?)-([^-]*?)-(\\d+?)");
	    
	    String a ="q-1-2-3-dsfdsf557dfdsf5754-2";
	    
	    Map<String,String> map = new HashMap<String,String>();
		Matcher m = urlParamerPattern.matcher(a);
		
		while(m.find())
		{
			System.out.println(m.group()+":"+m.group(1)+":"+m.group(2)+":"+m.group(3)+":"+m.group(4)+":"+m.group(5));
			map.put(m.group(1),m.group());
		}
		
	}
	
}
