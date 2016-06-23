package com.ocean.merger.orderby;

import com.google.common.base.Preconditions;
import com.ocean.merger.resultset.ResultSetUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于结果集的排序对象
 */
public class OrderByValue  implements Comparable<OrderByValue> {

    private List<OrderByColumn> orderByColumns;

    private Value orderByValue;

    public OrderByValue()
    {

    }

    public OrderByValue(final List<OrderByColumn> orderByColumns, final ResultSet resultSet) throws SQLException {
        this.orderByColumns = orderByColumns;
        orderByValue = new Value(orderByColumns, getValues(resultSet));
    }

    public List<OrderByColumn> getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(List<OrderByColumn> orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    public Value getOrderByValue() {
        return orderByValue;
    }

    public void setOrderByValue(Value orderByValue) {
        this.orderByValue = orderByValue;
    }

    private List<Comparable<?>> getValues(final ResultSet resultSet) throws SQLException {
        List<Comparable<?>> result = new ArrayList<Comparable<?>>(orderByColumns.size());
        for (OrderByColumn each : orderByColumns) {
            Object value = ResultSetUtil.getValue(each, resultSet);
            Preconditions.checkState(value instanceof Comparable, "Sharding-JDBC: order by value must extends Comparable");
            result.add((Comparable<?>) value);
        }
        return result;
    }

    @Override
    public int compareTo(final OrderByValue otherOrderByValue) {
        return orderByValue.compareTo(otherOrderByValue.orderByValue);
    }

    public static final class Value implements Comparable<Value> {

        private List<OrderByColumn> orderByColumns;

        private List<Comparable<?>> values;

        public Value( List<OrderByColumn> orderByColumns,List<Comparable<?>> values)
        {
            this.orderByColumns=orderByColumns;
            this.values=values;
        }

        @Override
        public int compareTo(final Value otherOrderByValue) {
            for (int i = 0; i < orderByColumns.size(); i++) {
                OrderByColumn thisOrderByColumn = orderByColumns.get(i);
                int result = ResultSetUtil.compareTo(values.get(i), otherOrderByValue.values.get(i), thisOrderByColumn.getOrderByType());
                if (0 != result) {
                    return result;
                }
            }
            return 0;
        }
    }
}
