package com.ocean.merger;

import java.util.ArrayList;
import java.util.List;

/**
 * 结果归并上下文
 */
public class MergeContext {

    private List<OrderByColumn> orderByColumns = new ArrayList<OrderByColumn>();

    private List<GroupByColumn> groupByColumns = new ArrayList<GroupByColumn>();

    private List<AggregationColumn> aggregationColumns = new ArrayList<AggregationColumn>();

    private int offset;

    private int rowCount;

    public List<OrderByColumn> getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(List<OrderByColumn> orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    public List<GroupByColumn> getGroupByColumns() {
        return groupByColumns;
    }

    public void setGroupByColumns(List<GroupByColumn> groupByColumns) {
        this.groupByColumns = groupByColumns;
    }

    public List<AggregationColumn> getAggregationColumns() {
        return aggregationColumns;
    }

    public void setAggregationColumns(List<AggregationColumn> aggregationColumns) {
        this.aggregationColumns = aggregationColumns;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public MergeContext()
    {

    }

    /**
     * 获取结果集类型
     */
    public ResultSetType getResultSetType() {
        if (!groupByColumns.isEmpty()) {
            return ResultSetType.GroupBy;
        }
        if (!aggregationColumns.isEmpty()) {
            return ResultSetType.Aggregate;
        }
        if (!orderByColumns.isEmpty()) {
            return ResultSetType.OrderBy;
        }
        return ResultSetType.Iterator;
    }

    /**
     * 结果集类型
     */
    public enum ResultSetType {
        Iterator, OrderBy, Aggregate, GroupBy
    }
}