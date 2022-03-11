package com.es.consumer.es;

import java.util.List;
import java.util.Map;

/**
 * es服务
 */
public interface EsService {

    /**
     * 获取索引
     */
    public String get(String index, String type, String id);

    /**
     * 新增索引
     *
     * @param data 索引数据,json格式
     */
    public void insert(String data, String index, String type, String id);

    /**
     * 增加索引(带等待结果时间)
     *
     * @param data 索引数据,json格式
     */
    public void insert(String data, String index, String type, String id, Long timeout);

    /**
     * 批量新增索引
     *
     * @param listData 索引数据列表,json格式
     */
    public void bulkInsert(List<String> listData, String index, String type);

    /**
     * 批量新增索引
     *
     * @param mapData id-json
     */
    public void bulkInsert(Map<String, String> mapData, String index, String type, boolean autoId);

    /**
     * 更新索引
     *
     * @param data 索引数据,json格式
     */
    public void update(String data, String index, String type, String id);

    /**
     * 更新或增加索引
     * 如果不存在id的索引记录，index新增
     * 如果存在id的索引记录，index更新
     */
    public void upsert(String data, String index, String type, String id);

    /**
     * 删除索引
     */
    public void delete(String index, String type, String id);

    /**
     * 获取es执行线程池状态
     */
    public String getExecutorStatus();
}
