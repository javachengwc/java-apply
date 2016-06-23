package com.ocean.merger.resultset;

import com.google.common.base.Preconditions;
import com.ocean.exception.ShardException;
import com.ocean.merger.aggregation.AggregationColumn;
import com.ocean.merger.groupby.GroupByColumn;
import com.ocean.merger.orderby.OrderByColumn;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 结果集处理工具
 */
public class ResultSetUtil {

    /**
     * 从结果集中提取结果值
     */
    public static Object getValue(GroupByColumn groupByColumn, ResultSet resultSet) throws SQLException {
        Object result = getValue(groupByColumn.getAlias(), resultSet);
        Preconditions.checkNotNull(result);
        return result;
    }

    /**
     * 从结果集中提取结果值
     */
    public static Object getValue(OrderByColumn orderByColumn, ResultSet resultSet) throws SQLException {
        Object result;
        if (orderByColumn.getIndex().isPresent()) {
            result = resultSet.getObject(orderByColumn.getIndex().get());
        } else {
            result = getValue(orderByColumn.getName().get(), resultSet);
        }
        Preconditions.checkNotNull(result);
        return result;
    }

    private static Object getValue(final String columnName, final ResultSet resultSet) throws SQLException {
        Object result = resultSet.getObject(columnName);
        if (null == result) {
            result = resultSet.getObject(columnName.toUpperCase());
        }
        if (null == result) {
            result = resultSet.getObject(columnName.toLowerCase());
        }
        return result;
    }

    /**
     * 根据返回值类型返回特定类型的结果
     */
    public static Object convertValue(final Object value, final Class<?> convertType) {
        if (value instanceof Number) {
            return convertNumberValue(value, convertType);
        } else {
            if (String.class.equals(convertType)) {
                return value.toString();
            } else {
                return value;
            }
        }
    }

    private static Object convertNumberValue(Object value,Class<?> convertType) {
        Number number = (Number) value;
        String clazzName =convertType.getSimpleName();
        if("int".equals(clazzName) || "Integer".equals(clazzName))
        {
            return number.intValue();
        }
        if("long".equals(clazzName) || "Long".equals(clazzName))
        {
            return number.longValue();
        }
        if("double".equals(clazzName) || "Double".equals(clazzName))
        {
            return  number.doubleValue();
        }
        if("float".equals(clazzName) || "Float".equals(clazzName))
        {
            return number.floatValue();
        }
        if("BigDecimal".equals(clazzName) )
        {
            if (number instanceof BigDecimal) {
                return number;
            } else {
                return new BigDecimal(number.toString());
            }
        }
        throw new ShardException("Unsupported data type:%s", convertType);
    }

    /**
     * 根据排序类型比较大小.
     *
     * @param thisValue 待比较的值
     * @param otherValue 待比较的值
     * @param orderByType 排序类型
     * @return 负数，零和正数分别表示小于，等于和大于
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static int compareTo(final Comparable thisValue, final Comparable otherValue, final OrderByColumn.OrderByType orderByType) {
        return OrderByColumn.OrderByType.ASC == orderByType ? thisValue.compareTo(otherValue) : -thisValue.compareTo(otherValue);
    }

    /**
     * 向聚合列的补列填充索引值
     */
    public static void fillIndexesForDerivedAggregationColumns(final ResultSet resultSet, final Collection<AggregationColumn> aggregationColumns) throws SQLException {
        for (AggregationColumn aggregationColumn : aggregationColumns) {
            for (AggregationColumn derivedColumn : aggregationColumn.getDerivedColumns()) {
                derivedColumn.setIndex(resultSet.findColumn(derivedColumn.getAlias().get()));
            }
        }
    }
}

