package com.es.consumer.es;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * es工具
 */
public class EsUtil {

    private static Logger logger = LoggerFactory.getLogger(EsUtil.class);

    /**
     * 增加索引
     *
     * @param data 索引数据,json格式
     */
    public static void insert(Client client, String data, String index, String type, String id) {
        EsUtil.insert(client, data, index, type, id, null);
    }

    /**
     * 增加索引(带等待结果时间)
     *
     * @param data 索引数据,json格式
     */
    public static void insert(Client client, String data, String index, String type, String id, Long timeout) {
        try {
            long startTime = System.currentTimeMillis();
            IndexRequestBuilder builder = null;
            if (null == id) {
                builder = client.prepareIndex(index, type);
            } else {
                builder = client.prepareIndex(index, type, id);
            }
            IndexResponse response = null;
            if (null == timeout) {
                response = builder.setSource(data).execute().actionGet();
            } else {
                response = builder.setSource(data).execute().actionGet(timeout);
            }
            long diffTime = System.currentTimeMillis() - startTime;
            logger.info("EsUtil insert end,index={},type={},id={},cost time={},result _id={}", index, type, id, diffTime, response.getId());
        } catch (Exception e) {
            logger.error("EsUtil insert error,index={},type={},id={}", index, type, id, e);
        }
    }

    /**
     * 批量新增索引
     *
     * @param listData 索引数据列表,json格式
     */
    public static void bulkInsert(Client client, List<String> listData, String index, String type) {
        try {
            long startTime = System.currentTimeMillis();
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (String data : listData) {
                bulkRequest.add(client.prepareIndex(index, type).setSource(data));
            }
            BulkResponse bulkResponse = bulkRequest.get();
            long diffTime = System.currentTimeMillis() - startTime;
            if (bulkResponse.hasFailures()) {
                logger.warn("EsUtil bulkInsert list hasFailures,index={},type={},cost time={},failureMessage={}",
                        index, type, diffTime, bulkResponse.buildFailureMessage());
            }
            logger.info("EsUtil bulkInsert list end,index={},type={},cost time={}", index, type, diffTime);
        } catch (Exception e) {
            logger.error("EsUtil bulkInsert list error,index={},type={}", index, type, e);
        }
    }

    /**
     * 批量增加索引
     *
     * @param mapData id-json
     */
    public static void bulkInsert(Client client, Map<String, String> mapData, String index, String type) {
        try {
            long startTime = System.currentTimeMillis();
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (Map.Entry<String, String> entry : mapData.entrySet()) {
                String id = entry.getKey();
                if (StringUtils.isNotBlank(id)) {
                    bulkRequest.add(client.prepareIndex(index, type, id).setSource(entry.getValue()));
                } else {
                    bulkRequest.add(client.prepareIndex(index, type).setSource(entry.getValue()));
                }
            }
            BulkResponse bulkResponse = bulkRequest.get();
            long diffTime = System.currentTimeMillis() - startTime;
            if (bulkResponse.hasFailures()) {
                logger.warn("EsUtil bulkInsert map hasFailures,index={},type={},cost time={},failureMessage={}",
                        index, type, diffTime, bulkResponse.buildFailureMessage());
            }
            logger.info("EsUtil bulkInsert map end,index={},type={},cost time={}", index, type, diffTime);
        } catch (Exception e) {
            logger.error("EsUtil bulkInsert map error,index={},type={}", index, type, e);
        }
    }
}
