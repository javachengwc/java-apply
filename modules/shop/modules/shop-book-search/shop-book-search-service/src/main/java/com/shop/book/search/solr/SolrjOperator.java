package com.shop.book.search.solr;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolrjOperator {

    private static Logger logger= LoggerFactory.getLogger(SolrjOperator.class);

    private static String SOLR_QUERY_AND=" AND ";

    public static String SOLR_SORT_ASC="asc";

    public static String SOLR_SORT_DESC="desc";

    private static String QUERY_KEYWORD="keyword";

    private SolrClient solrClient;

    public SolrjOperator() {
    }

    public SolrjOperator(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public void setSolrClient(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public SolrDocumentList query(Map<String, Object> queryMap, Map<String, String> sortMap,
                                  Long start, Long pageSize) throws Exception {
        SolrQuery query = new SolrQuery();
        int fieldCount = 0;
        if (null == queryMap) {
            query.setQuery("*:*");// 查询条件为空时,默认查询全部
        } else {
            StringBuffer buffer = new StringBuffer("");
            for (Map.Entry<String, Object> entry: queryMap.entrySet()) {
                String key = entry.getKey();
                Object valObject =entry.getValue();
                if(valObject==null) {
                    continue;
                }
                if (valObject instanceof List<?>) {
                    List<?> valList = (List<?>)valObject;
                    if(valList==null || valList.size()<=0) {
                        continue;
                    }
                    for(Object obj:valList) {
                        String fragmentQueryStr =joinQuery(key,obj);
                        buffer.append(fragmentQueryStr);
                        fieldCount++;
                    }
                } else {
                    String fragmentQueryStr =joinQuery(key,valObject);
                    buffer.append(fragmentQueryStr);
                    fieldCount++;
                }
            }

            String queryStr =buffer.toString();
            if (queryStr.endsWith(SOLR_QUERY_AND)) {
                queryStr=queryStr.substring(0,queryStr.length()-SOLR_QUERY_AND.length());
            }
            if (StringUtils.isBlank(queryStr)) {
                query.setQuery("*:*");
            } else {
                query.setQuery(queryStr);
            }
        }
        query.setParam("q.op", SOLR_QUERY_AND.trim());

        // 设置排序
        if (null != sortMap) {
            for (String  sortStr : sortMap.keySet()) {
                if (StringUtils.isBlank(sortStr)) {
                    continue;
                }
                String sortVlu = sortMap.get(sortStr);
                if (SOLR_SORT_ASC.equalsIgnoreCase(sortVlu)) {
                    query.addSort(sortStr, SolrQuery.ORDER.asc);
                } else {
                    query.addSort(sortStr, SolrQuery.ORDER.desc);
                }
            }
        }
        if (null != start) {
            query.setStart(start.intValue());
        }
        if (null != pageSize) {
            query.setRows(pageSize.intValue());
        }
        try {
            QueryResponse qrsp = solrClient.query(query);
            SolrDocumentList docs = qrsp.getResults();
            query.clear();
            return docs;
        } catch (Exception e) {
            logger.error("SolrjOperator query error,",e);
            throw new RuntimeException(e);
        }
    }

    public String joinQuery(String key,Object valObject) {
        String value= valObject==null?null:valObject.toString();
        if(StringUtils.isBlank(value)) {
           return "";
        }
        StringBuffer buffer = new StringBuffer("");
        if (QUERY_KEYWORD.equals(key)) {
            String transformKeyword = transMetacharactor(value);
            buffer.append(key).append(":").append(transformKeyword).append(SOLR_QUERY_AND);
        }else {
            buffer.append(key).append(":").append(value).append(SOLR_QUERY_AND);
        }
        String rt =buffer.toString();
        return rt;
    }

    public static String transMetacharactor(String input){
        StringBuffer sb = new StringBuffer();
        String regex = "[+/\\-&|!(){}\\[\\]^\"~*?:(\\)<>]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            matcher.appendReplacement(sb, "\\\\\\\\"+matcher.group());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //增加或更新索引文档
    public int addDocument(Map<String,Object> fields) throws Exception{
        SolrInputDocument document = new SolrInputDocument();
        for(Map.Entry<String,Object> entry: fields.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            document.addField(key, value);
        }
        UpdateResponse updateResponse = solrClient.add(document);
        solrClient.commit();
        return updateResponse.getStatus();
    }

    //删除索引文档
    public int deleteDocument(String id) throws Exception{
        UpdateResponse updateResponse = solrClient.deleteById(id);
        solrClient.commit();
        return updateResponse.getStatus();
    }
}