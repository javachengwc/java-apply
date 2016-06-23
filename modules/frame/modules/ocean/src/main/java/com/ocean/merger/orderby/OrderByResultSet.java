package com.ocean.merger.orderby;

import com.ocean.jdbc.ShardResultSet;
import com.ocean.merger.MergeContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 排序结果集处理
 */
public class OrderByResultSet extends ShardResultSet {

    private List<OrderByColumn> orderByColumns;

    private List<ResultSet> effectivedResultSets;

    private boolean initial;

    public OrderByResultSet(List<ResultSet> resultSets, MergeContext mergeContext) {
        super(resultSets, mergeContext.getLimit());
        orderByColumns = mergeContext.getOrderByColumns();
        effectivedResultSets = new ArrayList<ResultSet>(resultSets.size());
    }

    public List<OrderByColumn> getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(List<OrderByColumn> orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    public List<ResultSet> getEffectivedResultSets() {
        return effectivedResultSets;
    }

    public void setEffectivedResultSets(List<ResultSet> effectivedResultSets) {
        this.effectivedResultSets = effectivedResultSets;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    @Override
    public boolean nextForShard() throws SQLException {
        if (!initial) {
            initialEffectivedResultSets();
        } else {
            nextEffectivedResultSets();
        }
        OrderByValue choosenOrderByValue = null;
        for (ResultSet each : effectivedResultSets) {
            OrderByValue eachOrderByValue = new OrderByValue(orderByColumns, each);
            if (null == choosenOrderByValue || choosenOrderByValue.compareTo(eachOrderByValue) > 0) {
                choosenOrderByValue = eachOrderByValue;
                setCurrentResultSet(each);
            }
        }
        return !effectivedResultSets.isEmpty();
    }

    private void initialEffectivedResultSets() throws SQLException {
        for (ResultSet each : getResultSets()) {
            if (each.next()) {
                effectivedResultSets.add(each);
            }
        }
        initial = true;
    }

    private void nextEffectivedResultSets() throws SQLException {
        boolean next = getCurrentResultSet().next();
        if (!next) {
            effectivedResultSets.remove(getCurrentResultSet());
        }
    }
}