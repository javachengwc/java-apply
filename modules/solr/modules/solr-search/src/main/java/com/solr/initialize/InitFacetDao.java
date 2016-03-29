package com.solr.initialize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 设置筛选条件排序规则
 */
public class InitFacetDao extends InitDao {
	
	private static HashMap<String,ArrayList<Integer>> sortArray = new HashMap<String, ArrayList<Integer>>();
	private static HashMap<String,String> propertyIdName = new HashMap<String, String>();
	private static HashMap<Integer,String> paramIdName = new HashMap<Integer, String>();
	private static HashMap<String,String> propertyDbNameName = new HashMap<String, String>();
	private static HashMap<String,String> propertyDbNameHtmlName = new HashMap<String, String>();
	private static HashMap<String,String> propertyHtmlNameDbName = new HashMap<String, String>();
	private static HashMap<String,String> brandIdName = new HashMap<String, String>();
	//产品分类的排序字段,竖排序
	private static HashMap<String,Integer> catSortMap = new HashMap<String, Integer>();
	//产品每一个分类的自项排序,横排排序
	private static HashMap<String,Integer> catArrtSotMap = new HashMap<String, Integer>();
	//更具cat的分类字段获取catId
	private static HashMap<String,String> catDbnameIdMap = new HashMap<String, String>();
	
	//更具cat的分类字段获取catId
	private static HashMap<String,String> attrIdRalationId = new HashMap<String, String>();
	
	private static HashMap<String, Integer> listAttr = new HashMap<String, Integer>();
	private static HashMap<String, Integer> navAttr = new HashMap<String, Integer>();
	
	private static Set<String> brandSet=new HashSet<String>();
	
	private static StringBuffer styleAndMaterialKeyword=new StringBuffer();
	private static StringBuffer buildingMaterialKeyword=new StringBuffer();

	/**
	 * 初始化 筛选条件排序规则相关数据
	 */
	public static void initSolrRelationParams(){
		fetchStaticParams();//静态属性
		fetchSortNum();//按照属性的排序值顺序获取属性
		fetchProperty();
		fetchPropertyVal();
		fetchBrand();
		initGroupCatSort();
		initGroupCatAttrSort();
		initCatAttrId();
		initRalaitonId();
		initIsShowWeb();
		initIsShowNav();
		//初始化风格和材质下面的搜索关键字
		fetchFurnitureHotIndex();
		fetchBuildingMaterialsHotIndex();
	}
	
    public static boolean isBrandName(String brankName){
    	return brandSet.contains(brankName);
    }
    public static boolean isFurnitureHotIndex(String keyword){
    	return styleAndMaterialKeyword.indexOf(keyword)!=-1?true:false;
    }
    
    public static boolean isBuildingMaterialsHotIndex(String searchKeywords) {
    	return buildingMaterialKeyword.indexOf(searchKeywords)!=-1?true:false;
	}
    
	
	public static int getIsShowWebValue(String key) {
		//防止NullPointerException
		if(listAttr.get(key) == null) {
			return 0;
		} else {
			return listAttr.get(key);
		}
	}
	
	public static int getIsShowNavValue(String key) {
		//防止NullPointerException
		if(navAttr.get(key) == null) {
			return 0;
		} else {
			return navAttr.get(key);
		}
	}
	
	public static int getCatSortValue(String key){
		//如果获取不到值，就返回0为默认的排序字段
		if(catSortMap.get(key)==null){
			return 0;
		}else{
		 return catSortMap.get(key);
		}
	}
	
	public static int getCatAttrSortValue(String key){
		//如果获取不到值，就返回0为默认的排序字段
		if(catArrtSotMap.get(key)==null){
			return 0;
		}else{
		 return catArrtSotMap.get(key);
		}
		
	}
	
	public static String getCatAttrIdByDbName(String key){
		//如果获取不到值，就返回0为默认的排序字段
		return catDbnameIdMap.get(key);
		
	}
	
	public static String getAttrIdByRelationId(String key){
		return attrIdRalationId.get(key);
	}
	
	private static void fetchStaticParams() {
		paramIdName.clear();
		//String sql = "select attr_id,attr_name from mll_def_attr where attr_id in(1,2,3,4,5)";
		String sql = "select attr_id,attr_name from mll_def_attr where type in(0,2)";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				int brand_id = rs.getInt("attr_id");
				String brand_name = rs.getString("attr_name");
				paramIdName.put(brand_id, brand_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initCatSort() {
		catSortMap.clear();
		initConnection();
		String sql = " select CONCAT(cat_id,'-',cat_attr_id) sortKey, cat_sort from ecs_cat_sort_java where attr_val_id=0 ";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				String sortKey = rs.getString("sortKey");
				int cat_sort = rs.getInt("cat_sort");
				catSortMap.put(sortKey, cat_sort);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
            close();
        }
	}
	
	private static void initGroupCatSort() {
        catSortMap.clear();
        
        String sql = " select CONCAT(cat_id,'-',cat_attr_id) sortKey, cat_sort from ecs_cat_sort_java where attr_val_id=0 ";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String sortKey = rs.getString("sortKey");
                int cat_sort = rs.getInt("cat_sort");
                catSortMap.put(sortKey, cat_sort);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
	
	private static void initRalaitonId() {
		attrIdRalationId.clear();
		String sql = " select a.attr_db_name,relation_id,id from mll_def_attr_val v " +
				     " inner join mll_def_attr a on v.attr_id=a.attr_id" +
				     " where relation_id is not null and attr_db_name in('material_id','series_id','style_id')";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				String attr_db_name=rs.getString("attr_db_name");
				String relation_id = rs.getString("relation_id");
				String id = rs.getString("id");
				attrIdRalationId.put(attr_db_name+"-"+relation_id,id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//在打开的集成连接环境中调用
	private static void initGroupCatAttrSort() {
        catArrtSotMap.clear();
        //initConnection();
        String sql = " select  CONCAT(cat_id,'-',attr_val_id) sortKey,cat_sort from ecs_cat_sort_java where attr_val_id<>0  ";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String sortKey = rs.getString("sortKey");
                int cat_sort = rs.getInt("cat_sort");
                catArrtSotMap.put(sortKey, cat_sort);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	//单独连接调用
	public static void initCatAttrSort() {
	    
	    initCatSort();
	    
		catArrtSotMap.clear();
		initConnection();
		String sql = " select  CONCAT(cat_id,'-',attr_val_id) sortKey,cat_sort from ecs_cat_sort_java where attr_val_id<>0  ";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				String sortKey = rs.getString("sortKey");
				int cat_sort = rs.getInt("cat_sort");
				catArrtSotMap.put(sortKey, cat_sort);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            close();
        }
	}
	
	private static void initCatAttrId() {
		catDbnameIdMap.clear();
		String sql = " select attr_id id,attr_db_name name from mll_def_attr ";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				String id = rs.getString("id");
				String name = rs.getString("name");
				catDbnameIdMap.put(name, id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	private static void fetchBrand() {
		String sql = "select brand_id,brand_name from ecs_brand";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			brandIdName.clear();
			brandSet.clear();
			while(rs.next()){
				String brand_id = rs.getString("brand_id");
				String brand_name = rs.getString("brand_name");
				brandSet.add(brand_name);
				brandIdName.put(brand_id, brand_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void fetchFurnitureHotIndex() {
		String sql = " select searchkeywords from ecs_category where parent_id in(select cat_id from ecs_category where cat_name " +
				     " in('卧室','客厅','餐厅','书房','儿童房','户外家具','定制家具','按风格分类','按材质分类'))" +
				     " or cat_name in('卧室','客厅','餐厅','书房','儿童房','户外家具','定制家具','按风格分类','按材质分类')";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			styleAndMaterialKeyword.delete(0,styleAndMaterialKeyword.length()); 
			while(rs.next()){
				styleAndMaterialKeyword.append(rs.getString("searchkeywords"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void fetchBuildingMaterialsHotIndex() {
		String sql = " select searchkeywords from ecs_category where parent_id in(" +
				     " select cat_id from ecs_category where cat_name in('灯饰照明','卫浴用品','墙地面材料','五金工具','五金电器','厨房用品'))" +
				     " or cat_name in('灯饰照明','卫浴用品','墙地面材料','五金工具','五金电器','厨房用品') ";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			buildingMaterialKeyword=new StringBuffer().delete(0,buildingMaterialKeyword.length()); 
			while(rs.next()){
				buildingMaterialKeyword.append(rs.getString("searchkeywords"));
				//System.out.println(buildingMaterialKeyword.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void fetchPropertyVal(){
		String sql = "select * from mll_def_attr_val";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			propertyIdName.clear();
			while(rs.next()){
				String id = rs.getString("id");
				String attr_val_name = rs.getString("attr_val_name");
				propertyIdName.put(id, attr_val_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static void fetchProperty(){
		String sql = "select attr_db_name,attr_name,html_param_name from mll_def_attr where type=1";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			propertyDbNameName.clear();
			propertyDbNameHtmlName.clear();
			propertyHtmlNameDbName.clear();
			while(rs.next()){
				String attr_db_name = rs.getString("attr_db_name");
				String attr_name = rs.getString("attr_name");
				String html_param_name = rs.getString("html_param_name");
				propertyDbNameName.put(attr_db_name, attr_name);
				propertyDbNameHtmlName.put(attr_db_name, html_param_name);
				propertyHtmlNameDbName.put(html_param_name, attr_db_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void initIsShowWeb() {
		String sql = "select cat_id, cat_attr_id, attr_val_id, attr_db_name " +
				"from " +
				"ecs_cat_sort_java inner join mll_def_attr " +
				"on " +
				"cat_attr_id = attr_id " +
				"where is_show_in_list = 1";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			listAttr.clear();
			while (rs.next()) {
				String attr_name_id  = rs.getString("cat_id") + "_" + rs.getString("attr_db_name") + "_" + rs.getString("attr_val_id");
				if (attr_name_id != null) {
					//由于rs中is_show_in_list值始终为1，则直接将1放入map中，作为后续判断的一个条件
					listAttr.put(attr_name_id, 1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void initIsShowNav() {
		
		String sql = "select cat_id, cat_attr_id, attr_db_name " +
		"from " +
		"ecs_cat_sort_java inner join mll_def_attr " +
		"on " +
		"cat_attr_id = attr_id " +
		"where attr_val_id = 0 and is_show_in_nav = 1";		
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			navAttr.clear();
			while (rs.next()) {
				String attr_name_id  = rs.getString("cat_id") + "_" + rs.getString("attr_db_name");
				if (attr_name_id != null) {
					//由于rs中is_show_in_nav值始终为1，则直接将1放入map中，作为后续判断的一个条件
					navAttr.put(attr_name_id, 1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void fetchSortNum(){
		String sql = "select mcav.cat_id,mcav.cat_attr_id,mda.attr_name,sj.cat_sort from mll_cat_attr_value mcav " +
				" join mll_def_attr mda on mcav.cat_attr_id=mda.attr_id " +
				" left join ecs_cat_sort_java sj on sj.cat_id=mcav.cat_id and sj.cat_attr_id=mcav.cat_attr_id " +
				" where mcav.cat_id not in(3,249) and (sj.cat_sort > 0 or sj.cat_sort is null)" +
				" group by mcav.cat_id,mcav.cat_attr_id" +
				" order by mcav.cat_id,sj.cat_sort desc";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			ArrayList<Integer> sortIds = null;
			sortArray.clear();
			while(rs.next()){
				String cat_id = rs.getString("cat_id");
				int cat_attr_id = rs.getInt("cat_attr_id");
				sortIds = sortArray.get(cat_id);
				if(null==sortIds){
					sortIds = new ArrayList<Integer>();
					sortArray.put(cat_id, sortIds);
				}
				sortIds.add(cat_attr_id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	
	public static final ArrayList<Integer> getFacetIdListByCatId(String cat_id){
		if(null!=sortArray.get(cat_id)){
			return sortArray.get(cat_id);
		}
		return null;
	}
	
	public static final String getPropertyNameById(String id){
		return propertyIdName.get(id);
	}
	
	public static final String getPropertyNameByDbName(String dbname){
		return propertyDbNameName.get(dbname);
	}
	
	public static final String getStaticParamsNameById(int attr_id){
		return paramIdName.get(attr_id);
	}
	
	public static final String getPropertyHtmlNameByDbName(String dbName){
		return propertyDbNameHtmlName.get(dbName);
	}
	
	public static final String getPropertDbNameByHtmlName(String htmlName){
		return propertyHtmlNameDbName.get(htmlName);
	}
	
	public static final List<Map<String,String>> getRelationSearchByBrandId(String brand_id){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		int i = 0;
		for(String key:brandIdName.keySet()){
			if(i<5 && !key.equals(brand_id)){
				map = new HashMap<String, String>();
				map.put("name", brandIdName.get(key));
				map.put("url", "/brand-"+key+"/");
				list.add(map);
				i++;
			}
		}
		return list;
	}
	/**根据属性值,获取属性值的排序值**/
	public static final int getFacetValueSortByValueId(String name) {
		// TODO ...
		return 1;
	}

	
}