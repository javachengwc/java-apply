package com.es.consumer.es.impl;

import com.es.consumer.es.EsService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EsServiceImpl implements EsService {

    private Logger logger = LoggerFactory.getLogger(EsServiceImpl.class);

    private ThreadPoolExecutor esExecutor = null;

    @Autowired(required = false)
    @Qualifier("esClient")
    private Client client;

    public EsServiceImpl() {
        this.init();
    }

    private void init() {
        logger.info("EsServiceImpl init start.............");

        esExecutor = new ThreadPoolExecutor(8, 8, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    final AtomicInteger poolNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "EsExecutor-thread-" + poolNumber.getAndIncrement());
                    }
                }, new ThreadPoolExecutor.CallerRunsPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                while (!esExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                    String threadInfo = this.getExecutorStatus();
                    logger.info("EsServiceImpl thread doing 执行中,线程池信息:" + threadInfo);
                }
            } catch (Exception e) {
                logger.info("EsServiceImpl shutdownHook error,", e);
            }
        }));
        logger.info("EsServiceImpl init end.............");
    }

    @Override
    public String get(String index, String type, String id) {
        String result = null;
        if (StringUtils.isBlank(id)) {
            return result;
        }
        try {
            GetResponse get = client.prepareGet(index, type, id).get();
            result = get.getSourceAsString();
        } catch (Exception e) {
            logger.error("EsServiceImpl get error,index={},type={},id={}", index, type, id, e);
        }
        return result;

    }

    @Override
    public void insert(String data, String index, String type, String id) {
        this.insert(data, index, type, id, null);
    }

    @Override
    public void insert(String data, String index, String type, String id, Long timeout) {
        try {
            esExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    _insert(data, index, type, id, timeout);
                }
            });
        } catch (Exception e) {
            logger.error("EsServiceImpl insert error,index={},type={},id={}", index, type, id, e);
        }
    }

    private void _insert(String data, String index, String type, String id, Long timeout) {
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
            logger.info("EsServiceImpl _insert end,index={},type={},id={},cost time={},result _id={}",
                    index, type, id, diffTime, response.getId());
        } catch (Exception e) {
            logger.error("EsServiceImpl _insert error,index={},type={},id={}", index, type, id, e);
        }
    }

    @Override
    public void bulkInsert(List<String> listData, String index, String type) {
        esExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    BulkRequestBuilder bulkRequest = client.prepareBulk();
                    for (String data : listData) {
                        bulkRequest.add(client.prepareIndex(index, type).setSource(data));
                    }
                    BulkResponse bulkResponse = bulkRequest.get();
                    long diffTime = System.currentTimeMillis() - startTime;
                    if (bulkResponse.hasFailures()) {
                        logger.warn("EsServiceImpl bulkInsert list hasFailures,index={},type={},cost time={},failureMessage={}",
                                index, type, diffTime, bulkResponse.buildFailureMessage());
                    }
                    logger.info("EsServiceImpl bulkInsert list end,index={},type={},cost time={}", index, type, diffTime);
                } catch (Exception e) {
                    logger.error("EsServiceImpl bulkInsert list error,index={},type={}", index, type, e);
                }
            }
        });
    }

    @Override
    public void bulkInsert(Map<String, String> mapData, String index, String type, boolean autoId) {
        esExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    BulkRequestBuilder bulkRequest = client.prepareBulk();
                    for (Map.Entry<String, String> entry : mapData.entrySet()) {
                        String id = entry.getKey();
                        if(autoId) {
                            bulkRequest.add(client.prepareIndex(index, type).setSource(entry.getValue()));
                        } else {
                            if (StringUtils.isNotBlank(id)) {
                                bulkRequest.add(client.prepareIndex(index, type, id).setSource(entry.getValue()));
                            } else {
                                bulkRequest.add(client.prepareIndex(index, type).setSource(entry.getValue()));
                            }
                        }
                    }
                    BulkResponse bulkResponse = bulkRequest.get();
                    long diffTime = System.currentTimeMillis() - startTime;
                    if (bulkResponse.hasFailures()) {
                        logger.warn("EsServiceImpl bulkInsert map hasFailures,index={},type={},cost time={},failureMessage={}",
                                index, type, diffTime, bulkResponse.buildFailureMessage());
                    }
                    logger.info("EsServiceImpl bulkInsert map end,index={},type={},cost time={}", index, type, diffTime);
                } catch (Exception e) {
                    logger.error("EsServiceImpl bulkInsert map error,index={},type={}", index, type, e);
                }
            }
        });
    }

    @Override
    public void update(String data, String index, String type, String id) {
        esExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    UpdateRequest updateRequest = new UpdateRequest(index, type, id);
                    updateRequest.doc(data);
                    UpdateResponse response = client.update(updateRequest).get();
                    logger.info("EsServiceImpl update end,index={},type={},id={},result _version={}", index, type, id, response.getVersion());
                } catch (Exception e) {
                    logger.error("EsServiceImpl update error,index={},type={},id={}", index, type, id, e);
                }
            }
        });
    }

    @Override
    public void upsert(String data, String index, String type, String id) {
        esExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    IndexRequest indexRequest = new IndexRequest(index, type, id).source(data);
                    UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(data).upsert(indexRequest);
                    UpdateResponse response = client.update(updateRequest).get();
                    logger.info("EsServiceImpl upsert end,index={},type={},id={},result _id={}, _version={}",
                            index, type, id, response.getId(), response.getVersion());
                } catch (Exception e) {
                    logger.error("EsServiceImpl upsert error,index={},type={},id={}", index, type, id, e);
                }
            }
        });
    }

    @Override
    public void delete(String index, String type, String id) {
        esExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    DeleteResponse response = client.prepareDelete(index, type, id)
                            //不另起线程
                            .setOperationThreaded(false)
                            .execute()
                            .actionGet();
                    logger.info("EsServiceImpl delete end,index={},type={},id={},result={}", index, type, id, response.isFound());
                } catch (Exception e) {
                    logger.error("EsServiceImpl delete error,index={},type={},id={}", index, type, id, e);
                }
            }
        });
    }

    @Override
    public String getExecutorStatus() {
        long taskCount = esExecutor.getTaskCount();
        long completedCount = esExecutor.getCompletedTaskCount();
        int queueTaskSize = esExecutor.getQueue().size();
        int activeThreadCount = esExecutor.getActiveCount();
        String result = "EsServiceImpl.esExecutor 运行状态:";
        result += "任务总数:" + taskCount + ",完成任务数:" + completedCount + ",队列中任务数:" + queueTaskSize + ",活动线程数:" + activeThreadCount;
        return result;
    }
}
