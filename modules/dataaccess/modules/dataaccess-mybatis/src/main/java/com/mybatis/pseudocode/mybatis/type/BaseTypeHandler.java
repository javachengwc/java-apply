package com.mybatis.pseudocode.mybatis.type;


import com.mybatis.pseudocode.mybatis.executor.result.ResultMapException;
import com.mybatis.pseudocode.mybatis.mapping.JdbcType;
import com.mybatis.pseudocode.mybatis.session.Configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseTypeHandler<T> extends TypeReference<T> implements TypeHandler<T>
{
    protected Configuration configuration;

    public void setConfiguration(Configuration c)
    {
        this.configuration = c;
    }

    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException
    {
        if (parameter == null) {
            if (jdbcType == null)
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            try
            {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " ." +
                        " Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. Cause: " + e, e);
            }
        }
        else
        {
            try {
                setNonNullParameter(ps, i, parameter, jdbcType);
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " ." +
                        " Try setting a different JdbcType for this parameter or a different configuration property. Cause: " + e, e);
            }
        }
    }

    public T getResult(ResultSet rs, String columnName) throws SQLException
    {
        T result;
        try
        {
            result = getNullableResult(rs, columnName);
        }
        catch (Exception e)
        {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + e, e);
        }

        if (rs.wasNull()) {
            return null;
        }
        return result;
    }

    public T getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        T result;
        try
        {
            result = getNullableResult(rs, columnIndex);
        }
        catch (Exception e)
        {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        }
        return result;
    }

    public T getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        T result;
        try
        {
            result = getNullableResult(cs, columnIndex);
        }
        catch (Exception e)
        {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
        if (cs.wasNull()) {
            return null;
        }
        return result;
    }

    public abstract void setNonNullParameter(PreparedStatement preparedStatement, int paramInt, T t, JdbcType jdbcType) throws SQLException;

    public abstract T getNullableResult(ResultSet resultSet, String paramString) throws SQLException;

    public abstract T getNullableResult(ResultSet resultSet, int paramInt) throws SQLException;

    public abstract T getNullableResult(CallableStatement pcallableStatement, int paramInt) throws SQLException;
}
