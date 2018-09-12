package com.mybatis.pseudocode.mybatis.transaction.managed;

import com.mybatis.pseudocode.mybatis.session.TransactionIsolationLevel;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import com.mybatis.pseudocode.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class ManagedTransactionFactory implements TransactionFactory
{
    private boolean closeConnection = true;

    public void setProperties(Properties props)
    {
        if (props != null) {
            String closeConnectionProperty = props.getProperty("closeConnection");
            if (closeConnectionProperty != null)
                this.closeConnection = Boolean.valueOf(closeConnectionProperty).booleanValue();
        }
    }

    public Transaction newTransaction(Connection conn)
    {
        return new ManagedTransaction(conn, this.closeConnection);
    }

    public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit)
    {
        return new ManagedTransaction(ds, level, this.closeConnection);
    }
}
