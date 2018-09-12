package com.mybatis.pseudocode.mybatis.transaction.jdbc;

import com.mybatis.pseudocode.mybatis.session.TransactionIsolationLevel;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//使用JDBC的事务管理机制,利用java.sql.Connection对象完成对事务的提交（commit()）、回滚（rollback()）、关闭（close()
public class JdbcTransaction implements Transaction
{
    private static final Log log = LogFactory.getLog(JdbcTransaction.class);
    protected Connection connection;
    protected DataSource dataSource;
    protected TransactionIsolationLevel level;
    protected boolean autoCommmit;

    public JdbcTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit)
    {
        this.dataSource = ds;
        this.level = desiredLevel;
        this.autoCommmit = desiredAutoCommit;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() throws SQLException
    {
        if (this.connection == null) {
            openConnection();
        }
        return this.connection;
    }

    public void commit() throws SQLException
    {
        if ((this.connection != null) && (!this.connection.getAutoCommit())) {
            if (log.isDebugEnabled()) {
                log.debug("Committing JDBC Connection [" + this.connection + "]");
            }
            this.connection.commit();
        }
    }

    public void rollback() throws SQLException
    {
        if ((this.connection != null) && (!this.connection.getAutoCommit())) {
            if (log.isDebugEnabled()) {
                log.debug("Rolling back JDBC Connection [" + this.connection + "]");
            }
            this.connection.rollback();
        }
    }

    public void close() throws SQLException
    {
        if (this.connection != null) {
            resetAutoCommit();
            if (log.isDebugEnabled()) {
                log.debug("Closing JDBC Connection [" + this.connection + "]");
            }
            this.connection.close();
        }
    }

    protected void setDesiredAutoCommit(boolean desiredAutoCommit)  throws SQLException {
        if (this.connection.getAutoCommit() != desiredAutoCommit) {
            if (log.isDebugEnabled()) {
                log.debug("Setting autocommit to " + desiredAutoCommit + " on JDBC Connection [" + this.connection + "]");
            }
            this.connection.setAutoCommit(desiredAutoCommit);
        }
    }

    protected void resetAutoCommit()
    {
        try
        {
            if (!this.connection.getAutoCommit())
            {
                if (log.isDebugEnabled()) {
                    log.debug("Resetting autocommit to true on JDBC Connection [" + this.connection + "]");
                }
                this.connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            if (log.isDebugEnabled())
                log.debug("Error resetting autocommit to true before closing the connection.  Cause: " + e);
        }
    }

    protected void openConnection() throws SQLException
    {
        if (log.isDebugEnabled()) {
            log.debug("Opening JDBC Connection");
        }
        this.connection = this.dataSource.getConnection();
        if (this.level != null) {
            this.connection.setTransactionIsolation(this.level.getLevel());
        }
        setDesiredAutoCommit(this.autoCommmit);
    }

    public Integer getTimeout() throws SQLException
    {
        return null;
    }
}
