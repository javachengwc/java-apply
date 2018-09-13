package com.mybatis.pseudocode.mybatisspring.transaction;

import com.mybatis.pseudocode.mybatis.session.TransactionIsolationLevel;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import com.mybatis.pseudocode.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class SpringManagedTransactionFactory implements TransactionFactory
{
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit)
    {
        return new SpringManagedTransaction(dataSource);
    }

    public Transaction newTransaction(Connection conn)
    {
        throw new UnsupportedOperationException("New Spring transactions require a DataSource");
    }

    public void setProperties(Properties props)
    {
    }
}
