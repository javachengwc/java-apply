package com.solr.initialize;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class InitOtherDao extends InitDao {

	private static Map<String,String> advertisementTagMap = new HashMap<String, String>();
	private static List<String> brandList = new ArrayList<String>();
	private static Map<String, String> keywordsInMllDictionaryMap = new HashMap<String, String>();

	/**
	 * 初始化广告位信息
	 */
	public static void initAdvertisementTag(){
		String sql = " select a.desc  from ecs_cus_ad a join ecs_ad_type b on a.cat_id=b.id where b.name='列表页自定义标签' and a.is_show=1  " +
				     " order by a.ad_order ";
		try {
			advertisementTagMap.clear();
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				String desc_content = rs.getString("desc");
			    String[] descArray  =desc_content.split("\\|");
				if(descArray!=null&&descArray.length==2){
					String goods_sn="";
					String adTag="";
					if(StringUtils.isNotEmpty(descArray[0]) && StringUtils.isNotEmpty(descArray[1])){
						goods_sn = descArray[0].trim();
						adTag= descArray[1].trim();
					}
					advertisementTagMap.put(goods_sn, adTag);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getAdvertisementTag(String goodsSN){
		return advertisementTagMap.get(goodsSN);
	}

	public static void initAllBrand() {
		brandList.clear();
		String sql = "SELECT brand_name FROM ecs_brand WHERE brand_name != ''";
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			String brandName = null;
			while (rs.next()) {
				brandName = rs.getString("brand_name");
				if (brandName.matches("[0-9a-zA-Z#-]+")) {
					brandList.add(brandName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void initAllkeywordsInMllDictionary() {
		keywordsInMllDictionaryMap.clear();
		String sql = "SELECT keywords,cat_id FROM hot_keywords_stat WHERE is_in_mll_dictionary = 1 AND keywords != '' AND cat_id != 0";
		try {
			pst = con.prepareStatement(sql);
			rs  = pst.executeQuery();
			while (rs.next()) {
				keywordsInMllDictionaryMap.put(rs.getString("keywords"), rs.getString("cat_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getAllBrand() {
		return brandList;
	}

	public static String getKeywordsCatId(String keywords) {
		return keywordsInMllDictionaryMap.get(keywords);
	}
}