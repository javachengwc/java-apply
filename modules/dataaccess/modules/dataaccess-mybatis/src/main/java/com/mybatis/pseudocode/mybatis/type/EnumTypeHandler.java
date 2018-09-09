package com.mybatis.pseudocode.mybatis.type;

import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E>
{
    private final Class<E> type;

    public EnumTypeHandler(Class<E> type)
    {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException
    {
        if (jdbcType == null)
            ps.setString(i, parameter.name());
        else
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE);
    }

    public E getNullableResult(ResultSet rs, String columnName) throws SQLException
    {
        String s = rs.getString(columnName);
        return s == null ? null : Enum.valueOf(this.type, s);
    }

    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException
    {
        String s = rs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(this.type, s);
    }

    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        String s = cs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(this.type, s);
    }
}
