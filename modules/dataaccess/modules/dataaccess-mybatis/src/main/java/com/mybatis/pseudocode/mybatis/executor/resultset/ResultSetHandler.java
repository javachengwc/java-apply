package com.mybatis.pseudocode.mybatis.executor.resultset;

import com.mybatis.pseudocode.mybatis.cursor.Cursor;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract interface ResultSetHandler
{
    public abstract <E> List<E> handleResultSets(Statement statement) throws SQLException;

    public abstract <E> Cursor<E> handleCursorResultSets(Statement statement) throws SQLException;

    public abstract void handleOutputParameters(CallableStatement callableStatement) throws SQLException;
}
