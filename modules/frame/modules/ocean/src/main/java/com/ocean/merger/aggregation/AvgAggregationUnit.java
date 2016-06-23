package com.ocean.merger.aggregation;

import com.ocean.merger.resultset.ResultSetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 平均值聚合单元
 */
public class AvgAggregationUnit extends AbstractAggregationUnit {

    private static Logger logger = LoggerFactory.getLogger(AvgAggregationUnit.class);

    private Class<?> returnType;

    private BigDecimal count = new BigDecimal(0);

    private BigDecimal sum = new BigDecimal(0);

    public AvgAggregationUnit()
    {

    }

    public AvgAggregationUnit(Class<?> returnType)
    {
        this.returnType=returnType;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public void doMerge(final Comparable<?>... values) {
        count = count.add(new BigDecimal(values[0].toString()));
        sum = sum.add(new BigDecimal(values[1].toString()));
        logger.trace("AVG result COUNT: {} SUM: {}", count, sum);
    }

    @Override
    public Comparable<?> getResult() {
        return (Comparable<?>) ResultSetUtil.convertValue(sum.divide(count, 4, BigDecimal.ROUND_HALF_UP), returnType);
    }
}