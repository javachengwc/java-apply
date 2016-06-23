package com.ocean.merger.groupby;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ocean.executor.ExecuteUnit;
import com.ocean.executor.ExecutorEngine;
import com.ocean.executor.MergeUnit;
import com.ocean.jdbc.ShardResultSet;
import com.ocean.merger.MergeContext;
import com.ocean.merger.aggregation.AggregationColumn;
import com.ocean.merger.aggregation.AggregationUnit;
import com.ocean.merger.aggregation.AggregationUnitFactory;
import com.ocean.merger.orderby.OrderByColumn;
import com.ocean.merger.resultset.ResultSetQueryIndex;
import com.ocean.merger.resultset.ResultSetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 分组结果集
 */
public class GroupByResultSet extends ShardResultSet {

    private static Logger logger = LoggerFactory.getLogger(GroupByResultSet.class);

    private List<GroupByColumn> groupByColumns;

    private List<OrderByColumn> orderByColumns;

    private List<AggregationColumn> aggregationColumns;

    private ResultSetMetaData resultSetMetaData;

    private List<String> columnLabels;

    private Iterator<GroupByValue> groupByResultIterator;

    private GroupByValue currentGroupByResultSet;

    public GroupByResultSet(List<ResultSet> resultSets, MergeContext mergeContext) throws SQLException {
        super(resultSets, mergeContext.getLimit());
        groupByColumns = mergeContext.getGroupByColumns();
        orderByColumns = mergeContext.getOrderByColumns();
        aggregationColumns = mergeContext.getAggregationColumns();
        resultSetMetaData = getResultSets().iterator().next().getMetaData();
        columnLabels = new ArrayList<String>(resultSetMetaData.getColumnCount());
        fillRelatedColumnNames();
    }

    public List<GroupByColumn> getGroupByColumns() {
        return groupByColumns;
    }

    public void setGroupByColumns(List<GroupByColumn> groupByColumns) {
        this.groupByColumns = groupByColumns;
    }

    public List<OrderByColumn> getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(List<OrderByColumn> orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    public List<AggregationColumn> getAggregationColumns() {
        return aggregationColumns;
    }

    public void setAggregationColumns(List<AggregationColumn> aggregationColumns) {
        this.aggregationColumns = aggregationColumns;
    }

    public ResultSetMetaData getResultSetMetaData() {
        return resultSetMetaData;
    }

    public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
        this.resultSetMetaData = resultSetMetaData;
    }

    public List<String> getColumnLabels() {
        return columnLabels;
    }

    public void setColumnLabels(List<String> columnLabels) {
        this.columnLabels = columnLabels;
    }

    public Iterator<GroupByValue> getGroupByResultIterator() {
        return groupByResultIterator;
    }

    public void setGroupByResultIterator(Iterator<GroupByValue> groupByResultIterator) {
        this.groupByResultIterator = groupByResultIterator;
    }

    public GroupByValue getCurrentGroupByResultSet() {
        return currentGroupByResultSet;
    }

    public void setCurrentGroupByResultSet(GroupByValue currentGroupByResultSet) {
        this.currentGroupByResultSet = currentGroupByResultSet;
    }

    private void fillRelatedColumnNames() throws SQLException {
        for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
            columnLabels.add(resultSetMetaData.getColumnLabel(i));
        }
    }

    @Override
    protected boolean nextForShard() throws SQLException {
        if (null == groupByResultIterator) {
            ResultSetUtil.fillIndexesForDerivedAggregationColumns(getResultSets().iterator().next(), aggregationColumns);
            groupByResultIterator = reduce(map()).iterator();
        }
        if (groupByResultIterator.hasNext()) {
            currentGroupByResultSet = groupByResultIterator.next();
            return true;
        } else {
            return false;
        }
    }

    private Multimap<GroupByKey, GroupByValue> map() throws SQLException {
        ExecuteUnit<ResultSet, Map<GroupByKey, GroupByValue>> executeUnit = new ExecuteUnit<ResultSet, Map<GroupByKey, GroupByValue>>() {

            @Override
            public Map<GroupByKey, GroupByValue> execute(final ResultSet resultSet) throws SQLException {
                //应该可以根据limit判断result的初始size，避免size满了重分配
                Map<GroupByKey, GroupByValue> result = new HashMap<GroupByKey, GroupByValue>();
                while (resultSet.next()) {
                    GroupByValue groupByValue = new GroupByValue();
                    for (int count = 1; count <= columnLabels.size(); count++) {
                        groupByValue.put(count, resultSetMetaData.getColumnLabel(count), (Comparable<?>) resultSet.getObject(count));
                    }
                    GroupByKey groupByKey = new GroupByKey();
                    for (GroupByColumn each : groupByColumns) {
                        groupByKey.append(ResultSetUtil.getValue(each, resultSet).toString());
                    }
                    result.put(groupByKey, groupByValue);
                }
                logger.trace("Result set mapping: {}", result);
                return result;
            }
        };

        MergeUnit<Map<GroupByKey, GroupByValue>, Multimap<GroupByKey, GroupByValue>> mergeUnit = new MergeUnit<Map<GroupByKey, GroupByValue>, Multimap<GroupByKey, GroupByValue>>() {

            @Override
            public Multimap<GroupByKey, GroupByValue> merge(final List<Map<GroupByKey, GroupByValue>> values) {
                Multimap<GroupByKey, GroupByValue> result = HashMultimap.create();
                for (Map<GroupByKey, GroupByValue> each : values) {
                    for (Map.Entry<GroupByKey, GroupByValue> entry : each.entrySet()) {
                        result.put(entry.getKey(), entry.getValue());
                    }
                }
                return result;
            }
        };
        Multimap<GroupByKey, GroupByValue> result = ExecutorEngine.execute(getResultSets(), executeUnit, mergeUnit);
        logger.trace("Mapped result: {}", result);
        return result;
    }

    private Collection<GroupByValue> reduce(final Multimap<GroupByKey, GroupByValue> mappedResult) throws SQLException {
        List<GroupByValue> result = new ArrayList<GroupByValue>(mappedResult.values().size() * columnLabels.size());
        for (GroupByKey key : mappedResult.keySet()) {
            Collection<GroupByValue> each = mappedResult.get(key);
            GroupByValue reduceResult = new GroupByValue();
            for (int i = 0; i < columnLabels.size(); i++) {
                int index = i + 1;
                Optional<AggregationColumn> aggregationColumn = findAggregationColumn(index);
                Comparable<?> value = null;
                if (aggregationColumn.isPresent()) {
                    value = aggregate(aggregationColumn.get(), index, each);
                }
                value = null == value ? each.iterator().next().getValue(new ResultSetQueryIndex(index)) : value;
                reduceResult.put(index, columnLabels.get(i), value);
            }
            if (orderByColumns.isEmpty()) {
                reduceResult.addGroupByColumns(groupByColumns);
            } else {
                reduceResult.addOrderColumns(orderByColumns);
            }
            result.add(reduceResult);
        }
        Collections.sort(result);
        logger.trace("Reduced result: {}", result);
        return result;
    }

    private Optional<AggregationColumn> findAggregationColumn(final int index) {
        for (AggregationColumn each : aggregationColumns) {
            if (each.getIndex() == index) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    private Comparable<?> aggregate(final AggregationColumn column, final int index, final Collection<GroupByValue> groupByValues) throws SQLException {
        AggregationUnit unit = AggregationUnitFactory.create(column.getAggregationType(), BigDecimal.class);
        for (GroupByValue each : groupByValues) {
            unit.merge(column, each, new ResultSetQueryIndex(index));
        }
        return unit.getResult();
    }
}