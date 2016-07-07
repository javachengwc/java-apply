package com.ocean.merger.aggregation;

import com.ocean.merger.resultset.ResultSetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 累加聚合单元
 */
public class AccumulationAggregationUnit extends AbstractAggregationUnit {

    private static Logger logger = LoggerFactory.getLogger(AccumulationAggregationUnit.class);

    private Class<?> returnType;

    public AccumulationAggregationUnit()
    {

    }

    public AccumulationAggregationUnit( Class<?> returnType)
    {
        this.returnType=returnType;
    }

    private BigDecimal result = new BigDecimal(0);

    @Override
    public void doMerge(final Comparable<?>... values) {
        result = result.add(new BigDecimal(values[0].toString()));
        logger.info("AccumulationAggregationUnit sum result: {}", result.toString());
    }

    @Override
    public Comparable<?>  getResult() {
        return (Comparable<?>) ResultSetUtil.convertValue(result, returnType);
    }
}
