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

    //排序列
    private List<OrderByColumn> orderByColumns;

    //排序列值
    private List<Comparable<?>> values;

    public OrderByValue(final List<OrderByColumn> orderByColumns, final ResultSet resultSet) throws SQLException {
        this.orderByColumns = orderByColumns;
        this.values =getValues(resultSet);
    }

    //获取结果集中各个排序列的值
    private List<Comparable<?>> getValues(final ResultSet resultSet) throws SQLException {
        List<Comparable<?>> result = new ArrayList<Comparable<?>>(orderByColumns.size());
        for (OrderByColumn each : orderByColumns) {
            Object value = ResultSetUtil.getValue(each, resultSet);
            Preconditions.checkState(value instanceof Comparable, "order by value must extends Comparable");
            result.add((Comparable<?>) value);
        }
        return result;
    }

    @Override
    public int compareTo(OrderByValue otherOrderByValue) {
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
