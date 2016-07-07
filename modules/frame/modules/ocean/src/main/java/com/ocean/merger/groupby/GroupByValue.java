package com.ocean.merger.groupby;

import com.ocean.merger.aggregation.AggregationValue;
import com.ocean.merger.orderby.OrderByColumn;
import com.ocean.merger.resultset.ResultSetQueryIndex;
import com.ocean.merger.resultset.ResultSetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分组结果集数据存储对象
 */
public class GroupByValue implements AggregationValue, Comparable<GroupByValue> {

    private static Logger logger = LoggerFactory.getLogger(GroupByValue.class);

    //具体某行各列按照序号的值,key为序号，value为列值
    private Map<Integer, Comparable<?>> indexMap = new LinkedHashMap<Integer, Comparable<?>>();

    //具体某行各列的值，key为列名，value为列值
    private Map<String, Comparable<?>> columnLabelMap = new LinkedHashMap<String, Comparable<?>>();

    //排序列
    private List<OrderByColumn> orderColumns = new ArrayList<OrderByColumn>();

    //分组列
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
        if (orderColumns==null || orderColumns.isEmpty()) {
            return compareFromGroupByColumns(other);
        }
        return compareFromOrderByColumns(other);
    }

    private int compareFromGroupByColumns(final GroupByValue other) {

        logger.info("GroupByValue compareFromGroupByColumns start");

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

    public String toString()
    {
        StringBuffer buf = new StringBuffer("");
        int indexMapCnt = (indexMap==null)?0:indexMap.size();
        buf.append("indexMap:count="+indexMapCnt).append("\r\n");
        if(indexMapCnt>0)
        {
            buf.append("indexMap:data=").append("\r\n");
            for(Integer key:indexMap.keySet())
            {
                buf.append(key).append(":").append(indexMap.get(key)).append("\r\n");
            }
        }
        int columnLabelCnt = (columnLabelMap==null)?0:columnLabelMap.size();
        buf.append("columnLabelMap:count="+columnLabelCnt).append("\r\n");
        if(columnLabelCnt>0)
        {
            buf.append("columnLabelMap:data=").append("\r\n");
            for(String key:columnLabelMap.keySet())
            {
                buf.append(key).append(":").append(columnLabelMap.get(key)).append("\r\n");
            }
        }
        int  orderColumnsCnt = (orderColumns==null)?0:orderColumns.size();
        buf.append("orderColumns:count="+orderColumnsCnt).append("\r\n");
        if(orderColumnsCnt>0)
        {
            buf.append("orderColumns:data=").append("\r\n");
            for(OrderByColumn per:orderColumns)
            {
                buf.append(per.toString()).append("\r\n");
            }
        }
        int  groupByColumnsCnt = (groupByColumns==null)?0:groupByColumns.size();
        buf.append("groupByColumns:count="+groupByColumnsCnt).append("\r\n");
        if(groupByColumnsCnt>0)
        {
            buf.append("groupByColumns:data=").append("\r\n");
            for(GroupByColumn per:groupByColumns)
            {
                buf.append(per.toString()).append("\r\n");
            }
        }
        return buf.toString();
    }
}