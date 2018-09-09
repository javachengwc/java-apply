package com.mybatis.pseudocode.mybatis.mapping;


import com.mybatis.pseudocode.mybatis.annotations.Param;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.Jdk;
import org.apache.ibatis.reflection.ParamNameUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ResultMap
{
    private Configuration configuration;
    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings;
    private List<ResultMapping> idResultMappings;
    private List<ResultMapping> constructorResultMappings;
    private List<ResultMapping> propertyResultMappings;
    private Set<String> mappedColumns;
    private Set<String> mappedProperties;
    private boolean hasNestedResultMaps;
    private boolean hasNestedQueries;
    private Boolean autoMapping;

    public String getId()
    {
        return this.id;
    }

    public boolean hasNestedResultMaps() {
        return this.hasNestedResultMaps;
    }

    public boolean hasNestedQueries() {
        return this.hasNestedQueries;
    }

    public Class<?> getType() {
        return this.type;
    }

    public List<ResultMapping> getResultMappings() {
        return this.resultMappings;
    }

    public List<ResultMapping> getConstructorResultMappings() {
        return this.constructorResultMappings;
    }

    public List<ResultMapping> getPropertyResultMappings() {
        return this.propertyResultMappings;
    }

    public List<ResultMapping> getIdResultMappings() {
        return this.idResultMappings;
    }

    public Set<String> getMappedColumns() {
        return this.mappedColumns;
    }

    public Set<String> getMappedProperties() {
        return this.mappedProperties;
    }

    public void forceNestedResultMaps() {
        this.hasNestedResultMaps = true;
    }

    public Boolean getAutoMapping() {
        return this.autoMapping;
    }

    public static class Builder
    {
        private static final Log log = LogFactory.getLog(Builder.class);

        private ResultMap resultMap = new ResultMap();

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings) {
            this(configuration, id, type, resultMappings, null);
        }

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings, Boolean autoMapping) {
           //...
        }

        public Class<?> type() {
            return this.resultMap.type;
        }

        public ResultMap build() {
            if (this.resultMap.id == null) {
                throw new IllegalArgumentException("ResultMaps must have an id");
            }
            //...
            return this.resultMap;
        }

        private List<String> argNamesOfMatchingConstructor(List<String> constructorArgNames) {
            Constructor[] constructors = this.resultMap.type.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                Class[] paramTypes = constructor.getParameterTypes();
                if (constructorArgNames.size() == paramTypes.length) {
                    List paramNames = getArgNames(constructor);
                    if ((constructorArgNames.containsAll(paramNames)) &&
                            (argTypesMatch(constructorArgNames, paramTypes, paramNames)))
                    {
                        return paramNames;
                    }
                }
            }
            return null;
        }

        private boolean argTypesMatch(List<String> constructorArgNames, Class<?>[] paramTypes, List<String> paramNames)
        {
            for (int i = 0; i < constructorArgNames.size(); i++) {
                Class actualType = paramTypes[paramNames.indexOf(constructorArgNames.get(i))];
                Class specifiedType = ((ResultMapping)this.resultMap.constructorResultMappings.get(i)).getJavaType();
                if (!actualType.equals(specifiedType)) {
                    if (log.isDebugEnabled()) {
                        log.debug("While building result map '" + this.resultMap.id + "', found a constructor with arg names " + constructorArgNames + ", but the type of '" +
                                (String)constructorArgNames
                                        .get(i) +
                                "' did not match. Specified: [" + specifiedType
                                .getName() + "] Declared: [" + actualType
                                .getName() + "]");
                    }
                    return false;
                }
            }
            return true;
        }

        private List<String> getArgNames(Constructor<?> constructor) {
            if ((this.resultMap.configuration.isUseActualParamName()) && (Jdk.parameterExists)) {
                return ParamNameUtil.getParamNames(constructor);
            }
            List paramNames = new ArrayList();
            Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
            int paramCount = paramAnnotations.length;
            for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
                String name = null;
                for (Annotation annotation : paramAnnotations[paramIndex]) {
                    if ((annotation instanceof Param)) {
                        name = ((Param)annotation).value();
                        break;
                    }
                }
                paramNames.add("arg" + paramIndex);
            }
            return paramNames;
        }
    }
}
