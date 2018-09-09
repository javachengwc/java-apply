package com.mybatis.pseudocode.mybatis.executor.statement;


import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.executor.ExecutorException;
import com.mybatis.pseudocode.mybatis.executor.kengen.KeyGenerator;
import com.mybatis.pseudocode.mybatis.executor.parameter.ParameterHandler;
import com.mybatis.pseudocode.mybatis.executor.resultset.ResultSetHandler;
import com.mybatis.pseudocode.mybatis.mapping.BoundSql;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;
import org.apache.ibatis.executor.statement.StatementUtil;
import org.apache.ibatis.reflection.factory.ObjectFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandler implements StatementHandler
{
    protected final Configuration configuration;
    protected final ObjectFactory objectFactory;
    protected final TypeHandlerRegistry typeHandlerRegistry;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;
    protected final RowBounds rowBounds;
    protected BoundSql boundSql;

    protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject,
                                   RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql)
    {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;

        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
        this.objectFactory = this.configuration.getObjectFactory();

        if (boundSql == null) {
            generateKeys(parameterObject);
            boundSql = mappedStatement.getBoundSql(parameterObject);
        }

        this.boundSql = boundSql;

        this.parameterHandler = this.configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        this.resultSetHandler = this.configuration.newResultSetHandler(executor, mappedStatement, rowBounds, this.parameterHandler, resultHandler, boundSql);
    }

    public BoundSql getBoundSql()
    {
        return this.boundSql;
    }

    public ParameterHandler getParameterHandler()
    {
        return this.parameterHandler;
    }

    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException
    {
        //ErrorContext.instance().sql(this.boundSql.getSql());
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            setStatementTimeout(statement, transactionTimeout);
            setFetchSize(statement);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw e;
        } catch (Exception e) {
            closeStatement(statement);
            throw new ExecutorException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection paramConnection) throws SQLException;

    protected void setStatementTimeout(Statement stmt, Integer transactionTimeout) throws SQLException {
        Integer queryTimeout = null;
        if (this.mappedStatement.getTimeout() != null)
            queryTimeout = this.mappedStatement.getTimeout();
        else if (this.configuration.getDefaultStatementTimeout() != null) {
            queryTimeout = this.configuration.getDefaultStatementTimeout();
        }
        if (queryTimeout != null) {
            stmt.setQueryTimeout(queryTimeout.intValue());
        }
        StatementUtil.applyTransactionTimeout(stmt, queryTimeout, transactionTimeout);
    }

    protected void setFetchSize(Statement stmt) throws SQLException {
        Integer fetchSize = this.mappedStatement.getFetchSize();
        if (fetchSize != null) {
            stmt.setFetchSize(fetchSize.intValue());
            return;
        }
        Integer defaultFetchSize = this.configuration.getDefaultFetchSize();
        if (defaultFetchSize != null)
            stmt.setFetchSize(defaultFetchSize.intValue());
    }

    protected void closeStatement(Statement statement)
    {
        try {
            if (statement != null)
                statement.close();
        }
        catch (SQLException localSQLException)
        {
        }
    }

    protected void generateKeys(Object parameter) {
        KeyGenerator keyGenerator = this.mappedStatement.getKeyGenerator();
        //ErrorContext.instance().store();
        keyGenerator.processBefore(this.executor, this.mappedStatement, null, parameter);
        //ErrorContext.instance().recall();
    }
}
