package com.mybatis.pseudocode.mybatis.mapping;


import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;

import java.sql.ResultSet;

//参数映射实体
public class ParameterMapping
{
    private Configuration configuration;

    //参数名字
    private String property;

    //参数模型
    private ParameterMode mode;

    //参数的java类型
    private Class<?> javaType = Object.class;
    //参数对应的jdbc类型
    private JdbcType jdbcType;

    private Integer numericScale;

    //参数的typeHandler
    private TypeHandler<?> typeHandler;

    private String resultMapId;

    private String jdbcTypeName;

    private String expression;

    public String getProperty()
    {
        return this.property;
    }

    public ParameterMode getMode()
    {
        return this.mode;
    }

    public Class<?> getJavaType()
    {
        return this.javaType;
    }

    public JdbcType getJdbcType()
    {
        return this.jdbcType;
    }

    public Integer getNumericScale()
    {
        return this.numericScale;
    }

    public TypeHandler<?> getTypeHandler()
    {
        return this.typeHandler;
    }

    public String getResultMapId()
    {
        return this.resultMapId;
    }

    public String getJdbcTypeName()
    {
        return this.jdbcTypeName;
    }

    public String getExpression()
    {
        return this.expression;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("ParameterMapping{");

        sb.append("property='").append(this.property).append('\'');
        sb.append(", mode=").append(this.mode);
        sb.append(", javaType=").append(this.javaType);
        sb.append(", jdbcType=").append(this.jdbcType);
        sb.append(", numericScale=").append(this.numericScale);
        sb.append(", resultMapId='").append(this.resultMapId).append('\'');
        sb.append(", jdbcTypeName='").append(this.jdbcTypeName).append('\'');
        sb.append(", expression='").append(this.expression).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Builder
    {
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property, TypeHandler<?> typeHandler) {
            //...
        }

        public Builder(Configuration configuration, String property, Class<?> javaType) {
           //...
        }


        //...

        public ParameterMapping build() {
            //resolveTypeHandler();
            validate();
            return this.parameterMapping;
        }

        private void validate() {
            if (ResultSet.class.equals(this.parameterMapping.javaType)) {
                if (this.parameterMapping.resultMapId == null)
                {
                    throw new IllegalStateException("Missing resultmap in property '" + this.parameterMapping.property +
                            "'.  Parameters of type java.sql.ResultSet require a resultmap.");
                }
            }
            else if (this.parameterMapping.typeHandler == null)
            {
                throw new IllegalStateException("Type handler was null on parameter mapping for property '" + this.parameterMapping.property +
                        "'. It was either not specified and/or could not be found for the javaType (" + this.parameterMapping.javaType
                        .getName() + ") : jdbcType (" + this.parameterMapping.jdbcType + ") combination.");
            }
        }
    }
}
