package com.mybatis.pseudocode.mybatis.executor.statement;


import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.executor.ExecutorException;
import com.mybatis.pseudocode.mybatis.executor.parameter.ParameterHandler;
import com.mybatis.pseudocode.mybatis.mapping.BoundSql;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RoutingStatementHandler implements StatementHandler
{
    private final StatementHandler delegate;

    public RoutingStatementHandler(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql)
    {
        int a=1;
        //switch (1.$SwitchMap$org$apache$ibatis$mapping$StatementType[ms.getStatementType().ordinal()]) {
        switch (a) {
//        case 1:
//            this.delegate = new SimpleStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
//            break;
        case 2:
            this.delegate = new PreparedStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
            break;
//        case 3:
//            this.delegate = new CallableStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
//            break;
        default:
            throw new ExecutorException("Unknown statement type: " + ms.getStatementType());
    }
    }

    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException
    {
        return this.delegate.prepare(connection, transactionTimeout);
    }

    public void parameterize(Statement statement) throws SQLException
    {
        this.delegate.parameterize(statement);
    }

    public void batch(Statement statement) throws SQLException
    {
        this.delegate.batch(statement);
    }

    public int update(Statement statement) throws SQLException
    {
        return this.delegate.update(statement);
    }

    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException
    {
        return this.delegate.query(statement, resultHandler);
    }

    public <E> Cursor<E> queryCursor(Statement statement) throws SQLException
    {
        return this.delegate.queryCursor(statement);
    }

    public BoundSql getBoundSql()
    {
        return this.delegate.getBoundSql();
    }

    public ParameterHandler getParameterHandler()
    {
        return this.delegate.getParameterHandler();
    }
}
