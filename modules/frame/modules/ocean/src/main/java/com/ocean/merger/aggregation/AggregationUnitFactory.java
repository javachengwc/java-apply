package com.ocean.merger.aggregation;

/**
 * 聚合函数结果集归并单元工厂
 */
public class AggregationUnitFactory {

    public static AggregationUnit create(AggregationColumn.AggregationType type, Class<?> returnType) {
        switch (type) {
            case MAX:
                return new ComparableAggregationUnit(false);
            case MIN:
                return new ComparableAggregationUnit(true);
            case SUM:
            case COUNT:
                return new AccumulationAggregationUnit(returnType);
            case AVG:
                return new AvgAggregationUnit(returnType);
            default:
                throw new UnsupportedOperationException(type.name());
        }
    }
}

