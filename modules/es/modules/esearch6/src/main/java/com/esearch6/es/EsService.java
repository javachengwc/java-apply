package com.esearch6.es;

import com.esearch6.model.AggResult;
import com.esearch6.model.RangeValue;
import com.util.page.Page;

import java.util.List;
import java.util.Map;

public interface EsService {

    //分页查询
    public <T> Page<T> queryPageBean(String collectName, String indexType,
                                     Map<String, String> queryMap, List<RangeValue> rangeList, Map<String, String> sortMap,
                                     Long start, Long pageSize, Class<T> clazz) throws Exception;

    //聚合查询(也就是分组查询)
    public AggResult queryAgg(String collectName, String indexType,
                              Map<String, String> queryMap, List<String> aggFieldList) throws Exception;

    //增加索引
    public boolean addIndex(String collectName,String indexType,Map<String, Object> dataMap) throws Exception;

    //修改或增加索引
    public boolean uptOrAddIndex(String collectName,String indexType,Map<String, Object> dataMap,String businessIdKey) throws Exception;

    //删除索引
    public long deleteIndex(String collectName,String indexType,String businessIdKey,String businessIdValue) throws Exception;

    //根据条件删除索引
    public boolean deleteIndexByCdn(String collectName,String indexType,String cdnKey,String cdnValue) throws Exception;
}
