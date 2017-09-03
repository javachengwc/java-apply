package com.es;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortOrder;

public class ElasticSearchDao {

    //es的客户端实例
    Client client=null;
    {
        client=new TransportClient().
                addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

    }

    public static void main(String[] args)throws Exception {
        ElasticSearchDao es=new ElasticSearchDao();
        //es.indexdata();//索引数据
        //es.queryComplex();
        es.querySimple();
        //es.scorllQuery();
        //es.mutilCombineQuery();
        //es.aggregationQuery();
    }

    /**组合分组查询*/
    public void aggregationQuery()throws Exception{
        SearchResponse sr = client.prepareSearch()
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(
                        AggregationBuilders.terms("1").field("type")
                )
//              .addAggregation(
//                      AggregationBuilders.dateHistogram("agg2")
//                              .field("birth")
//                              .interval(DateHistogram.Interval.YEAR)
//              )
                .execute().actionGet();

        // Get your facet results
        org.elasticsearch.search.aggregations.bucket.terms.Terms a = sr.getAggregations().get("1");

        for(org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bk:a.getBuckets()){
            System.out.println("类型: "+bk.getKey()+"  分组统计数量 "+bk.getDocCount()+"  ");
        }

        System.out.println("聚合数量："+a.getBuckets().size());
        //DateHistogram agg2 = sr.getAggregations().get("agg2");
        //结果：
//          类型: 1  分组数量 2
//          类型: 2  分组数量 1
//          类型: 3  分组数量 1
//          聚合数量：3
    }

    /**多个不一样的请求组装*/
    public void mutilCombineQuery(){

        //查询请求1
        SearchRequestBuilder srb1 =client.prepareSearch().setQuery(QueryBuilders.queryString("eng").field("address")).setSize(1);
        //查询请求2//matchQuery
        SearchRequestBuilder srb2 = client.prepareSearch().setQuery(QueryBuilders.matchQuery("title", "标题")).setSize(1);
        //组装查询
        MultiSearchResponse sr = client.prepareMultiSearch().add(srb1).add(srb2).execute().actionGet();

        // You will get all individual responses from MultiSearchResponse#getResponses()
        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            for(SearchHit hits:response.getHits().getHits()){
                String sourceAsString = hits.sourceAsString();//以字符串方式打印
                System.out.println(sourceAsString);
            }
            nbHits += response.getHits().getTotalHits();
        }
        System.out.println("命中数据量："+nbHits);
        //输出：
//      {"title":"我是标题","price":25.65,"type":1,"status":true,"address":"血落星域风阳星","createDate":"2015-03-16T09:56:20.440Z"}
//      命中数据量：2

        client.close();
    }


    /**翻页查询**/
    public void scorllQuery()throws Exception{
        QueryStringQueryBuilder queryString = QueryBuilders.queryString("标题").field("title");
        //TermQueryBuilder qb=QueryBuilders.termQuery("title", "我是标题");
        SearchResponse scrollResp = client.prepareSearch("collection1")
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000))
                .setQuery(queryString)
                .setSize(100).execute().actionGet(); //100 hits per shard will be returned for each scroll


        while (true) {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                //Handle the hit...
                String sourceAsString = hit.sourceAsString();//以字符串方式打印
                System.out.println(sourceAsString);
            }
            //通过scrollid来实现深度翻页
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).execute().actionGet();
            //Break condition: No hits are returned
            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }
        }
        //输出
//      {"title":"我是标题","price":25.65,"type":1,"status":true,"address":"血落星域风阳星","createDate":"2015-03-16T09:56:20.440Z"}
//      {"title":"标题","price":251.65,"type":1,"status":true,"address":"美国东部","createDate":"2015-03-16T10:33:58.743Z"}
        client.close();

    }

    /**简单查询*/
    public void querySimple()throws Exception{

        SearchResponse sp = client.prepareSearch("collection1").execute().actionGet();
        for(SearchHit hits:sp.getHits().getHits()){
            String sourceAsString = hits.sourceAsString();//以字符串方式打印
            System.out.println(sourceAsString);
        }

        //结果
//              {"title":"我是标题","price":25.65,"type":1,"status":true,"address":"血落星域风阳星","createDate":"2015-03-16T09:56:20.440Z"}
//              {"title":"中国","price":205.65,"type":2,"status":true,"address":"河南洛阳","createDate":"2015-03-16T10:33:58.740Z"}
//              {"title":"标题","price":251.65,"type":1,"status":true,"address":"美国东部","createDate":"2015-03-16T10:33:58.743Z"}
//              {"title":"elasticsearch是一个搜索引擎","price":25.65,"type":3,"status":true,"address":"china","createDate":"2015-03-16T10:33:58.743Z"}


    }
    /**组合查询**/
    public void queryComplex()throws Exception{
        SearchResponse sp=client.prepareSearch("collection1")//检索的目录
                .setTypes("core1")//检索的索引
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//Query type
                .setQuery(QueryBuilders.termQuery("type", "1"))//查询--Query
                .setPostFilter(FilterBuilders.rangeFilter("price").from(10).to(550.23))//过滤 --Filter
                .addSort("price",SortOrder.DESC) //排序 -- sort
                .setFrom(0).setSize(20).setExplain(true)//topN方式
                .execute().actionGet();//执行
        System.out.println("本次查询命中条数: "+sp.getHits().getTotalHits());
        for(SearchHit hits:sp.getHits().getHits()){
            //String sourceAsString = hits.sourceAsString();//以字符串方式打印
            //System.out.println(sourceAsString);
            Map<String, Object> sourceAsMap = hits.sourceAsMap();
            for(Entry<String, Object> k:sourceAsMap.entrySet()){
                System.out.println("name： "+k.getKey()+"     value： "+k.getValue());
            }

            System.out.println("=============================================");

        }

        //结果
//              本次查询命中条数: 2
//              name： title     value： 标题
//              name： price     value： 251.65
//              name： address     value： 美国东部
//              name： status     value： true
//              name： createDate     value： 2015-03-16T10:33:58.743Z
//              name： type     value： 1
//              =============================================
//              name： title     value： 我是标题
//              name： price     value： 25.65
//              name： address     value： 血落星域风阳星
//              name： status     value： true
//              name： createDate     value： 2015-03-16T09:56:20.440Z
//              name： type     value： 1
//              =============================================

        client.close();
    }

    /**索引数据*/
    public void indexdata()throws Exception{

        BulkRequestBuilder bulk=client.prepareBulk();

        XContentBuilder doc=XContentFactory.jsonBuilder()
                .startObject()
                .field("title","中国")
                .field("price",205.65)
                .field("type",2)
                .field("status",true)
                .field("address", "河南洛阳")
                .field("createDate", new Date()).endObject();
        //collection为索引库名，类似一个数据库，索引名为core，类似一个表
//       client.prepareIndex("collection1", "core1").setSource(doc).execute().actionGet();

        //批处理添加
        bulk.add(client.prepareIndex("collection1", "core1").setSource(doc));

        doc=XContentFactory.jsonBuilder()
                .startObject()
                .field("title","标题")
                .field("price",251.65)
                .field("type",1)
                .field("status",true)
                .field("address", "美国东部")
                .field("createDate", new Date()).endObject();
        //collection为索引库名，类似一个数据库，索引名为core，类似一个表
//      client.prepareIndex("collection1", "core1").setSource(doc).execute().actionGet();
        //批处理添加
        bulk.add(client.prepareIndex("collection1", "core1").setSource(doc));

        doc=XContentFactory.jsonBuilder()
                .startObject()
                .field("title","elasticsearch是一个搜索引擎")
                .field("price",25.65)
                .field("type",3)
                .field("status",true)
                .field("address", "china")
                .field("createDate", new Date()).endObject();
        //collection为索引库名，类似一个数据库，索引名为core，类似一个表
        //client.prepareIndex("collection1", "core1").setSource(doc).execute().actionGet();
        //批处理添加
        bulk.add(client.prepareIndex("collection1", "core1").setSource(doc));

        //发一次请求，提交所有数据
        BulkResponse bulkResponse = bulk.execute().actionGet();
        if (!bulkResponse.hasFailures()) {
            System.out.println("创建索引success!");
        } else {
            System.out.println("创建索引异常:"+bulkResponse.buildFailureMessage());
        }
        client.close();//释放资源
//      System.out.println("索引成功！");

    }

    //创建索引
    public void createIndex() throws Exception{
        XContentBuilder builder=XContentFactory.jsonBuilder()
            .startObject()
            .startObject("article")
            .startObject("properties")
                .startObject("title")
                .field("type", "string")
                .field("store", "yes")
                .field("analyzer","ik")
                .field("index","analyzed").endObject()
                .startObject("content")
                .field("type", "string")
                .field("store", "no")
                .field("analyzer","ik")
                .field("index","analyzed").endObject()
                .startObject("url")
                .field("type", "string")
                .field("store", "yes")
                .field("index","not_analyzed").endObject()
            .endObject()
            .endObject()
            .endObject();
        PutMappingRequest mapping = Requests.putMappingRequest("data").type("article").source(builder);
        client.admin().indices().putMapping(mapping).actionGet();
    }
}
