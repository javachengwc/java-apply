package com.ocean.merger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 创建归并分片结果集的工厂
 */
public class ResultSetFactory {

    //获取结果集
    public static ResultSet getResultSet(final List<ResultSet> resultSets, final MergeContext mergeContext) throws SQLException {
        MergeContext.ResultSetType resultSetType  = mergeContext.getResultSetType();
        switch (resultSetType) {
            case GroupBy:
                return createDelegateResultSet(new GroupByInvokeHandler(new GroupByResultSet(resultSets, mergeContext)));
            case Aggregate:
                return createDelegateResultSet(new AggregationInvokeHandler(new AggregationResultSet(resultSets, mergeContext)));
            case OrderBy:
                return new OrderByResultSet(resultSets, mergeContext);
            case Iterator:
                return new IteratorResultSet(resultSets, mergeContext);
            default:
                throw new UnsupportedOperationException(resultSetType.name());
        }
    }

    private static ResultSet createDelegateResultSet(final InvocationHandler handler) {
        return (ResultSet) Proxy.newProxyInstance(ResultSetFactory.class.getClassLoader(), new Class[]{ResultSet.class}, handler);
    }
}
