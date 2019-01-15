package com.esearch6.es.impl;

import com.esearch6.enums.BookIndexKeyEnum;
import com.esearch6.es.EsService;
import com.esearch6.model.AggResult;
import com.esearch6.model.RangeValue;
import com.esearch6.util.IndexKeyUtil;
import com.util.col.MapUtil;
import com.util.page.Page;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsServiceImpl implements EsService {

    private static Logger logger= LoggerFactory.getLogger(EsServiceImpl.class);

    private static String KEYWORD="keyword";

    @Autowired
    private TransportClient esClient;

    //分页查询
    public <T> Page<T> queryPageBean(String collectName,String indexType,
                                     Map<String, String> queryMap,List<RangeValue> rangeList,Map<String, String> sortMap,
                                     Long start, Long pageSize, Class<T> clazz) throws Exception {
        logger.info("EsServiceImpl queryPageBean start...............");
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        for(String colName:queryMap.keySet()) {
            String val= queryMap.get(colName);
            if(StringUtils.isNotBlank(val)) {

                if(KEYWORD.equals(colName)) {
                    //关键字,在多个字段分词搜索
                    boolQuery.must(
                    QueryBuilders.multiMatchQuery(val,
                        BookIndexKeyEnum.NAME.getIndexKey(),
                        BookIndexKeyEnum.STORE_NAME.getIndexKey(),
                        BookIndexKeyEnum.LABEL.getIndexKey(),
                        BookIndexKeyEnum.PUBLISHER_NAME.getIndexKey(),
                        BookIndexKeyEnum.TOP_TYPE_NAME.getIndexKey(),
                        BookIndexKeyEnum.SECOND_TYPE_NAME.getIndexKey()
                    ));
                }else {
                    boolQuery.filter(QueryBuilders.termQuery(colName, val));
                }
            }
        }

        //组装范围条件
        if(rangeList!=null) {
            for(RangeValue rangeValue: rangeList) {
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(rangeValue.getKey());
                if (rangeValue.getMax() !=null) {
                    rangeQueryBuilder.lte(rangeValue.getMax());
                }
                if (rangeValue.getMin() !=null) {
                    rangeQueryBuilder.gte(rangeValue.getMin());
                }
                boolQuery.filter(rangeQueryBuilder);
            }
        }

        //请求builder
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(collectName).setTypes(indexType).setQuery(boolQuery);

        // 设置排序
        if (null != sortMap) {
            for (String  sortStr : sortMap.keySet()) {
                if (StringUtils.isBlank(sortStr)) {
                    continue;
                }
                String sortVlu = sortMap.get(sortStr);
                if (SortOrder.ASC.toString().equalsIgnoreCase(sortVlu)) {
                    requestBuilder.addSort(sortStr, SortOrder.ASC);
                } else {
                    requestBuilder.addSort(sortStr, SortOrder.DESC);
                }
            }
        }

        if (null != start) {
            requestBuilder.setFrom(start.intValue());
        }
        if (null != pageSize) {
            requestBuilder.setSize(pageSize.intValue());
        }

        //设置返回的结果列
        String [] indexKeyArrays= IndexKeyUtil.tipIndexKey(clazz);
        requestBuilder.setFetchSource(indexKeyArrays, null);

        try {
            //es查询
            SearchResponse response = requestBuilder.get();
            if (response.status() != RestStatus.OK) {
                logger.warn("EsServiceImpl queryPageBean query status is no ok for " + requestBuilder);
                Page<T> zeroPage = new Page<T>();
                zeroPage.setResult(new ArrayList<T>());
                zeroPage.setTotalCount(0);
                return zeroPage;
            }

            List<T> list = new ArrayList<T>();

            SearchHits searchHits = response.getHits();
            long totalCount = searchHits.totalHits;
            for (SearchHit hit : searchHits) {
                Map<String,Object> map = hit.getSourceAsMap();
                T t = MapUtil.map2Bean(map,clazz,true);
                list.add(t);
            }

            Page<T> page = new Page<T>();
            page.setResult(list);
            page.setTotalCount(new Long(totalCount).intValue());
            return page;
        } catch (Exception e) {
            logger.error("EsServiceImpl queryPageBean error,",e);
            throw new RuntimeException(e);
        }
    }

    //聚合查询(也就是分组查询)
    public AggResult queryAgg(String collectName, String indexType,
                              Map<String, String> queryMap, List<String> aggFieldList) throws Exception {
        logger.info("EsServiceImpl queryAgg start...............");
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if(queryMap!=null) {
            for (String colName : queryMap.keySet()) {
                String val = queryMap.get(colName);
                if (StringUtils.isNotBlank(val)) {
                    boolQuery.filter(QueryBuilders.termQuery(colName, val));
                }
            }
        }

        AggregationBuilder aggBuilder= null;
        int appCnt=0;
        for(String perAgg:aggFieldList) {
             if(appCnt==0) {
                 //等价于 select count(*) as perAgg from table group by perAgg
                 aggBuilder=AggregationBuilders.terms(perAgg).field(perAgg);
             }else {
                 aggBuilder.subAggregation(AggregationBuilders.terms(perAgg).field(perAgg));
             }
             appCnt++;
        }
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(collectName)
                .setTypes(indexType)
                .setQuery(boolQuery)
                .addAggregation(aggBuilder);

        logger.debug("EsServiceImpl queryAgg request={}",requestBuilder.toString());

//        等价于 select deptid as deptid ,max(salary) as maxsalary from db group by deptid
//        TermsAggregationBuilder aggregationBuilder1 = AggregationBuilders.terms("deptid").field("deptid")
//            .order(Order.aggregation("maxsalary", true)); //按部门最大薪酬升序排序,
//        MaxAggregationBuilder aggregationBuilder2 = AggregationBuilders.max("maxsalary").field("salary");
//        requestBuilder.addAggregation(aggregationBuilder1.subAggregation(aggregationBuilder2));

        try {
            //es查询
            SearchResponse response =requestBuilder.execute().actionGet();
            if (response.status() != RestStatus.OK) {
                logger.warn("EsServiceImpl queryAgg query status is no ok ");
                return AggResult.zeroResult();
            }

            AggResult result = new AggResult();
            Map<String,Long> aggMap = new HashMap<String,Long>();
            result.setAggMap(aggMap);

            Terms terms1 = response.getAggregations().get(aggFieldList.get(0));
            merginResult(result,terms1,result,"",0,aggFieldList);

            return result;
        } catch (Exception e) {
            logger.error("EsServiceImpl queryAgg error,",e);
            throw new RuntimeException(e);
        }
    }

    public void merginResult(AggResult topResult,Terms curTerms,AggResult curResult,String preKey,int curIndex,List<String> fieldList) {
        long totalCnt =0L;

        Map<String,AggResult> innerMap = new HashMap<String,AggResult>();
        curResult.setInnerMap(innerMap);

        for (Terms.Bucket bucket : curTerms.getBuckets()) {
            String key = bucket.getKey()==null?"":bucket.getKey().toString();
            long perCnt = bucket.getDocCount();
            curResult.getGroupMap().put(key,perCnt);
            String aggKey = preKey+"_"+key;
            if(aggKey.startsWith("_")) {
                aggKey=aggKey.substring(1);
            }

            totalCnt+=perCnt;

            int nextIndex = curIndex+1;
            if(nextIndex<=fieldList.size()-1) {
                Terms nextTerm = bucket.getAggregations().get(fieldList.get(nextIndex));
                AggResult innerResult = new AggResult();
                innerMap.put(key,innerResult);
                merginResult(topResult,nextTerm,innerResult,aggKey,nextIndex,fieldList);
            }else {
                topResult.getAggMap().put(aggKey,perCnt);
            }
        }
        curResult.setTotalCnt(totalCnt);
    }
}
