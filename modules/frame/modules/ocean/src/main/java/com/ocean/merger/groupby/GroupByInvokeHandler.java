package com.ocean.merger.groupby;

import com.ocean.merger.AbstractMergerInvokeHandler;
import com.ocean.merger.resultset.ResultSetQueryIndex;
import com.ocean.merger.resultset.ResultSetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 分组函数动态代理
 */
public class GroupByInvokeHandler extends AbstractMergerInvokeHandler<GroupByResultSet> {

    private static Logger logger = LoggerFactory.getLogger(GroupByInvokeHandler.class);

    public GroupByInvokeHandler(final GroupByResultSet groupByResultSet) {
        super(groupByResultSet);
        System.out.println("GroupByInvokeHandler create");
    }

    @Override
    protected Object doMerge(GroupByResultSet groupByResultSet, Method method, ResultSetQueryIndex resultSetQueryIndex) throws ReflectiveOperationException, SQLException {

        logger.info("GroupByInvokeHandler doMerge start.");

        Object value =groupByResultSet.getCurrentGroupByResultSet().getValue(resultSetQueryIndex);
        logger.info("GroupByInvokeHandler doMerge value="+value+",type="+method.getReturnType());
        return ResultSetUtil.convertValue(value, method.getReturnType());
    }
}