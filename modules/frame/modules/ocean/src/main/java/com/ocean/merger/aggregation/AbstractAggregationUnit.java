package com.ocean.merger.aggregation;

import com.ocean.merger.resultset.ResultSetQueryIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 归并计算单元的抽象类
 */
public abstract class AbstractAggregationUnit implements AggregationUnit {

    private static Logger logger = LoggerFactory.getLogger(AbstractAggregationUnit.class);

    @Override
    public final void merge(AggregationColumn aggregationColumn, AggregationValue aggregationValue, ResultSetQueryIndex resultSetQueryIndex) throws SQLException {
        logger.info("AbstractAggregationUnit merge start");
        if (!aggregationColumn.getDerivedColumns().isEmpty()) {
            Collection<Comparable<?>> paramList = new ArrayList<Comparable<?>>(aggregationColumn.getDerivedColumns().size());
            for (AggregationColumn each : aggregationColumn.getDerivedColumns()) {
                paramList.add(aggregationValue.getValue(new ResultSetQueryIndex(each.getAlias().get())));
            }
            doMerge(paramList.toArray(new Comparable<?>[aggregationColumn.getDerivedColumns().size()]));
        } else {
            doMerge(aggregationValue.getValue(resultSetQueryIndex));
        }
    }

    protected abstract void doMerge(Comparable<?>... values);
}