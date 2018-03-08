package com.solr7.search.solr;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolrOperate {

    private static Logger logger= LoggerFactory.getLogger(SolrOperate.class);

    private static String QUERY_AND=" AND ";

    public static String SORT_ASC="asc";

    public static String ORT_DESC="desc";

    private static String KEYWORD="keyword";

    private SolrClient solrClient;

    public SolrOperate() {
    }

    public SolrOperate(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public void setSolrClient(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public SolrDocumentList query(Map<String, String> queryMap, Map<String, String> sortMap,
                                  Long start, Long pageSize) throws Exception {
        logger.error("SolrQuery query start................");
        SolrQuery query = new SolrQuery();
        if (null == queryMap) {
            // 查询条件为空时,默认查询全部
            query.setQuery("*:*");
        } else {
            StringBuffer buffer = new StringBuffer("");
            for (Map.Entry<String, String> entry: queryMap.entrySet()) {
                String key = entry.getKey();
                String value =entry.getValue();
                if(StringUtils.isBlank(value)) {
                    continue;
                }
                if (KEYWORD.equals(key)) {
                    String transformKeyword = transMetacharactor(value);
                    buffer.append(key).append(":").append(transformKeyword).append(QUERY_AND);
                }else {
                    buffer.append(key).append(":").append(value).append(QUERY_AND);
                }
            }
            String queryStr =buffer.toString();
            if (queryStr.endsWith(QUERY_AND)) {
                queryStr=queryStr.substring(0,queryStr.length()-QUERY_AND.length());
            }
            if (StringUtils.isBlank(queryStr)) {
                query.setQuery("*:*");
            } else {
                query.setQuery(queryStr);
            }
        }
        query.setParam("q.op", QUERY_AND.trim());

        // 设置排序
        if (null != sortMap) {
            for (String  sortStr : sortMap.keySet()) {
                if (StringUtils.isBlank(sortStr)) {
                    continue;
                }
                String sortVlu = sortMap.get(sortStr);
                if (SORT_ASC.equalsIgnoreCase(sortVlu)) {
                    query.addSort(sortStr, org.apache.solr.client.solrj.SolrQuery.ORDER.asc);
                } else {
                    query.addSort(sortStr, org.apache.solr.client.solrj.SolrQuery.ORDER.desc);
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
            logger.error("SolrQuery query error,",e);
            throw new RuntimeException(e);
        }
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
}