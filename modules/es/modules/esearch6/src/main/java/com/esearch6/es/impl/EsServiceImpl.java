package com.esearch6.es.impl;

import com.esearch6.enums.BookIndexKeyEnum;
import com.esearch6.es.EsService;
import com.esearch6.model.AggResult;
import com.esearch6.model.RangeValue;
import com.esearch6.util.IndexKeyUtil;
import com.esearch6.util.JsonUtil;
import com.google.common.collect.Lists;
import com.util.col.MapUtil;
import com.util.page.Page;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EsServiceImpl implements EsService {

    private static Logger logger= LoggerFactory.getLogger(EsServiceImpl.class);

    private static String KEYWORD="keyword";

    @Autowired
    private TransportClient esClient;

    //创建索引映射
    public void createMapping(String collectName,String indexType,Map<String,Map<String,String>> fieldSetting) throws Exception {
        logger.info("EsServiceImpl createMapping start,collectName={},indexType={}",collectName,indexType);
        CreateIndexRequestBuilder cib=esClient.admin().indices().prepareCreate(collectName);
        XContentBuilder mapping = XContentFactory.jsonBuilder()
            .startObject()
                .startObject("properties"); //设置定义字段
//                    .startObject("author")
//                        .field("type","string") //设置数据类型
//                    .endObject()
//                    .startObject("date")
//                        .field("type","date")  //设置Date类型
//                        .field("format","yyyy-MM-dd HH:mm:ss") //设置Date的格式
//                    .endObject()
        for(String field: fieldSetting.keySet()) {
            mapping.startObject(field);
            Map<String,String> setting = fieldSetting.get(field);
            for(Map.Entry<String,String> entry:setting.entrySet()) {
                mapping.field(entry.getKey(),entry.getValue());
            }
            mapping.endObject();
        }
        mapping.endObject();
        mapping.endObject();

        cib.addMapping(indexType, mapping);
        CreateIndexResponse response=cib.execute().get();
        logger.info("EsServiceImpl createMapping end,collectName={},indexType={}",collectName,indexType);
    }

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
                 aggBuilder=AggregationBuilders.terms(perAgg).field(perAgg).size(Integer.MAX_VALUE);
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

    //增加索引
    public boolean  addIndex(String collectName,String indexType,Map<String, Object> dataMap) throws Exception {
        logger.info("EsServiceImpl addIndex start,collectName={},indexType={},dataMap={}",collectName,indexType,dataMap);
        try {
            IndexRequestBuilder requestBuilder =  this.esClient.prepareIndex(collectName, indexType)
                    .setSource(JsonUtil.obj2Json(dataMap),XContentType.JSON);
            IndexResponse response = requestBuilder.get();
            boolean rt = (response.status() == RestStatus.CREATED);
            return rt;
        } catch (Exception e) {
            logger.error("EsServiceImpl addIndex error,",e);
            throw new RuntimeException(e);
        }
    }

    //修改或增加索引
    public boolean uptOrAddIndex(String collectName,String indexType,Map<String, Object> dataMap,String businessIdKey) throws Exception {
        logger.info("EsServiceImpl uptIndex start,collectNam={},indexType={},businessIdKey={}",collectName,indexType,businessIdKey);
        try {
            String businessIdValue = dataMap.get(businessIdKey)==null?"":dataMap.get(businessIdKey).toString();
            SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(collectName).setTypes(indexType)
                    .setQuery(QueryBuilders.termQuery(businessIdKey, businessIdValue));
            SearchResponse searchResponse = requestBuilder.get();
            long totalHit = searchResponse.getHits().getTotalHits();
            boolean rt=false;
            if (totalHit == 0) {
                rt =addIndex(collectName,indexType,dataMap);
            } else if (totalHit == 1) {
                String hitId = searchResponse.getHits().getAt(0).getId();
                rt = uptByHitId(collectName,indexType,hitId,dataMap);
            } else {
                deleteIndex(collectName,indexType,businessIdKey,businessIdValue); //先删
                rt=addIndex(collectName,indexType,dataMap); //后加
            }
            return rt;
        } catch (Exception e) {
            logger.error("EsServiceImpl uptIndex error,",e);
            throw new RuntimeException(e);
        }
    }

    //根据索引文档id更新索引
    private boolean uptByHitId(String collectName,String indexType,String hitId,Map<String, Object> dataMap) {
        logger.info("EsServiceImpl uptByHitId start,collectName={},indexType={},hitId={}", collectName,indexType,hitId);
        try {
            UpdateResponse response = this.esClient.prepareUpdate(collectName, indexType, hitId)
                    .setDoc(JsonUtil.obj2Json(dataMap), XContentType.JSON)
                    .get();
            boolean rt = (response.status() == RestStatus.OK);
            return rt;
        } catch (Exception e) {
            logger.error("EsServiceImpl uptByHitId error,", e);
            throw new RuntimeException(e);
        }
    }

    //删除索引
    public long deleteIndex(String collectName,String indexType,String businessIdKey,String businessIdValue) throws Exception {
        logger.info("EsServiceImpl deleteIndex start,collectName={},indexType={},businessId={}",collectName,indexType,businessIdValue);

        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(businessIdKey, businessIdValue))
                .source(collectName);

        BulkByScrollResponse response = builder.get();
        long delCnt = response.getDeleted();

        //如果是一个长时间运行的操作，可以异步地完成它，这里调用execute而不是get并提供一个侦听器
//        DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient)
//                .filter(QueryBuilders.matchQuery(businessIdKey, businessIdValue))
//                .source(indexType)
//                .execute(new ActionListener<BulkByScrollResponse>() {
//                    public void onResponse(BulkByScrollResponse response) {
//                        long deleted = response.getDeleted();
//                    }
//                    public void onFailure(Exception e) {
//                        // Handle the exception
//                    }
//                });

        return delCnt;
    }

    //根据索引文档id删除索引
    private boolean deleteByHitId(String collectName,String indexType,String hitId) {
        DeleteResponse response = esClient.prepareDelete(collectName, indexType, hitId).get();
        boolean rt = response.status()==RestStatus.OK;
        return rt;
    }

    //根据条件删除索引,谨慎使用
    public boolean deleteIndexByCdn(String collectName,String indexType,String cdnKey,String cdnValue) throws Exception {
        logger.info("EsServiceImpl deleteIndexByCdn start,collectName={},indexType={},cdnKey={},cdnValue={}",collectName,indexType,cdnKey,cdnValue);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery(cdnKey, cdnValue));

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(collectName).setTypes(indexType);
        searchRequestBuilder.setQuery(queryBuilder);
        SearchResponse response = searchRequestBuilder.execute().get();
        SearchHits searchHits = response.getHits();
        long totalCount = searchHits.totalHits;

        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        for(SearchHit hit : searchHits){
            String id = hit.getId();
            bulkRequest.add(esClient.prepareDelete(collectName, indexType, id).request());
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            StringBuilder buf = new StringBuilder("");
            for(BulkItemResponse item : bulkResponse.getItems()){
                buf.append(item.getFailureMessage()).append(";");
            }
            logger.warn("EsServiceImpl deleteIndexByCdn fail,collectName={},indexType={},cdnKey={},cdnValue={},failInfo={}",
                    collectName,indexType,cdnKey,cdnValue,buf.toString());
            return false;
        }
        return true;

    }

    //获取补全建议关键词
    public List<String> suggest(String collectName,String indexType,String prefix) {
        CompletionSuggestionBuilder suggestion = SuggestBuilders.completionSuggestion("suggest").prefix(prefix).size(5);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("autocomplete", suggestion);

        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(collectName)
                .setTypes(indexType)
                .suggest(suggestBuilder);
        logger.info("EsServiceImpl suggest start,prefix={},request={}",prefix,requestBuilder.toString());

        SearchResponse response = requestBuilder.get();
        Suggest suggest = response.getSuggest();
        if (suggest == null) {
            return new ArrayList<String>();
        }
        Suggest.Suggestion result = suggest.getSuggestion("autocomplete");

        int maxSuggest = 0;//最多取数
        Set<String> suggestSet = new HashSet<String>();

        for (Object term : result.getEntries()) {
            if (term instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;
                if (item.getOptions()==null || item.getOptions().isEmpty()) {
                    continue;
                }
                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String tip = option.getText().string();
                    if (suggestSet.contains(tip)) {
                        continue;
                    }
                    suggestSet.add(tip);
                    maxSuggest++;
                }
            }
            if (maxSuggest > 5) {
                //最多取数超过5条就不取了
                break;
            }
        }
        List<String> rtList = new ArrayList<String>(suggestSet);
        return  rtList;
    }
}
