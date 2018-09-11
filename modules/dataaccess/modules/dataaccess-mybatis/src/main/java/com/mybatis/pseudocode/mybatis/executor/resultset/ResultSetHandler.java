package com.mybatis.pseudocode.mybatis.executor.resultset;

import com.mybatis.pseudocode.mybatis.cursor.Cursor;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

//责将JDBC返回的ResultSet结果集对象转换成List类型的集合
public abstract interface ResultSetHandler
{
    //处理结果
    public abstract <E> List<E> handleResultSets(Statement statement) throws SQLException;

    public abstract <E> Cursor<E> handleCursorResultSets(Statement statement) throws SQLException;

    public abstract void handleOutputParameters(CallableStatement callableStatement) throws SQLException;
}
