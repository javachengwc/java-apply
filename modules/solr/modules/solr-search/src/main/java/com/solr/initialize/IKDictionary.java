package com.solr.initialize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.solr.util.SysConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 加载用户词典到内存
 */
public class IKDictionary extends InitDao {

	private static Set<String>	fullWords	= new HashSet<String>();
	public static IKAnalyzer			analyzer	= null;

	public static void main(String[] args) {

		Configuration cfg = DefaultConfig.getInstance();  //加载词库
		Dictionary.initial(cfg);
		System.out.println(Dictionary.getSingleton().isStopWord("是".toCharArray(),0,1));
		
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
		loadGoodsActivityName(userWords);
		loadPropertyName(userWords);
		loadStopWords(stopWords);
		writeWord2File(userWords);
		close();
		Configuration cfg = DefaultConfig.getInstance();  //加载词库
		//cfg.setUseSmart(true);
		Dictionary.initial(cfg);
		Dictionary.getSingleton().addWords(userWords);// 设置用户词典词库
		Dictionary.getSingleton().disableWords(stopWords);//设置用户停止词
		analyzer = new IKAnalyzer(true);
		//analyzer.setUseSmart(true);
	}

	/**
	 * 加载用户词典和停用词,数据源可以是数据库或者文件
	 */
	public static void loadDictionary() {
		Configuration cfg = DefaultConfig.getInstance();  //加载词库
		Dictionary.initial(cfg);
		//Dictionary.getSingleton().addWords(userWords);// 设置用户词典词库
		//Dictionary.initial(DefaultConfig.getInstance());//初始化词典
		analyzer = new IKAnalyzer(true);
		//		Dictionary.getSingleton().disableWords(stopWords);//设置用户停止词
		//Dictionary.getSingleton().addWords(userWords);// 设置用户词典词库
	}

	private static void writeWord2File(Set<String> userWords) {
		BufferedWriter bw = null;
		try {

			String fileName = SysConfig.getValue("mydic");
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

	private static void loadGoodsActivityName(Set<String> userWords) {
		List<String> acts = Arrays.asList(new String[] { "直降", "新品", "热卖", "特价", "包邮", "包快递", "包物流 ", "促销", 
				"限购", "返现", "人气", "包安装","团购","现货","新品","特价","秒杀" });
		userWords.addAll(acts);
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
	private static void loadStopWords(Set<String> stopWords) {
		stopWords.add("的");
	}

	/** 获取分词结果 */
	public static Set<String> getAnalyseValues(String t) {// 只会在二次搜索的时候使用
		Set<String> values = new LinkedHashSet<String>();
		TokenStream ts = null;
		try {
			ts = analyzer.tokenStream("text", new StringReader(t));
			ts.reset();
			while (ts.incrementToken()) {
				CharTermAttribute termAtt = ts.getAttribute(CharTermAttribute.class);
				values.add(termAtt.toString());
			}
			ts.end();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != ts) {
					ts.close();
				}
				//analyzer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return values;
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
