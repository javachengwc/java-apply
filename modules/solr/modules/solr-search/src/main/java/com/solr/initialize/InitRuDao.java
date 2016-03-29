package com.solr.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solr.model.UsualTitleInfo;

public class InitRuDao extends InitDao{
	
	private	final static Logger logger = LoggerFactory.getLogger(InitRuDao.class);
	
	private static Map<String,String> pinyinKeywords = new HashMap<String,String>();//关键字连表
	private static Map<String,Map<String,String>> catSeoInfo = new HashMap<String,Map<String, String>>();//SEO信息
	private static Pattern catSeoPtn = Pattern.compile("(\\d+)$");
	private static Map<String,String> searchSeoInfo = new HashMap<String,String>();
	private static String searchMark ="search";
	//占位符
	private static String searchReplaChar ="${search}";
	private static Map<String, UsualTitleInfo> usualTitleMap = new HashMap<String, UsualTitleInfo>();
	private static List<UsualTitleInfo> catList = new LinkedList<UsualTitleInfo>();  //分类信息
	private static UsualTitleInfo topCat=new UsualTitleInfo();//总分类
	
	//沙发分类id
	public static Integer sfCat=770;
	//查询沙发分类id的语句
	private static String QUERY_CAT_STR="select * from ecs_cus_ad where cat_id =(select id from ecs_ad_type where name = 'ru-sofa' )";
	//查询seo信息
	private static String QUERY_SEO_STR="select * from ecs_seoinfo cc where cc.remark REGEXP '[0-9]' or cc.remark='search' ";
	
	public static final void initAllRuData(){
		
		doProcedure();
		initRuConnection();
		initPinyinKeywords();
		initSaCat();
		initUsualTitle();
		initSeoInfo();
		close();
	}
	
	/**重导数据前先执行存储过程*/
	public static final void doProcedure(){
		/** 初始化连接 */
		initRuConnection();
		CallableStatement cst;
		try {
			cst = con.prepareCall("{call channel_ru_pro()} ");
			cst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			/** 关闭流 */
			close();
		}
	}
	
	/**
	 * 获取沙发分类
	 */
	private static void initSaCat()
	{
		String sql = QUERY_CAT_STR;
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			if(rs.next())
			{
				String desc =rs.getString("desc");
				sfCat=Integer.parseInt(desc);
				logger.error("initSaCat desc="+desc);
			}else
			{
				logger.error("initSaCat 结果集为null");
			}
			
		} catch (SQLException e) {
			logger.error("initSaCat error,",e);
		}
	}
	
	/**
	 * 获取类别信息
	 */
	private static void initUsualTitle(){
		String sql = "select cat_name,keywords,cat_desc,style,grade,filter_attr,parent_id,cat_id from ecs_category where lang='ru' and is_show=1 and (parent_id=? or cat_id=?)";
		pst = getPst(con, sql);
		try {
			
			pst.setInt(1,sfCat );
			pst.setInt(2,sfCat );
			rs = getRs(pst);
			
			Map<String, UsualTitleInfo> tmpUsualTitleMap = new HashMap<String,UsualTitleInfo>();
			List<UsualTitleInfo> tmpCatList = new LinkedList<UsualTitleInfo>();
			while(rs.next()){
				UsualTitleInfo uti = new UsualTitleInfo();
				uti.setKeywords(rs.getString("keywords"));
				String catName = rs.getString("cat_name");
				if(!StringUtils.isBlank(catName) && catName.indexOf("|")>0)
				{
					catName = catName.split("\\|")[0];
				}
				uti.setCatName(catName);
				uti.setDescription(rs.getString("cat_desc"));
				uti.setFilter_attr(rs.getString("filter_attr"));
				uti.setGrade(rs.getString("grade"));
				uti.setParent_id(rs.getString("parent_id"));
				uti.setStyle(rs.getString("style"));
				String catId =rs.getString("cat_id");
				uti.setCat_id(catId);
				tmpUsualTitleMap.put(catId, uti);
				if(!StringUtils.isBlank(catId) && Integer.parseInt(catId)==sfCat)
				{
					topCat=uti;
				}else
				{
				    tmpCatList.add(uti);
				}
			}
			usualTitleMap=tmpUsualTitleMap;
			catList = tmpCatList;
			if(topCat==null)
			{
				topCat =new UsualTitleInfo();
				topCat.setCatName(StringUtils.capitalize("Диваны"));
				topCat.setCat_id(String.valueOf(sfCat));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取配置文件内容
	 * @param path
	 */
	public static final Map<String,String> getPropertyContent(String path){
		Properties pro = new Properties();
		InputStream is = null;
		Map<String,String> tm = new HashMap<String, String>();
		try {
//			is = new FileInputStream(path);
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			pro.load(is);
			for(Object key : pro.keySet()){
				tm.put(key.toString(), pro.getProperty(key.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tm;
	}
	
	/**
	 * 设置所有的关键字链表
	 */
	private static void initPinyinKeywords(){
		String sql = "select pinyin,keyword from ecs_searchkeywords where keyword is not null and keyword !='' and lang='ru'";
		pst = getPst(con, sql);
		rs = getRs(pst);
		try {
			Map<String,String> tmpPinyinKeywords =new HashMap<String,String>();
			while(rs.next()){
				String pinyin = rs.getString("pinyin");
				String keywords = rs.getString("keyword");
				if(keywords!=null&&!"".equals(keywords)&&pinyin!=null&&!"".equals(pinyin)){
					tmpPinyinKeywords.put(pinyin, keywords);
				}
			}
			pinyinKeywords =tmpPinyinKeywords;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**根据缩写找关键词**/
	public static final String getKeywordsByAcronym(String pinyin){
		return pinyinKeywords.get(pinyin);
	}
	
	
	
	public static final UsualTitleInfo getUsualTitleByCatId(String cat_id){
		return usualTitleMap.get(cat_id);
	}
	
	public static final List<UsualTitleInfo> getCatList()
	{
		return catList;
	}
	
	public static final UsualTitleInfo getTopCat()
	{
		return topCat;
	}
	
	/**
	 * 初始化网站三元信息
	 */
	public static void initSeoInfo(){
		pst = getPst(con, QUERY_SEO_STR);
		rs = getRs(pst);
		try {
			Map<String,Map<String,String>> seoTmpInfo = new HashMap<String,Map<String,String>>();
			while(rs.next()){
			    Map<String,String> m = new HashMap<String,String>();
			    m.put("title", rs.getString("title"));
			    m.put("keywords", rs.getString("keywords"));
			    m.put("description", rs.getString("description"));
			    String remark =rs.getString("remark");
			    if(!StringUtils.isBlank(remark))
			    {
			    	Matcher mth = catSeoPtn.matcher(remark);
			    	if(mth.find())
			    	{
			    		String catId = mth.group(1);
			    		seoTmpInfo.put(catId, m);
			    	}
			    	if(remark.startsWith(searchMark))
			    	{
			    		searchSeoInfo = m;
			    	}
			    }
			}
			catSeoInfo =seoTmpInfo;
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Map<String, String>> getCatSeoInfo() {
		return catSeoInfo;
	}
	
	public  static Map<String, String>  getSeoInfoByCat(String catId)
	{
		return catSeoInfo.get(catId);
	}

	public static Map<String, String> getSearchSeoInfo() {
		return searchSeoInfo;
	}
    
	public static String getSearchReplaChar()
	{
	    return searchReplaChar;
	}
	
	
	
}