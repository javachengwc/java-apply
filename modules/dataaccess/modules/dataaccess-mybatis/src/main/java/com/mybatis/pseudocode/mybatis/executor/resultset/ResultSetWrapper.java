package com.mybatis.pseudocode.mybatis.executor.resultset;

import com.mybatis.pseudocode.mybatis.mapping.JdbcType;
import com.mybatis.pseudocode.mybatis.mapping.ResultMap;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.type.ObjectTypeHandler;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;
import com.mybatis.pseudocode.mybatis.type.UnknownTypeHandler;
import org.apache.ibatis.io.Resources;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

//ResultSetWrapper是ResultSet的包装类
public class ResultSetWrapper
{
    private final ResultSet resultSet;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final List<String> columnNames = new ArrayList();
    private final List<String> classNames = new ArrayList();
    private final List<JdbcType> jdbcTypes = new ArrayList();
    private final Map<String, Map<Class<?>, TypeHandler<?>>> typeHandlerMap = new HashMap();
    private final Map<String, List<String>> mappedColumnNamesMap = new HashMap();
    private final Map<String, List<String>> unMappedColumnNamesMap = new HashMap();

    public ResultSetWrapper(ResultSet rs, Configuration configuration) throws SQLException
    {
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        this.resultSet = rs;
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            this.columnNames.add(configuration.isUseColumnLabel() ? metaData.getColumnLabel(i) : metaData.getColumnName(i));
            this.jdbcTypes.add(JdbcType.forCode(metaData.getColumnType(i)));
            this.classNames.add(metaData.getColumnClassName(i));
        }
    }

    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    public List<String> getClassNames() {
        return Collections.unmodifiableList(this.classNames);
    }

    public JdbcType getJdbcType(String columnName) {
        for (int i = 0; i < this.columnNames.size(); i++) {
            if (((String)this.columnNames.get(i)).equalsIgnoreCase(columnName)) {
                return (JdbcType)this.jdbcTypes.get(i);
            }
        }
        return null;
    }

    public TypeHandler<?> getTypeHandler(Class<?> propertyType, String columnName)
    {
        TypeHandler handler = null;
        Map columnHandlers = (Map)this.typeHandlerMap.get(columnName);
        if (columnHandlers == null) {
            columnHandlers = new HashMap();
            this.typeHandlerMap.put(columnName, columnHandlers);
        } else {
            handler = (TypeHandler)columnHandlers.get(propertyType);
        }
        if (handler == null) {
            JdbcType jdbcType = getJdbcType(columnName);
            handler = this.typeHandlerRegistry.getTypeHandler(propertyType, jdbcType);

            if ((handler == null) || ((handler instanceof UnknownTypeHandler))) {
                int index = this.columnNames.indexOf(columnName);
                Class javaType = resolveClass((String)this.classNames.get(index));
                if ((javaType != null) && (jdbcType != null))
                    handler = this.typeHandlerRegistry.getTypeHandler(javaType, jdbcType);
                else if (javaType != null)
                    handler = this.typeHandlerRegistry.getTypeHandler(javaType);
                else if (jdbcType != null) {
                    handler = this.typeHandlerRegistry.getTypeHandler(jdbcType);
                }
            }
            if ((handler == null) || ((handler instanceof UnknownTypeHandler))) {
                handler = new ObjectTypeHandler();
            }
            columnHandlers.put(propertyType, handler);
        }
        return handler;
    }

    private Class<?> resolveClass(String className)
    {
        try {
            if (className != null)
                return Resources.classForName(className);
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
        }
        return null;
    }

    private void loadMappedAndUnmappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException {
        List mappedColumnNames = new ArrayList();
        List unmappedColumnNames = new ArrayList();
        String upperColumnPrefix = columnPrefix == null ? null : columnPrefix.toUpperCase(Locale.ENGLISH);
        Set mappedColumns = prependPrefixes(resultMap.getMappedColumns(), upperColumnPrefix);
        for (String columnName : this.columnNames) {
            String upperColumnName = columnName.toUpperCase(Locale.ENGLISH);
            if (mappedColumns.contains(upperColumnName))
                mappedColumnNames.add(upperColumnName);
            else {
                unmappedColumnNames.add(columnName);
            }
        }
        this.mappedColumnNamesMap.put(getMapKey(resultMap, columnPrefix), mappedColumnNames);
        this.unMappedColumnNamesMap.put(getMapKey(resultMap, columnPrefix), unmappedColumnNames);
    }

    public List<String> getMappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException {
        List mappedColumnNames = (List)this.mappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        if (mappedColumnNames == null) {
            loadMappedAndUnmappedColumnNames(resultMap, columnPrefix);
            mappedColumnNames = (List)this.mappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        }
        return mappedColumnNames;
    }

    public List<String> getUnmappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException {
        List unMappedColumnNames = (List)this.unMappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        if (unMappedColumnNames == null) {
            loadMappedAndUnmappedColumnNames(resultMap, columnPrefix);
            unMappedColumnNames = (List)this.unMappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        }
        return unMappedColumnNames;
    }

    private String getMapKey(ResultMap resultMap, String columnPrefix) {
        return resultMap.getId() + ":" + columnPrefix;
    }

    private Set<String> prependPrefixes(Set<String> columnNames, String prefix) {
        if ((columnNames == null) || (columnNames.isEmpty()) || (prefix == null) || (prefix.length() == 0)) {
            return columnNames;
        }
        Set prefixed = new HashSet();
        for (String columnName : columnNames) {
            prefixed.add(prefix + columnName);
        }
        return prefixed;
    }
}
