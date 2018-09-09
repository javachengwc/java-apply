package com.mybatis.pseudocode.mybatis.mapping;


import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;

import java.util.List;
import java.util.Set;

public class ResultMapping
{
    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;
    private String nestedResultMapId;
    private String nestedQueryId;
    private Set<String> notNullColumns;
    private String columnPrefix;
    private List<ResultFlag> flags;
    private List<ResultMapping> composites;
    private String resultSet;
    private String foreignColumn;
    private boolean lazy;

    public String getProperty()
    {
        return this.property;
    }

    public String getColumn() {
        return this.column;
    }

    public Class<?> getJavaType() {
        return this.javaType;
    }

    public JdbcType getJdbcType() {
        return this.jdbcType;
    }

    public TypeHandler<?> getTypeHandler() {
        return this.typeHandler;
    }

    public String getNestedResultMapId() {
        return this.nestedResultMapId;
    }

    public String getNestedQueryId() {
        return this.nestedQueryId;
    }

    public Set<String> getNotNullColumns() {
        return this.notNullColumns;
    }

    public String getColumnPrefix() {
        return this.columnPrefix;
    }

    public List<ResultFlag> getFlags() {
        return this.flags;
    }

    public List<ResultMapping> getComposites() {
        return this.composites;
    }

    public boolean isCompositeResult() {
        return (this.composites != null) && (!this.composites.isEmpty());
    }

    public String getResultSet() {
        return this.resultSet;
    }

    public String getForeignColumn() {
        return this.foreignColumn;
    }

    public void setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;
    }

    public boolean isLazy() {
        return this.lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        ResultMapping that = (ResultMapping)o;

        return (this.property != null) && (this.property.equals(that.property));
    }


    public static class Builder
    {
        private ResultMapping resultMapping = new ResultMapping();

        public Builder(Configuration configuration, String property, String column, TypeHandler<?> typeHandler) {
            this(configuration, property);
           //...
        }

        public Builder(Configuration configuration, String property, String column, Class<?> javaType) {
            this(configuration, property);
           //...
        }

        public Builder(Configuration configuration, String property) {
           //...
        }



        public ResultMapping build()
        {
           //...
            resolveTypeHandler();
            validate();
            return this.resultMapping;
        }

        private void validate()
        {
            if ((this.resultMapping.nestedQueryId != null) && (this.resultMapping.nestedResultMapId != null)) {
                throw new IllegalStateException("Cannot define both nestedQueryId and nestedResultMapId in property " + this.resultMapping.property);
            }

            if ((this.resultMapping.nestedQueryId == null) && (this.resultMapping.nestedResultMapId == null) && (this.resultMapping.typeHandler == null)) {
                throw new IllegalStateException("No typehandler found for property " + this.resultMapping.property);
            }

            if ((this.resultMapping.nestedResultMapId == null) && (this.resultMapping.column == null) && (this.resultMapping.composites.isEmpty())) {
                throw new IllegalStateException("Mapping is missing column attribute for property " + this.resultMapping.property);
            }
            if (this.resultMapping.getResultSet() != null) {
                int numColumns = 0;
                if (this.resultMapping.column != null) {
                    numColumns = this.resultMapping.column.split(",").length;
                }
                int numForeignColumns = 0;
                if (this.resultMapping.foreignColumn != null) {
                    numForeignColumns = this.resultMapping.foreignColumn.split(",").length;
                }
                if (numColumns != numForeignColumns)
                    throw new IllegalStateException("There should be the same number of columns and foreignColumns in property " + this.resultMapping.property);
            }
        }

        private void resolveTypeHandler()
        {
            if ((this.resultMapping.typeHandler == null) && (this.resultMapping.javaType != null)) {
                Configuration configuration = this.resultMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                //...
            }
        }

        public Builder column(String column) {
           //...
            return this;
        }
    }
}