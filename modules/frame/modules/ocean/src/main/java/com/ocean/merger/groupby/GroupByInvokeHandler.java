package com.ocean.merger.groupby;

import com.ocean.merger.AbstractMergerInvokeHandler;
import com.ocean.merger.resultset.ResultSetQueryIndex;
import com.ocean.merger.resultset.ResultSetUtil;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 分组函数动态代理
 */
public class GroupByInvokeHandler extends AbstractMergerInvokeHandler<GroupByResultSet> {

    public GroupByInvokeHandler(final GroupByResultSet groupByResultSet) {
        super(groupByResultSet);
    }

    @Override
    protected Object doMerge(GroupByResultSet groupByResultSet, Method method, ResultSetQueryIndex resultSetQueryIndex) throws ReflectiveOperationException, SQLException {
        return ResultSetUtil.convertValue(groupByResultSet.getCurrentGroupByResultSet().getValue(resultSetQueryIndex), method.getReturnType());
    }
}