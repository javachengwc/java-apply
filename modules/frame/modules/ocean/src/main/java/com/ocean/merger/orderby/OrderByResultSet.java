package com.ocean.merger.orderby;

import com.ocean.jdbc.ShardResultSet;
import com.ocean.merger.MergeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 排序结果集处理
 * 排序查询是先分别在各库中排序查询，
 * 当程序调用result.next迭代结果时再在程序中按照排序字段排序输出按照顺序对应的结果
 * 当查询语句是排序后再有limit语句来限定数量的时候，
 * 程序会改写slq的条件从各库中查询出所有前limit+n的数据，再在程序中截取按照排序的对应片段的数据
 */
public class OrderByResultSet extends ShardResultSet {

    private List<OrderByColumn> orderByColumns;

    private List<ResultSet> effectivedResultSets;

    private boolean initial=false;

    public OrderByResultSet(List<ResultSet> resultSets, MergeContext mergeContext) {
        super(resultSets, mergeContext.getLimit());
        orderByColumns = mergeContext.getOrderByColumns();
        effectivedResultSets = new ArrayList<ResultSet>(resultSets.size());
    }


    @Override
    public boolean nextForShard() throws SQLException {
        logger.info("OrderByResultSet nextForShard start.");
        if (!initial) {
            initialEffectivedResultSets();
        } else {
            nextEffectivedResultSets();
        }
        OrderByValue choosenOrderByValue = null;
        //在结果集列表中找出orderByValue最靠前的结果集做为当前结果集
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
        logger.info("OrderByResultSet initialEffectivedResultSets 把结果集放到effectivedResultSets中");
        for (ResultSet each : getResultSets()) {
            if (each.next()) {
                effectivedResultSets.add(each);
            }
        }
        initial = true;
    }

    private void nextEffectivedResultSets() throws SQLException {
        logger.info("OrderByResultSet nextEffectivedResultSets 迭代当前结果集数据，如果没有数据，把当前结果集从effectivedResultSets中移除");
        boolean next = getCurrentResultSet().next();
        if (!next) {
            effectivedResultSets.remove(getCurrentResultSet());
        }
    }
}