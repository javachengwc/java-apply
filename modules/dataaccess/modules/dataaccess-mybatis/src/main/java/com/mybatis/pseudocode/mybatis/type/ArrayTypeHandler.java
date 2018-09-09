package com.mybatis.pseudocode.mybatis.type;

import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.sql.*;

public class ArrayTypeHandler extends BaseTypeHandler<Object>
{
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException
    {
        ps.setArray(i, (Array)parameter);
    }

    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException
    {
        Array array = rs.getArray(columnName);
        return array == null ? null : array.getArray();
    }

    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException
    {
        Array array = rs.getArray(columnIndex);
        return array == null ? null : array.getArray();
    }

    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        Array array = cs.getArray(columnIndex);
        return array == null ? null : array.getArray();
    }
}
