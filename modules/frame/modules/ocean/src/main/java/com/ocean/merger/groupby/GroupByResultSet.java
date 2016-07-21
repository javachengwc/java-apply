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
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 分组结果集
 */
public class GroupByResultSet extends ShardResultSet {

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

        System.out.println("GroupByResultSet create");
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
        logger.info("GroupByResultSet nextForShard... ");
        if (null == groupByResultIterator) {
            logger.info("GroupByResultSet groupByResultIterator is null ");
            ResultSetUtil.fillIndexesForDerivedAggregationColumns(getResultSets().iterator().next(), aggregationColumns);
            groupByResultIterator = reduce(map()).iterator();
        }
        if (groupByResultIterator.hasNext()) {
            logger.info("GroupByResultSet groupByResultIterator hasNext... ");
            currentGroupByResultSet = groupByResultIterator.next();
            return true;
        } else {
            return false;
        }
    }

    private Map<GroupByKey, List<GroupByValue>> map() throws SQLException {

        logger.info("GroupByResultSet map  data start... ");
        ExecuteUnit<ResultSet, Map<GroupByKey, GroupByValue>> executeUnit = new ExecuteUnit<ResultSet, Map<GroupByKey, GroupByValue>>() {

            //查询
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

        //分组
        MergeUnit<Map<GroupByKey, GroupByValue>, Map<GroupByKey, List<GroupByValue>>> mergeUnit = new MergeUnit<Map<GroupByKey, GroupByValue>, Map<GroupByKey, List<GroupByValue>>>() {

            @Override
            public Map<GroupByKey, List<GroupByValue>> merge(List<Map<GroupByKey, GroupByValue>> values) {
                Map<GroupByKey, List<GroupByValue>> result = new HashMap<GroupByKey, List<GroupByValue>>();
                for (Map<GroupByKey, GroupByValue> each : values) {
                    for (Map.Entry<GroupByKey, GroupByValue> entry : each.entrySet()) {
                        GroupByKey key = entry.getKey();
                        List<GroupByValue> list = result.get(key);
                        if(list==null)
                        {
                            list = new ArrayList<GroupByValue>();
                            result.put(key,list);
                        }
                        list.add(entry.getValue());
                    }
                }
                return result;
            }
        };
        Map<GroupByKey, List<GroupByValue>> result = ExecutorEngine.execute(getResultSets(), executeUnit, mergeUnit);
        int  cnt = (result==null)?0:result.size();
        logger.info("GroupByResultSet map  end, result count="+cnt);
        printMapResult(result);
        return result;
    }

    public void printMapResult(  Map<GroupByKey, List<GroupByValue>> result)
    {
        StringBuffer buf = new StringBuffer("");
        int  cnt = (result==null)?0:result.size();
        buf.append("GroupByResultSet printMapResult result:count="+cnt).append("\r\n");
        if(cnt>0)
        {
            buf.append("GroupByResultSet printMapResult result:data=").append("\r\n");
            for(GroupByKey key:result.keySet())
            {
                List<String> as= key.getUnionKey();
                int keyCnt = (as==null)?0:as.size();
                buf.append("key: keyCnt="+keyCnt+",vlu="+list2Line(as)).append("\r\n");
                Collection<GroupByValue> vlu=result.get(key);
                buf.append("keyValue:").append(vlu.toString()).append("\r\n");
                buf.append("---------------------------------------").append("\r\n");
            }
        }
        String str=buf.toString();
        logger.info("GroupByResultSet map result-->\r\n"+str);
    }

    public String list2Line( List<String>  values)
    {
        if(values==null || values.size()<=0)
        {
            return "";
        }
        StringBuffer buf = new StringBuffer("");
        for(String per:values)
        {
            buf.append(per).append(",");
        }
        return buf.toString();
    }

    private Collection<GroupByValue> reduce(Map<GroupByKey, List<GroupByValue>>  mappedResult) throws SQLException {

        //列数
        int columnLabelsCnt =(columnLabels==null)?0:columnLabels.size();
        //结果数
        int mappedResultCnt=(mappedResult==null)?0:mappedResult.size();
        logger.info("GroupByResultSet reduce start,columnLabels count="+columnLabelsCnt+",mappedResult count="+mappedResultCnt);

        List<GroupByValue> result = new ArrayList<GroupByValue>();
        //组装结果数据
        for (GroupByKey key : mappedResult.keySet()) {
            Collection<GroupByValue> each = mappedResult.get(key);
            GroupByValue reduceResult = new GroupByValue();
            for (int i = 0; i < columnLabelsCnt; i++) {
                int index = i + 1;
                Optional<AggregationColumn> aggregationColumn = findAggregationColumn(index);
                Comparable<?> value = null;
                if (aggregationColumn.isPresent()) {
                    value = aggregate(aggregationColumn.get(), index, each);
                }
                if(value==null)
                {
                    value=each.iterator().next().getValue(new ResultSetQueryIndex(index));
                }
                //设置每列值(列下标，列名，列值)
                reduceResult.put(index, columnLabels.get(i), value);
            }

            if (orderByColumns==null || orderByColumns.isEmpty()) {
                reduceResult.addGroupByColumns(groupByColumns);
            } else {
                reduceResult.addOrderColumns(orderByColumns);
            }

            result.add(reduceResult);
        }
        Collections.sort(result);

        int rtCnt = (result==null)?0:result.size();
        logger.info("GroupByResultSet reduced result count="+rtCnt);
        printReduceResult(result);

        return result;
    }

    public void printReduceResult(Collection<GroupByValue> result)
    {
        StringBuffer buf = new StringBuffer("");
        int  cnt = (result==null)?0:result.size();
        buf.append("result:count="+cnt).append("\r\n");
        if(cnt>0)
        {
            buf.append("result:data=").append("\r\n");
            for(GroupByValue per:result)
            {
                buf.append(per.toString()).append("\r\n");
            }
        }
        String str=buf.toString();
        logger.info("GroupByResultSet reduced result-->"+str);
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