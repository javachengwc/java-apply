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
}
