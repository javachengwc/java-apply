package com.mybatis.pseudocode.mybatis.executor.statement;


import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.executor.kengen.KeyGenerator;
import com.mybatis.pseudocode.mybatis.mapping.BoundSql;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PreparedStatementHandler extends BaseStatementHandler
{
    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter,
                                    RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql)
    {
        super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    public int update(Statement statement) throws SQLException
    {
        PreparedStatement ps = (PreparedStatement)statement;
        ps.execute();
        int rows = ps.getUpdateCount();
        Object parameterObject = this.boundSql.getParameterObject();
        KeyGenerator keyGenerator = this.mappedStatement.getKeyGenerator();
        keyGenerator.processAfter(this.executor, this.mappedStatement, ps, parameterObject);
        return rows;
    }

    public void batch(Statement statement) throws SQLException
    {
        PreparedStatement ps = (PreparedStatement)statement;
        ps.addBatch();
    }

    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException
    {
        PreparedStatement ps = (PreparedStatement)statement;
        //执行SQL查询操作
        ps.execute();
        //结果交给resultSetHandler来处理
        return this.resultSetHandler.handleResultSets(ps);
    }

    public <E> Cursor<E> queryCursor(Statement statement) throws SQLException
    {
        PreparedStatement ps = (PreparedStatement)statement;
        ps.execute();
        return this.resultSetHandler.handleCursorResultSets(ps);
    }

    protected Statement instantiateStatement(Connection connection) throws SQLException
    {
        String sql = this.boundSql.getSql();
        if ((this.mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator)) {
            String[] keyColumnNames = this.mappedStatement.getKeyColumns();
            if (keyColumnNames == null) {
                return connection.prepareStatement(sql, 1);
            }
            return connection.prepareStatement(sql, keyColumnNames);
        }
        if (this.mappedStatement.getResultSetType() != null) {
            return connection.prepareStatement(sql, this.mappedStatement.getResultSetType().getValue(), 1007);
        }
        return connection.prepareStatement(sql);
    }

    //设置sql参数
    public void parameterize(Statement statement) throws SQLException
    {
        this.parameterHandler.setParameters((PreparedStatement)statement);
    }
}
