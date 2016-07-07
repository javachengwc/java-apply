package com.ocean.merger.aggregation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  比较聚合单元
 */
public class ComparableAggregationUnit extends AbstractAggregationUnit {

    private static Logger logger = LoggerFactory.getLogger(ComparableAggregationUnit.class);

    private boolean asc;

    private Comparable<?> result;

    public ComparableAggregationUnit()
    {

    }

    public ComparableAggregationUnit(boolean asc)
    {
        this.asc=asc;
    }

    public ComparableAggregationUnit(boolean asc,Comparable<?> result)
    {
        this.asc=asc;
        this.result=result;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public void setResult(Comparable<?> result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doMerge(@SuppressWarnings("rawtypes") final Comparable... values) {
        if (null == result) {
            result = values[0];
            logger.info("ComparableAggregationUnit doMerge comparable result: {}", result);
            return;
        }
        int comparedValue = values[0].compareTo(result);
        if (asc && comparedValue < 0 || !asc && comparedValue > 0) {
            result = values[0];
            logger.trace("ComparableAggregationUnit doMerge comparable result: {}", result);
        }
    }

    @Override
    public Comparable<?> getResult() {
        return result;
    }
}

