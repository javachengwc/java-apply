package com.mybatis.pseudocode.mybatis.executor;


import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.executor.statement.StatementHandler;
import com.mybatis.pseudocode.mybatis.mapping.BoundSql;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import org.apache.ibatis.logging.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class SimpleExecutor extends BaseExecutor
{
    public SimpleExecutor(Configuration configuration, Transaction transaction)
    {
        super(configuration, transaction);
    }

    public int doUpdate(MappedStatement ms, Object parameter) throws SQLException
    {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
            stmt = prepareStatement(handler, ms.getStatementLog());
            int i = handler.update(stmt);
            return i;
        } finally {
            closeStatement(stmt);
        }
    }

    //具体的查询
    public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException
    {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this.wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
            //准备statment
            stmt = prepareStatement(handler, ms.getStatementLog());
            //StatementHandler来执行具体的查询
            List localList = handler.query(stmt, resultHandler);
            return localList;
        } finally {
            closeStatement(stmt);
        }
    }

    protected <E> Cursor<E> doQueryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds, BoundSql boundSql) throws SQLException
    {
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler(this.wrapper, ms, parameter, rowBounds, null, boundSql);
        Statement stmt = prepareStatement(handler, ms.getStatementLog());
        return handler.queryCursor(stmt);
    }

    public List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException
    {
        return Collections.emptyList();
    }

    //准备statment
    private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException
    {
        //打开连接
        Connection connection = getConnection(statementLog);
        //创建statment
        Statement stmt = handler.prepare(connection, this.transaction.getTimeout());
        //sql参数设置
        handler.parameterize(stmt);
        return stmt;
    }
}
