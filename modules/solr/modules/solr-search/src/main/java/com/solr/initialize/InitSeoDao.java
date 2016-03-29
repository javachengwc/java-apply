package com.solr.initialize;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.solr.model.HotKeywordsSeo;
import com.solr.model.UsualTitleInfo;

/**
 * 增加页面seo设置数据
 */
public class InitSeoDao extends InitDao{
	private static Map<String,Map<String,String>> seoMap = new HashMap<String, Map<String,String>>();
	private static Map<String,Map<String,String>> brandSeoMap = new HashMap<String, Map<String,String>>();
	private static Map<String, UsualTitleInfo> usualTitleMap = new HashMap<String, UsualTitleInfo>();//title描述的Map
	private static Map<String,HotKeywordsSeo> hotKeywordsSeoMap = new HashMap<String, HotKeywordsSeo>();//热门关键词SEO信息
	private static Map<String,String> publicTitleMap = new HashMap<String,String>();//共用的title描述的Map
	/**
	 * 获取自定义seo信息
	 * @return
	 */
	static boolean initSeoDao(){
		boolean result = false;
		try {
			pst = getPst(con, "SELECT * FROM ecs_goodspage_title WHERE 1 AND type=4");
			rs = getRs(pst);
			seoMap.clear();
			Map<String,Map<String,String>> sMap = new HashMap<String, Map<String,String>>();
			while(rs.next()){
				String cat_id = rs.getString("cat_id");
				String title = rs.getString("order_by");
				String keyword = rs.getString("keyword_str");
				String description = rs.getString("description_str");
				if(StringUtils.isNotBlank(cat_id)&&
						StringUtils.isNotBlank(title)&&
						StringUtils.isNotBlank(keyword)&&
						StringUtils.isNotBlank(description)){
					Map<String,String> catSeoMap = new HashMap<String, String>();
					catSeoMap.put("title", title);
					catSeoMap.put("keyword", keyword);
					catSeoMap.put("description", description);
					sMap.put(cat_id, catSeoMap);
				}
			}
			if(sMap!=null&&sMap.size()>0){
				seoMap.putAll(sMap);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询title,keyword,description信息
	 * Map<String,String>
	 */
	static final void initUsualSeo(){
		pst = getPst(con,"SELECT cat_name , keywords, cat_desc, style, grade, filter_attr, parent_id ,cat_id FROM ecs_category where lang='cn'");
		rs = getRs(pst);
		try {
			usualTitleMap.clear();
			while(rs.next()){
				UsualTitleInfo uti = new UsualTitleInfo();
				uti.setKeywords(rs.getString("keywords"));
				uti.setCatName(rs.getString("cat_name"));
				uti.setDescription(rs.getString("cat_desc"));
				uti.setFilter_attr(rs.getString("filter_attr"));
				uti.setGrade(rs.getString("grade"));
				uti.setParent_id(rs.getString("parent_id"));
				uti.setStyle(rs.getString("style"));
				usualTitleMap.put(rs.getString("cat_id"), uti);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询title,keyword,description信息
	 * Map<String,String>
	 */
	static final void initBrandSeo(){
		pst = getPst(con,"SELECT seo.remark brandId,title,keywords,description FROM ecs_seoinfo seo inner join ecs_ad_type type on seo.type=type.id where type.name='列表页品牌页面SEO信息'");
		rs = getRs(pst);
		try {
			brandSeoMap.clear();
			while(rs.next()){
				String brandId=rs.getString("brandId");
				Map<String,String> brandSeoInfo=new HashMap<String,String>();
				brandSeoInfo.put("title", rs.getString("title"));
				brandSeoInfo.put("keywords", rs.getString("keywords"));
				brandSeoInfo.put("description", rs.getString("description"));
				brandSeoMap.put(brandId, brandSeoInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static  Map<String,String> getBrandSeoMap(String brandId){
		return brandSeoMap.get(brandId);
	}
	
	static final void initPublicSeo() {
		publicTitleMap.clear();
		publicTitleMap.put("title", getStrResult("select value from ecs_shop_config where code = 'shop_title' "));
		publicTitleMap.put("description", getStrResult("select value from ecs_shop_config where code = 'shop_desc' "));
		publicTitleMap.put("keyword", getStrResult("select value from ecs_shop_config where code = 'shop_keywords' "));
	}
	
	/**初始化热门关键词SEO信息*/
	static void initHotSearchKeywordsSeo() {
		String sql = "select pinyin, des, related_keyword, title from ecs_searchkeywords where lang='cn'";
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			hotKeywordsSeoMap.clear();
			while(rs.next()){
				HotKeywordsSeo seo = new HotKeywordsSeo();
				seo.setDes(rs.getString("des"));
				seo.setKeywords(rs.getString("related_keyword"));
				seo.setTitle(rs.getString("title"));
				hotKeywordsSeoMap.put(rs.getString("pinyin"), seo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**获取热门关键词SEO信息*/
	public static final HotKeywordsSeo getHotKeywordsSeoByPinyin(String pinyin) {
		return hotKeywordsSeoMap.get(pinyin);
	}
	
	public static final Map<String, String> getSeoInfoByCatId(String cat_id) {
		return seoMap.get(cat_id);
	}
	
	/**
	 * 返回共用的共用title,description,keyword的Map集合
	 * String
	 */
	public static final String getPublicSeoInfoByName(String title){
		return publicTitleMap.get("title");
	}
	
	/**获取title描述的Map集合*/
	public static final UsualTitleInfo getUsualTitleByCatId(String cat_id) {
		return usualTitleMap.get(cat_id);
	}

	public static Map<String, Map<String, String>> getBrandSeoMap() {
		return brandSeoMap;
	}
}