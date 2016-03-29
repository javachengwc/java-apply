package com.solr.core;

import com.solr.model.Price;
import com.solr.model.ProductWeight;
import com.solr.initialize.IKDictionary;
import com.solr.initialize.InitDao;
import com.solr.util.KeywordsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolrjQuery {
    
	private SolrServer		solrServer;
	//private static String NORMAL = "normal";
	//private static String GOODSSN = "goods_sn";
	private static String	SOLR_SEPARATOR		= " AND ";
	//private String SEARCH_TYPE = "normal";
	private static String	SOLR_SEPARATORVALUE	= " OR ";

	public SolrjQuery() {
	}

	public SolrjQuery(SolrServer solrServer) {
		this.solrServer = solrServer;
		
	}

	public SolrDocumentList query(Map<String, String> propertyMap, Map<String, String> compositorMap, Long startIndex, Long pageSize)
			throws Exception {
 		SolrQuery query = new SolrQuery();
		String keywords = null;
		String flag = "";
		int fieldNumber = 0;
		// 设置搜索字段
		if (null == propertyMap) {
			query.setQuery("*:*");// 查询条件为空的时候,默认查询全部数据
			// throw new Exception("搜索字段不可为空!");
		} else {
			StringBuffer sb = new StringBuffer();
			flag = propertyMap.get("flag");
			propertyMap.remove("flag");
			for (String o : propertyMap.keySet()) {
				String value = propertyMap.get(o);
				if (StringUtils.isNotEmpty(value)) {
					if ("keywords".equals(o)) {
						keywords = value;
						//sb.append(StringUtil.transformSolrMetacharactor(value)).append(SOLR_SEPARATOR);
						if (StringUtils.isNotEmpty(keywords) && "main".equals(flag)) {
							String transformKeyword = KeywordsUtil.transformSolrMetacharactor(keywords);
							Set<String> analyseValuse = IKDictionary.getAnalyseValues(transformKeyword);
							StringBuffer keywordBuffer = new StringBuffer();
							for (String analyserValuse : analyseValuse) {
								keywordBuffer.append(analyserValuse + " AND ");
							}
							sb.append(keywordBuffer.toString());
							fieldNumber = analyseValuse.size();
							query.setParam("defType", "edismax");
							query.setParam(
									"qf",
									" new_goods_name^3 goods_name^1.5 cat_name^3 brand_name^3 style_name^3 material_name^3 goods_keywords^2 " +
									" activity_keywords^1 showtype_keywords^1 parent_name^1 ");
							//获取设置的权重属性
							List<ProductWeight> queryWeightList = InitDao.getProductWeightList();
							if (queryWeightList != null && queryWeightList.size() > 0) {
								StringBuffer bfBuffer = new StringBuffer();
								for (ProductWeight productWeight : queryWeightList) {
									bfBuffer.append(productWeight.getFielfBf() + " ");
								}
								query.setParam("bf", bfBuffer.toString());
							} else {
								//默认的排序字段
								query.setParam("bf",
										" div(total_sold_yes_count,1000)^30 div(click_count,100000)^20 recip(rord(add_time),1,10000,10000)^5 div(effect_price,10000)^10 ");
							}
						} else {
							//sb.append(StringUtil.transformSolrMetacharactor(value)).append(SOLR_SEPARATOR);
							sb.append(value).append(SOLR_SEPARATOR);
						}
					} else if ("queryValue".equals(o)){
						//处理一些需要构造的特殊查询
						sb.append(value).append(SOLR_SEPARATOR);
					} else if ("tl".equals(o)) {
						sb.append(value).append(SOLR_SEPARATOR);
					} else if ("cityQuery".equals(o)) {
						sb.append(value).append(SOLR_SEPARATOR);
					}else {
						if ("rt".equals(o)) {
						    //System.out.println("rt");
							sb.append(value).append(SOLR_SEPARATOR);
							//fieldNumber++;
						} else {
							sb.append(o).append(":").append(value).append(SOLR_SEPARATOR);
							  fieldNumber++;
//							if(!"effect_price".endsWith(o)){
//							   fieldNumber++;
//							}
						}

					}
				}
			}

			if (sb.toString().endsWith(SOLR_SEPARATOR)) {
				sb.delete(sb.toString().length() - SOLR_SEPARATOR.length(), sb.toString().length());
			}
			if (StringUtils.isEmpty(sb.toString())) {
				query.setQuery("*:*");
			} else {
				query.setQuery(sb.toString());
			}
		}
		// 设置排序条件
		if (null != compositorMap) {
			for (Object co : compositorMap.keySet()) {
				if (null != co && StringUtils.isNotEmpty(String.valueOf(co))) {
					if ("asc".equalsIgnoreCase(compositorMap.get(co))) {
						query.addSortField(co.toString(), SolrQuery.ORDER.asc);
					} else {
						query.addSortField(co.toString(), SolrQuery.ORDER.desc);
					}
				}
			}
		}

		if (null != startIndex) {
			query.setStart(Integer.parseInt(String.valueOf(startIndex)));
		}
		if (null != pageSize && 0L != pageSize.longValue()) {
			query.setRows(Integer.parseInt(String.valueOf(pageSize)));
		}
		try {
			if (StringUtils.isNotEmpty(propertyMap.get("queryValue"))) {
				query.setParam("q.op", SOLR_SEPARATORVALUE.trim());
			} else {
				query.setParam("q.op", SOLR_SEPARATOR.trim());
			}
			if (StringUtils.isNotEmpty(keywords) && "main".equals(flag)) {
				query.setParam("mm", String.valueOf(fieldNumber));
			}

			QueryResponse qrsp = solrServer.query(query);
			SolrDocumentList docs = qrsp.getResults();
			query.clear();
			qrsp = null;
			query = null;
			return docs;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public Long querySolrResultCount(SolrDocumentList docs) {
		return docs.getNumFound();
	}

	public List<FacetField> factQuery(Map<String, Object> factParameterMap) throws Exception {
		SolrQuery query = new SolrQuery();
		if (null == factParameterMap) {
			throw new Exception("facet字段不可为空!");
		} else {
			// 设置facet的query字段
			StringBuffer sb = new StringBuffer();
		  
		   if(factParameterMap.get("facet.flag")!=null && factParameterMap.get("facet.keywords")!=null){
			    String flag=(String) factParameterMap.get("facet.flag");
				String keywords=(String) factParameterMap.get("facet.keywords");
				if(StringUtils.isNotEmpty(keywords)&&"main".equals(flag)){
					int fieldNumber=(Integer) factParameterMap.get("facet.number");
					String transformKeyword=KeywordsUtil.transformSolrMetacharactor(keywords);
					Set<String> analyseValuse=IKDictionary.getAnalyseValues(transformKeyword);
					StringBuffer keywordBuffer=new StringBuffer();
					for(String analyserValuse:analyseValuse){
						keywordBuffer.append(analyserValuse+" AND ");
					}
					sb.append(keywordBuffer.toString());
					fieldNumber+=analyseValuse.size();
					//query.setParam("mm", String.valueOf(analyseValuse.size()));
					query.setParam("mm",String.valueOf(fieldNumber));
					query.setParam("defType", "edismax");
			 }
		   }
			
			String faectQuery = (String) factParameterMap.get("facet.query");
			sb.append(faectQuery);
			if (sb.toString().endsWith(SOLR_SEPARATOR)) {
                sb.delete(sb.toString().length() - SOLR_SEPARATOR.length(), sb.toString().length());
            }
			if (StringUtils.isNotEmpty(sb.toString())) {
				query.setQuery(sb.toString());
			} else {
				query.setQuery("*:*");
			}
			// 设置facet字段
			@SuppressWarnings("unchecked")
			List<String> facetField = (List<String>) factParameterMap.get("facet.field");
			for (String field : facetField) {
				query.addFacetField(field);
			}
		}
		
		query.setFacet(true);
		query.setRows(0);
		query.set("indent", true);
		query.setFacetMinCount(1);
		List<FacetField> faectField = null;
		try {
			query.setParam("q.op", SOLR_SEPARATOR.trim());
			QueryResponse qrsp = solrServer.query(query);
			faectField = qrsp.getFacetFields();
			query.clear();
			qrsp = null;
			query = null;
		} catch (Exception e) {
			throw new Exception(e);
		}
		return faectField;
	}

	@SuppressWarnings("unused")
	private String addBlank2Expression(String oldExpression) {
		String lastExpression;
		lastExpression = oldExpression.replace("AND", " AND ").replace("NOT", " NOT ").replace("OR", " OR ");
		return lastExpression;
	}

	public QueryResponse factQuery(Map<String, String> solrQueryMap, List<String> facet_query, List<String> facetField) throws Exception {
		SolrQuery query = new SolrQuery();
		for (String facetQuery : facet_query) {
			query.addFacetQuery(facetQuery);
		}
		for (String field : facetField) {
			query.addFacetField(field);
		}
		// 设置搜索字段
		if (null == solrQueryMap) {
			query.setQuery("*:*");// 查询条件为空的时候,默认查询全部数据
			// throw new Exception("搜索字段不可为空!");
		} else {
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
			if (StringUtils.isEmpty(sb.toString())) {
				query.setQuery("*:*");
			} else {
				query.setQuery(sb.toString());
			}
		}

		//query.setQuery("*:*");
		query.setFacet(true);
		query.setRows(0);
		query.set("indent", true);
		query.setFacetMinCount(1);
		QueryResponse qrsp = null;
		try {
			query.setParam("q.op", SOLR_SEPARATOR.trim());
			qrsp = solrServer.query(query);
			query.clear();
			//qrsp = null;
			query = null;
		} catch (Exception e) {
			throw new Exception(e);
		}
		return qrsp;
	}

	public List<FacetField> factCatQuery(String facetQuery, String facetField) throws Exception {
	    if(StringUtils.isNotEmpty(facetQuery) && facetQuery.endsWith(" AND ")){
	        facetQuery=facetQuery.substring(0,facetQuery.length()-5);
	    }
		SolrQuery query = new SolrQuery();
		query.addFacetField(facetField);
		query.setQuery(facetQuery);
		query.setFacet(true);
		query.setRows(0);
		query.set("indent", true);
		query.setFacetMinCount(1);
		List<FacetField> faectField = null;
		try {
			query.setParam("q.op", SOLR_SEPARATOR.trim());
			QueryResponse qrsp = solrServer.query(query);
			faectField = qrsp.getFacetFields();
			query.clear();
			qrsp = null;
			query = null;
		} catch (Exception e) {
			throw new Exception(e);
		}
		return faectField;
	}

	public List<FacetField> factQuery(Map<String, String> solrQueryMap, Map<String, Object> factParameterMap) throws Exception {
		SolrQuery query = new SolrQuery();
		// 设置搜索字段
		if (null == solrQueryMap) {
			query.setQuery("*:*");// 查询条件为空的时候,默认查询全部数据
			// throw new Exception("搜索字段不可为空!");
		} else {
			StringBuffer sb = new StringBuffer();
			for (String o : solrQueryMap.keySet()) {
				String value = solrQueryMap.get(o);
				if (StringUtils.isNotEmpty(value)) {
					if ("keywords".equals(o)) {
						sb.append(KeywordsUtil.transformSolrMetacharactor(value)).append(SOLR_SEPARATOR);
					} else if ("queryValue".equals(o)) {
						//处理一些需要构造的特殊查询
						sb.append(value).append(SOLR_SEPARATOR);
					} else {
						sb.append(o).append(":").append(value).append(SOLR_SEPARATOR);
					}
				}
			}

			if (sb.toString().endsWith(SOLR_SEPARATOR)) {
				sb.delete(sb.toString().length() - SOLR_SEPARATOR.length(), sb.toString().length());
			}
			if (StringUtils.isEmpty(sb.toString())) {
				query.setQuery("*:*");
			} else {
				query.setQuery(sb.toString());
			}
		}
		@SuppressWarnings("unchecked")
		List<String> facetField = (List<String>) factParameterMap.get("facet.field");
		for (String field : facetField) {
			query.addFacetField(field);
		}
		//query.setQuery("*:*");
		query.setFacet(true);
		query.setRows(0);
		query.set("indent", true);
		query.setFacetMinCount(1);
		QueryResponse qrsp = null;
		try {
			if (StringUtils.isNotEmpty(solrQueryMap.get("queryValue"))) {
				query.setParam("q.op", SOLR_SEPARATORVALUE.trim());
			} else {
				query.setParam("q.op", SOLR_SEPARATOR.trim());
			}
			qrsp = solrServer.query(query);
			query.clear();
			//qrsp = null;
			query = null;
		} catch (Exception e) {
			throw new Exception(e);
		}
		return qrsp.getFacetFields();
	}

    public  Map<String, Integer> factQuery(Map<String, Object> factParameterMap, List<Price> priceList) throws Exception {
        SolrQuery query = new SolrQuery();
        if (null == factParameterMap) {
            throw new Exception("facet字段不可为空!");
        } else {
            // 设置facet的query字段
            StringBuffer sb = new StringBuffer();
          
           if(factParameterMap.get("facet.flag")!=null && factParameterMap.get("facet.keywords")!=null){
                String flag=(String) factParameterMap.get("facet.flag");
                String keywords=(String) factParameterMap.get("facet.keywords");
                if(StringUtils.isNotEmpty(keywords)&&"main".equals(flag)){
                    int fieldNumber=(Integer) factParameterMap.get("facet.number");
                    String transformKeyword=KeywordsUtil.transformSolrMetacharactor(keywords);
                    Set<String> analyseValuse=IKDictionary.getAnalyseValues(transformKeyword);
                    StringBuffer keywordBuffer=new StringBuffer();
                    for(String analyserValuse:analyseValuse){
                        keywordBuffer.append(analyserValuse+" AND ");
                    }
                    sb.append(keywordBuffer.toString());
                    fieldNumber+=analyseValuse.size();
                    //query.setParam("mm", String.valueOf(analyseValuse.size()));
                    query.setParam("mm",String.valueOf(fieldNumber));
                    query.setParam("defType", "edismax");
             }
           }
            
            String faectQuery = (String) factParameterMap.get("facet.query");
            sb.append(faectQuery);
            if (sb.toString().endsWith(SOLR_SEPARATOR)) {
                sb.delete(sb.toString().length() - SOLR_SEPARATOR.length(), sb.toString().length());
            }
            if (StringUtils.isNotEmpty(sb.toString())) {
                query.setQuery(sb.toString());
            } else {
                query.setQuery("*:*");
            }
           //设置区间查询字段
            for(Price price:priceList){
                query.addFacetQuery(String.format("effect_price:[%s TO %s]",String.valueOf(price.getBegin()),price.getEnd()==0?"*":String.valueOf(price.getEnd())));
            }
        }
        
        query.setFacet(true);
        query.setRows(0);
        query.set("indent", true);
        query.setFacetMinCount(1);
       
        Map<String, Integer> faectQuery = null;
        try {
            query.setParam("q.op", SOLR_SEPARATOR.trim());
            QueryResponse qrsp = solrServer.query(query);
            faectQuery = qrsp.getFacetQuery();
            query.clear();
            qrsp = null;
            query = null;
        } catch (Exception e) {
            throw new Exception(e);
        }
        return faectQuery;
    }
}