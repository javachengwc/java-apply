package com.mybatis.pseudocode.mybatis.transaction.jdbc;

import com.mybatis.pseudocode.mybatis.session.TransactionIsolationLevel;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import com.mybatis.pseudocode.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class JdbcTransactionFactory implements TransactionFactory
{
    public void setProperties(Properties props)
    {
    }

    public Transaction newTransaction(Connection conn)
    {
        return new JdbcTransaction(conn);
    }

    public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit)
    {
        return new JdbcTransaction(ds, level, autoCommit);
    }
}
