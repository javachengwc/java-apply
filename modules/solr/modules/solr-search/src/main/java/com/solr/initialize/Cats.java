package com.solr.initialize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.solr.util.SolrServerFactory;
import com.solr.util.SysConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;

import com.solr.model.context.MyContext;
import com.solr.model.CatInfo;
import com.solr.model.LeftCatsVo;

/**
 * 获取分类数据
 */
public class Cats {
	
	private static Map<String,ArrayList<LeftCatsVo>> categorySortArray = new LinkedHashMap<String,ArrayList<LeftCatsVo>>();
	private static Map<String,String> catIdName = new HashMap<String, String>();
	private static Map<String,String> catNameId = new HashMap<String, String>();
	private static Map<String,String> catIdParent = new HashMap<String, String>();
	private static Map<String,String> catIdPinyin = new HashMap<String, String>();
	private static Map<String,String> catPinyinId = new HashMap<String, String>();
	private static Map<String,String> catIdShowType = new HashMap<String, String>();
	private static Map<String,Boolean> isParent = new HashMap<String, Boolean>();
	private static Map<String,List<String>> catParentIdSonList = new HashMap<String, List<String>>();
	public static void main(String[] args) {
		initLeftCategoryList();
	}
	
	/**
	 * 获取左边分类列表
	 */
	public static final void initLeftCategoryList(){//需要更新的时候,调用这个入口
		QueryResponse resp = searchSolr();
		NamedList<List<PivotField>> result = resp.getFacetPivot();
		packageCatList(result);
	}

	/**查询solr**/
	private static QueryResponse searchSolr(){
		//通过solr获取源数据,获取一次之后,基本不变,在分类有修改的时候,更新索引,再次查询
		try {
			SolrServer server = SolrServerFactory.getSolrServerInstance(SysConfig.getValue("bottom_cat_url"));
			SolrQuery query = new SolrQuery();
			query.setQuery("show_type:0 OR show_type:1 OR show_type:2");
			query.setRows(0);
			query.setFacet(true);
			query.setParam("facet.pivot", "city_sign,pdname,cdname");
			query.setParam("facet.sort", "city_sign,pdname,cdname");
			query.setFacetLimit(-1);
			QueryResponse resp = server.query(query);
			return resp;
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**组装分类数据**/
	private static void packageCatList(NamedList<List<PivotField>> result){
		for(int i = 0;i<result.size();i++){
			List<PivotField> list1 = result.getVal(i);
			ArrayList<LeftCatsVo> clist = null;
			if(list1.size()>0){//如果返回的数据正常,则清空数据
				isParent.clear();
				catIdName.clear();
				catNameId.clear();
				catIdParent.clear();
				catIdPinyin.clear();
				catPinyinId.clear();
				catIdShowType.clear();
				catParentIdSonList.clear();
				categorySortArray.clear();
			}
			for(int j=0;j<list1.size();j++){//第一层
				PivotField pf1 = list1.get(j);
				String key = (String) pf1.getValue();
				String sk = null;
				String show_type = null;
				if(StringUtils.isNotBlank(key)&&key.contains(",")){
					sk = key.split(",")[0];
					show_type = key.split(",")[1];
				}
				clist = new ArrayList<LeftCatsVo>();
				fetchParentCats(clist,pf1,show_type);
				Collections.sort(clist, new leftCatsVoComparator());
				categorySortArray.put(sk, clist);
			}
		}
	}

	/**
	 * 用于对左上分类比较排序,下同
	 */
	private static class leftCatsVoComparator implements Comparator<LeftCatsVo> {
		
		public int compare(LeftCatsVo o1, LeftCatsVo o2) {
			int o1_sort = InitCatDao.getSortByCat(o1.getParentCat().getCat_id());
			int o2_sort = InitCatDao.getSortByCat(o2.getParentCat().getCat_id());
			return (o1_sort - o2_sort);
		}
	}
	
	private static class catInfoComparator implements Comparator<CatInfo> {

		public int compare(CatInfo o1, CatInfo o2) {
			String id1 = o1.getCat_id();
			String id2 = o2.getCat_id();
			int o1_sort = InitCatDao.getSortByCat(id1);
			int o2_sort = InitCatDao.getSortByCat(id2);
			return (o1_sort - o2_sort);
		}
	}
	
	/**设置父分类信息**/
	private static void fetchParentCats(ArrayList<LeftCatsVo> clist,PivotField pf1, String show_type) {
		LeftCatsVo vo = null;
		List<PivotField> list2 = pf1.getPivot();
		for(int k = 0;k<list2.size();k++){//第二层
			PivotField pf2 = list2.get(k);
			vo = new LeftCatsVo();
			String pk = (String) pf2.getValue();
			CatInfo pinfo = vo.getParentCat();
			String pid = null;
			String pname = null;
			String ppinyin = null;
			if(StringUtils.isNotBlank(pk)){
				pid = pk.split(",")[0];
				pname = pk.split(",")[1];
				ppinyin = pk.split(",")[2];
				pinfo.setCat_id(pid);
				pinfo.setCat_name(pname);
				pinfo.setPinyin(ppinyin);
				pinfo.setShow_type(show_type);
				pinfo.setParent_id(pid);
				catIdName.put(pid, pname);
				catNameId.put(pname, pid);
				catIdPinyin.put(pid, ppinyin);
				catPinyinId.put(ppinyin, pid);
				catIdParent.put(pid, pid);
				catIdShowType.put(pid, show_type);
				isParent.put(pid, true);
			}
			fetchSonCats(pf2,vo,pid,show_type);
			Collections.sort(vo.getSubCatList(), new catInfoComparator());
			clist.add(vo);
		}
	}

	/**设置子类信息**/
	private static void fetchSonCats(PivotField pf2, LeftCatsVo vo,String parent_id, String show_type) {
		List<PivotField> list3 = pf2.getPivot();
		ArrayList<CatInfo> cinfos = vo.getSubCatList();
		List<String> sonList = new ArrayList<String>();
		for(int r = 0;r<list3.size();r++){
			PivotField pf3 = list3.get(r);
			String ck = (String) pf3.getValue();
			CatInfo cinfo = new CatInfo();
			if(StringUtils.isNotBlank(ck)){
				String cid = ck.split(",")[0];
				String cname = ck.split(",")[1];
				String cpinyin = ck.split(",")[2];
				cinfo.setCat_id(cid);
				cinfo.setCat_name(cname);
				cinfo.setPinyin(cpinyin);
				cinfo.setShow_type(show_type);
				cinfo.setParent_id(parent_id);
				catIdName.put(cid, cname);
				catNameId.put(cname, cid);
				catIdPinyin.put(cid, cpinyin);
				catPinyinId.put(cpinyin, cid);
				catIdParent.put(cid, parent_id);
				catIdShowType.put(cid, show_type);
				isParent.put(cid, false);
				sonList.add(cid);
			}
			cinfos.add(cinfo);
		}
		catParentIdSonList.put(parent_id, sonList);
	}

	public static final void setLeftCatList(MyContext context) {
		context.set("categorySortMap", categorySortArray);
		//return categorySortArray;
	}
	
	public static String getCatIdByName(String catname){
		return catNameId.get(catname);
	}
	
	public static final Set<String> getCatIdList(){
		return catIdName.keySet();
	}
	
	public static final String getCatNameById(String cat_id){
		return catIdName.get(cat_id);
	}
	
	public static final String getParentIdById(String cat_id){
		return catIdParent.get(cat_id);
	}
	
	public static final String getPinyinById(String cat_id){
		return catIdPinyin.get(cat_id);
	}
	
	public static final String getIdByPinyin(String pinyin){
		return catPinyinId.get(pinyin);
	}
	
	public static final String getShowTypeById(String cat_id){
		return catIdShowType.get(cat_id);
	}
	
	public static final boolean getIsParentById(String cat_id){
		if(null!=isParent.get(cat_id)){
			return isParent.get(cat_id);
		}else return false;
	}
	
	public static final List<String> getSonIdListByParentId(String parent_id){
		return catParentIdSonList.get(parent_id);
	}
	
	public static final boolean containsName(String catName){
		return catIdName.values().contains(catName);
	}
	
	public static final List<Map<String,String>> getRelationSearchByCatId(String cat_id){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		if(isParent.get(cat_id)){//是父分类,则取父分类相关分类
			for(String id:catParentIdSonList.keySet()){
				if(!id.equals(cat_id) 
						&& getShowTypeById(id).equals(getShowTypeById(cat_id))){
					map = new HashMap<String, String>();
					map.put("name", catIdName.get(id));
					map.put("url", "/category-"+catIdPinyin.get(id)+"/");
					list.add(map);
				}
			}
		}else{//子分类,则取子分类相关分类
			String parent_id = getParentIdById(cat_id);
			for(String id:catParentIdSonList.get(parent_id)){
				if(!id.equals(cat_id)){
					map = new HashMap<String, String>();
					map.put("name", catIdName.get(id));
					map.put("url", "/category-"+catIdPinyin.get(id)+"/");
					list.add(map);
				}
			}
		}
		if(list.size()>5){
			Collections.shuffle(list);//打乱列表
			return list.subList(0, 5);
		}else{
			return list;
		}
	}

	public static final String getIdWithKeyword(String keyword) {
		if (StringUtils.isNotBlank(catNameId.get(keyword))) {
			return catNameId.get(keyword);
		} else {
			for (String key : catIdName.keySet()) {
				if(keyword.contains(catIdName.get(key))) {
					return key;
				}
			}
		}
		return null;
	}
}