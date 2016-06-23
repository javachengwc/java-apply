package com.ocean.merger.aggregation;

import com.ocean.merger.resultset.ResultSetQueryIndex;

import java.sql.SQLException;

/**
 * 归并计算单元接口.
 */
public interface AggregationUnit {

    /**
     * 归并聚合值
     * @param aggregationColumn 聚合列
     * @param aggregationValue 聚合值
     * @param resultSetQueryIndex 结果集查询索引
     */
    public void merge(AggregationColumn aggregationColumn, AggregationValue aggregationValue, ResultSetQueryIndex resultSetQueryIndex) throws SQLException;

    /**
     * 获取计算结果
     */
    public Comparable<?> getResult();
}
