package com.solr.initialize;

import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


/**
 * 用于对分类相关的初始化
 */
public class InitCatDao extends InitDao {
	
	private static Map<String, Integer> catSortMap = new HashMap<String, Integer>();
	
	public static void initCatRelationParams() {
		fetchCatInfo();
	}
	
	public static int getSortByCat(String key) {
		if (catSortMap.get(key) != null) {
			return catSortMap.get(key);
		} else {
			return -1;
		}
	}
	
	private static void fetchCatInfo() {
		String sql = "select " +
				"cat_id, sort_order " +
				"from ecs_category " +
				"where " +
				"lang = 'cn' and is_show = 1 " +
				"order by " +
				"show_type, sort_order";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			catSortMap.clear();
			String cat_id = "";
			while (rs.next()) {
				cat_id = rs.getString("cat_id");
				if (StringUtils.isNotBlank(cat_id)) {
					catSortMap.put(cat_id, rs.getInt("sort_order"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
