package com.solr.initialize;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加载用户词典到内存
 */
public class MyDictionary extends InitDao {

	private static Set<String>	fullWords	= new HashSet<String>();
	

	public static void main(String[] args) {
		initConnection();
		//addWordToDictionary();
		close();
		
	}

	/**
	 * 加载用户词典和停用词,数据源可以是数据库或者文件
	 */
	public static void addWordToFile() {
		initConnection();
		// 这里考虑用一张表,专门存储词典
		fullWords.clear();
		Set<String> userWords = new HashSet<String>();
		Set<String> stopWords = new HashSet<String>();// 停用词优先级高于用户词典
		//loadGoodsNameAndSN(userWords, fullWords);
		loadStaticParamName(userWords, fullWords);
		loadBrandName(userWords, fullWords);
		loadManualName(userWords, fullWords);
		loadGoodsActivityName(userWords, stopWords);
		loadPropertyName(userWords);
		loadStopWords(stopWords);
		writeWord2File(userWords);
		close();
		
	}

	

	private static void writeWord2File(Set<String> userWords) {
		BufferedWriter bw = null;
		try {
			//bw = new BufferedWriter(new FileWriter(new File(Thread.currentThread().getContextClassLoader().getResource("/mydict.dic").getPath())));
			String fileName=Thread.currentThread().getContextClassLoader().getResource("/mydict.dic").getPath();
			//String fileName = InitDao.getInitParamByName("mydic");
			File file = new File(fileName);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
			for (String str : userWords) {
				bw.write(str);
				bw.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void loadGoodsActivityName(Set<String> userWords, Set<String> fullWords) {
		List<String> acts = Arrays.asList(new String[] { "直降", "新品", "热卖", "特价", "包邮", "包快递", "包物流 ", "促销", "限购", "返现", "人气", "包安装" });
		userWords.addAll(acts);
		fullWords.addAll(acts);
	}

	private static void loadGoodsNameAndSN(Set<String> userWords, Set<String> fullWords) {
		// //查询数据库,获取产品名称
		String sql = "select goods_name,goods_sn from java_city_channel";
		String regex = "[\u4E00-\u9FA5]+";
		String regex2 = "[a-zA-Z0-9#-]+";
		addUserWordsFromDataBase(sql, regex, regex2, userWords, fullWords);
	}

	private static void loadStaticParamName(Set<String> userWords, Set<String> fullWords) {
		String sql = "select cat_name from ecs_category";
		String regex = "[\u4E00-\u9FA5]+";
		setUserWordsByDataBase(sql, regex, userWords, fullWords);
	}

	private static void loadBrandName(Set<String> userWords, Set<String> fullWords) {
		String sql = "select brand_name from ecs_brand";
		String regex = "[\u4E00-\u9FA5]+";
		setUserWordsByDataBase(sql, regex, userWords, fullWords);
	}

	private static void loadPropertyName(Set<String> userWords) {
		String sql = "select attr_val_name from mll_def_attr_val where attr_id > 5";
		String regex = "[\u4E00-\u9FA5]+";
		setUserWordsByDataBase(sql, regex, userWords);
	}

	private static void loadManualName(Set<String> userWords, Set<String> fullWords) {
		userWords.add("欧式沙发床");
		userWords.add("转角沙发");
		userWords.add("皮艺");
		userWords.add("三件套");
		userWords.add("蒂美悦紫色皮艺转角");
		userWords.add("蒂美悦紫色皮艺转角\\(三人位\\)");
		//这里可以获取经过统计的热门搜索词,如果没有,则添加进去
		String sql = "select attr_val_name from mll_def_attr_val where attr_id > 5";
		String regex = "[\u4E00-\u9FA5]+";
		//setUserWords(sql, regex, userWords);
	}

	private static void loadStopWords(Set<String> stopWords) {
		stopWords.add("家具");
		stopWords.add("具");
		stopWords.add("家");
		stopWords.add("家居");
	}


	private static final void setUserWordsByDataBase(String sql, String regex, Set<String> useWords) {
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String goods_name = rs.getString(1);
				if (StringUtils.isNotBlank(goods_name)) {
					useWords.addAll(deleteSpecialWord(regex, goods_name));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static final void setUserWordsByDataBase(String sql, String regex, Set<String> useWords, Set<String> fullWords) {
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String goods_name = rs.getString(1);
				if (StringUtils.isNotBlank(goods_name)) {
					fullWords.add(goods_name);
					useWords.addAll(deleteSpecialWord(regex, goods_name));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static final void addUserWordsFromDataBase(String sql, String regex, String regex2, Set<String> useWords, Set<String> fullWords) {
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String goods_name = rs.getString(1);
				String goods_sn = rs.getString(2);
				//				if (StringUtil.isNotBlank(goods_name)) {
				//					fullWords.add(goods_name.trim());
				//					useWords.addAll(deleteSpecialWord(regex, goods_name));
				//				}
				if (StringUtils.isNotBlank(goods_sn)) {
					fullWords.add(goods_sn.trim());
					useWords.addAll(deleteSpecialWord(regex2, goods_sn));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删掉特殊符号
	 * 
	 * @param input
	 */
	private static final Set<String> deleteSpecialWord(String regex, String input) {
		Matcher m = Pattern.compile(regex).matcher(input);
		Set<String> useWord = new HashSet<String>();
		while (m.find()) {
			useWord.add(m.group());
		}
		return useWord;
	}

	public static final Set<String> getFullWords() {
		return fullWords;
	}
}
