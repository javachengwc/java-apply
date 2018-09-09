package com.mybatis.pseudocode.mybatis.transaction;

import com.mybatis.pseudocode.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public abstract interface TransactionFactory
{
    public abstract void setProperties(Properties props);

    public abstract Transaction newTransaction(Connection conn);

    public abstract Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit);
}
