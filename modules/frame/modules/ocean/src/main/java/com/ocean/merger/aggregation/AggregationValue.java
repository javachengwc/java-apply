package com.ocean.merger.aggregation;

import com.ocean.merger.resultset.ResultSetQueryIndex;

import java.sql.SQLException;

/**
 * 聚合值接口
 */
public interface AggregationValue {

    public Comparable<?> getValue(ResultSetQueryIndex resultSetQueryIndex) throws SQLException;
}
