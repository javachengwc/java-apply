package com.mybatis.pseudocode.mybatis.transaction.managed;

import com.mybatis.pseudocode.mybatis.session.TransactionIsolationLevel;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//使用MANAGED的事务管理机制,让容器管理事务transaction的整个生命周期
//MyBatis自身不会去实现事务管理，而是让程序的容器如spring来实现对事务的管理,比如SpringManagedTransaction
public class ManagedTransaction implements Transaction
{
    private static final Log log = LogFactory.getLog(ManagedTransaction.class);

    private DataSource dataSource;

    private TransactionIsolationLevel level;

    private Connection connection;

    private final boolean closeConnection;

    public ManagedTransaction(Connection connection, boolean closeConnection)
    {
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    public ManagedTransaction(DataSource ds, TransactionIsolationLevel level, boolean closeConnection) {
        this.dataSource = ds;
        this.level = level;
        this.closeConnection = closeConnection;
    }

    public Connection getConnection() throws SQLException
    {
        if (this.connection == null) {
            openConnection();
        }
        return this.connection;
    }

    public void commit()  throws SQLException
    {
    }

    public void rollback()  throws SQLException
    {
    }

    public void close() throws SQLException
    {
        if ((this.closeConnection) && (this.connection != null)) {
            if (log.isDebugEnabled()) {
                log.debug("Closing JDBC Connection [" + this.connection + "]");
            }
            this.connection.close();
        }
    }

    protected void openConnection() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Opening JDBC Connection");
        }
        this.connection = this.dataSource.getConnection();
        if (this.level != null)
            this.connection.setTransactionIsolation(this.level.getLevel());
    }

    public Integer getTimeout()  throws SQLException
    {
        return null;
    }
}
