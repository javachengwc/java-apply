package com.ocean.merger.aggregation;

import com.ocean.jdbc.ShardResultSet;
import com.ocean.merger.MergeContext;
import com.ocean.merger.resultset.ResultSetUtil;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 聚合结果集
 */
public class AggregationResultSet extends ShardResultSet {

    private Collection<ResultSet> effectivedResultSets;

    private List<AggregationColumn> aggregationColumns;

    private boolean hasIndexesForAggregationColumns;

    public Collection<ResultSet> getEffectivedResultSets() {
        return effectivedResultSets;
    }

    public void setEffectivedResultSets(Collection<ResultSet> effectivedResultSets) {
        this.effectivedResultSets = effectivedResultSets;
    }

    public List<AggregationColumn> getAggregationColumns() {
        return aggregationColumns;
    }

    public void setAggregationColumns(List<AggregationColumn> aggregationColumns) {
        this.aggregationColumns = aggregationColumns;
    }

    public boolean isHasIndexesForAggregationColumns() {
        return hasIndexesForAggregationColumns;
    }

    public void setHasIndexesForAggregationColumns(boolean hasIndexesForAggregationColumns) {
        this.hasIndexesForAggregationColumns = hasIndexesForAggregationColumns;
    }

    public AggregationResultSet(List<ResultSet> resultSets, MergeContext mergeContext) {
        super(resultSets, mergeContext.getLimit());
        aggregationColumns = mergeContext.getAggregationColumns();
        effectivedResultSets = new LinkedHashSet<ResultSet>(resultSets.size());
    }

    @Override
    public boolean nextForShard() throws SQLException {

        logger.info("AggregationResultSet nextForShard start");

        if (!hasIndexesForAggregationColumns) {
            ResultSetUtil.fillIndexesForDerivedAggregationColumns(getResultSets().iterator().next(), aggregationColumns);
            hasIndexesForAggregationColumns = true;
        }
        for (ResultSet each : getResultSets()) {
            if (!each.next()) {
                effectivedResultSets.remove(each);
                continue;
            }
            effectivedResultSets.add(each);
        }
        return !effectivedResultSets.isEmpty();
    }
}