package com.mybatis.pseudocode.mybatis.type;

import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UnknownTypeHandler extends BaseTypeHandler<Object>
{
    private static final ObjectTypeHandler OBJECT_TYPE_HANDLER = new ObjectTypeHandler();
    private TypeHandlerRegistry typeHandlerRegistry;

    public UnknownTypeHandler(TypeHandlerRegistry typeHandlerRegistry)
    {
        this.typeHandlerRegistry = typeHandlerRegistry;
    }

    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException
    {
        TypeHandler handler = resolveTypeHandler(parameter, jdbcType);
        handler.setParameter(ps, i, parameter, jdbcType);
    }

    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException
    {
        TypeHandler handler = resolveTypeHandler(rs, columnName);
        return handler.getResult(rs, columnName);
    }

    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException
    {
        TypeHandler handler = resolveTypeHandler(rs.getMetaData(), Integer.valueOf(columnIndex));
        if ((handler == null) || ((handler instanceof UnknownTypeHandler))) {
            handler = OBJECT_TYPE_HANDLER;
        }
        return handler.getResult(rs, columnIndex);
    }

    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return cs.getObject(columnIndex);
    }

    private TypeHandler<? extends Object> resolveTypeHandler(Object parameter, JdbcType jdbcType)
    {
        TypeHandler handler;
        if (parameter == null) {
            handler = OBJECT_TYPE_HANDLER;
        } else {
            handler = this.typeHandlerRegistry.getTypeHandler(parameter.getClass(), jdbcType);

            if ((handler == null) || ((handler instanceof UnknownTypeHandler))) {
                handler = OBJECT_TYPE_HANDLER;
            }
        }
        return handler;
    }

    private TypeHandler<?> resolveTypeHandler(ResultSet rs, String column)
    {
        try {
            Map columnIndexLookup = new HashMap();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String name = rsmd.getColumnName(i);
                columnIndexLookup.put(name, Integer.valueOf(i));
            }
            Integer columnIndex = (Integer)columnIndexLookup.get(column);
            TypeHandler handler = null;
            if (columnIndex != null) {
                handler = resolveTypeHandler(rsmd, columnIndex);
            }
            if ((handler == null) || ((handler instanceof UnknownTypeHandler))) {
                handler = OBJECT_TYPE_HANDLER;
            }
            return handler;
        } catch (SQLException e) {
            throw new TypeException("Error determining JDBC type for column " + column + ".  Cause: " + e, e);
        }

    }

    private TypeHandler<?> resolveTypeHandler(ResultSetMetaData rsmd, Integer columnIndex) throws SQLException
    {
        TypeHandler handler = null;
        JdbcType jdbcType = safeGetJdbcTypeForColumn(rsmd, columnIndex);
        Class javaType = safeGetClassForColumn(rsmd, columnIndex);
        if ((javaType != null) && (jdbcType != null))
            handler = this.typeHandlerRegistry.getTypeHandler(javaType, jdbcType);
        else if (javaType != null)
            handler = this.typeHandlerRegistry.getTypeHandler(javaType);
        else if (jdbcType != null) {
            handler = this.typeHandlerRegistry.getTypeHandler(jdbcType);
        }
        return handler;
    }

    private JdbcType safeGetJdbcTypeForColumn(ResultSetMetaData rsmd, Integer columnIndex) {
        try {
            return JdbcType.forCode(rsmd.getColumnType(columnIndex.intValue()));
        } catch (Exception e) {
        }
        return null;
    }

    private Class<?> safeGetClassForColumn(ResultSetMetaData rsmd, Integer columnIndex)
    {
//        try {
//            return Resources.classForName(rsmd.getColumnClassName(columnIndex.intValue())); } catch (Exception e) {
//        }
        return null;
    }
}