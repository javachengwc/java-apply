package com.ocean.merger.aggregation;

import com.ocean.merger.resultset.ResultSetQueryIndex;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetAggregationValue implements AggregationValue {

    private ResultSet resultSet;

    public ResultSetAggregationValue(ResultSet resultSet)
    {
        this.resultSet=resultSet;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public Comparable<?> getValue(ResultSetQueryIndex resultSetQueryIndex) throws SQLException {
        return resultSetQueryIndex.isQueryBySequence()? (Comparable<?>) resultSet.getObject(resultSetQueryIndex.getQueryIndex()) : (Comparable<?>) resultSet.getObject(resultSetQueryIndex.getQueryName());
    }
}