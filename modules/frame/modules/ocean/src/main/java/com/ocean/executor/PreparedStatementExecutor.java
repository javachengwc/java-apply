package com.ocean.executor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 预编译语句对象请求的执行器
 */
public class PreparedStatementExecutor {

    private Collection<PreparedStatement> preparedStatements;

    /**
     * 执行SQL查询
     */
    public List<ResultSet> executeQuery() throws SQLException {
        List<ResultSet> result;
        if (1 == preparedStatements.size()) {
            result =  Arrays.asList(preparedStatements.iterator().next().executeQuery());
            return result;
        }
        result = ExecutorEngine.execute(preparedStatements, new ExecuteUnit<PreparedStatement, ResultSet>() {

            @Override
            public ResultSet execute(final PreparedStatement input) throws Exception {
                return input.executeQuery();
            }
        });
        return result;
    }

    /**
     * 执行SQL更新
     */
    public int executeUpdate() throws SQLException {
        int result;
        if (1 == preparedStatements.size()) {
            result =  preparedStatements.iterator().next().executeUpdate();
            return result;
        }
        result = ExecutorEngine.execute(preparedStatements, new ExecuteUnit<PreparedStatement, Integer>() {

            @Override
            public Integer execute(final PreparedStatement input) throws Exception {
                return input.executeUpdate();
            }
        }, new MergeUnit<Integer, Integer>() {

            @Override
            public Integer merge(final List<Integer> results) {
                int result = 0;
                for (Integer each : results) {
                    result += each;
                }
                return result;
            }
        });
        return result;
    }

    /**
     * 执行SQL请求
     */
    public boolean execute() throws SQLException {
        if (1 == preparedStatements.size()) {
            boolean result = preparedStatements.iterator().next().execute();
            return result;
        }
        List<Boolean> result = ExecutorEngine.execute(preparedStatements, new ExecuteUnit<PreparedStatement, Boolean>() {
            @Override
            public Boolean execute(final PreparedStatement input) throws Exception {
                return input.execute();
            }
        });
        return result.get(0);
    }
}