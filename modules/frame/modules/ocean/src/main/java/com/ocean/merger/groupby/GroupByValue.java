package com.ocean.merger.groupby;

import com.ocean.merger.aggregation.AggregationValue;
import com.ocean.merger.orderby.OrderByColumn;
import com.ocean.merger.resultset.ResultSetQueryIndex;
import com.ocean.merger.resultset.ResultSetUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分组结果集数据存储对象
 */
public class GroupByValue implements AggregationValue, Comparable<GroupByValue> {

    private Map<Integer, Comparable<?>> indexMap = new LinkedHashMap<Integer, Comparable<?>>();

    private Map<String, Comparable<?>> columnLabelMap = new LinkedHashMap<String, Comparable<?>>();

    private List<OrderByColumn> orderColumns = new ArrayList<OrderByColumn>();

    private List<GroupByColumn> groupByColumns = new ArrayList<GroupByColumn>();

    public void put(final int index, final String columnName, final Comparable<?> value) {
        if (!indexMap.containsKey(index)) {
            indexMap.put(index, value);
        }
        if (!columnLabelMap.containsKey(columnName)) {
            columnLabelMap.put(columnName, value);
        }
    }

    @Override
    public Comparable<?> getValue(ResultSetQueryIndex resultSetQueryIndex) {
        return resultSetQueryIndex.isQueryBySequence() ? indexMap.get(resultSetQueryIndex.getQueryIndex()) : columnLabelMap.get(resultSetQueryIndex.getQueryName());
    }

    public void addGroupByColumns(final List<GroupByColumn> columns) {
        groupByColumns.addAll(columns);
    }

    public void addOrderColumns(final List<OrderByColumn> columns) {
        orderColumns.addAll(columns);
    }

    @Override
    public int compareTo(final GroupByValue other) {
        if (null == other) {
            return -1;
        }
        if (orderColumns.isEmpty()) {
            return compareFromGroupByColumns(other);
        }
        return compareFromOrderByColumns(other);
    }

    private int compareFromGroupByColumns(final GroupByValue other) {
        for (GroupByColumn each : groupByColumns) {
            int result = ResultSetUtil.compareTo(columnLabelMap.get(each.getAlias()), other.columnLabelMap.get(each.getAlias()), each.getOrderByType());
            if (0 != result) {
                return result;
            }
        }
        return 0;
    }

    private int compareFromOrderByColumns(final GroupByValue other) {
        for (OrderByColumn each : orderColumns) {
            OrderByColumn.OrderByType orderByType = null == each.getOrderByType() ? OrderByColumn.OrderByType.ASC : each.getOrderByType();
            Comparable<?> thisValue;
            Comparable<?> otherValue;
            if (each.getName().isPresent()) {
                thisValue = columnLabelMap.get(each.getName().get());
                otherValue = other.columnLabelMap.get(each.getName().get());
            } else {
                thisValue = indexMap.get(each.getIndex().get());
                otherValue = other.indexMap.get(each.getIndex().get());
            }
            int result = ResultSetUtil.compareTo(thisValue, otherValue, orderByType);
            if (0 != result) {
                return result;
            }
        }
        return 0;
    }
}