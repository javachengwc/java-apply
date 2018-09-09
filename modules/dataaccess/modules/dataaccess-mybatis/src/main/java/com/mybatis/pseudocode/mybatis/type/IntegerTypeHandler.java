package com.mybatis.pseudocode.mybatis.type;

import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHandler extends BaseTypeHandler<Integer>
{
    public void setNonNullParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException
    {
        ps.setInt(i, parameter.intValue());
    }

    public Integer getNullableResult(ResultSet rs, String columnName) throws SQLException
    {
        return Integer.valueOf(rs.getInt(columnName));
    }

    public Integer getNullableResult(ResultSet rs, int columnIndex) throws SQLException
    {
        return Integer.valueOf(rs.getInt(columnIndex));
    }

    public Integer getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return Integer.valueOf(cs.getInt(columnIndex));
    }
}
