package com.mybatis.pseudocode.mybatis.transaction;


import java.sql.Connection;
import java.sql.SQLException;

public abstract interface Transaction
{
    public abstract Connection getConnection() throws SQLException;

    public abstract void commit() throws SQLException;

    public abstract void rollback() throws SQLException;

    public abstract void close() throws SQLException;

    public abstract Integer getTimeout() throws SQLException;
}
