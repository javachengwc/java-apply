package com.mybatis.pseudocode.mybatis.builder;

import com.mybatis.pseudocode.mybatis.cache.Cache;
import com.mybatis.pseudocode.mybatis.cache.decorators.LruCache;
import com.mybatis.pseudocode.mybatis.cache.impl.PerpetualCache;
import com.mybatis.pseudocode.mybatis.executor.kengen.KeyGenerator;
import com.mybatis.pseudocode.mybatis.mapping.*;
import com.mybatis.pseudocode.mybatis.scripting.LanguageDriver;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.mapping.Discriminator;
import org.apache.ibatis.reflection.MetaClass;

import java.sql.ResultSet;
import java.util.*;

//解析Mapper.xml文件信息生成相应的对象,并添加到configuration相应的集合中
//解析配置中很重要的一个类
public class MapperBuilderAssistant extends BaseBuilder
{
    private String currentNamespace;

    private final String resource;

    private Cache currentCache;

    private boolean unresolvedCacheRef;

    public MapperBuilderAssistant(Configuration configuration, String resource)
    {
        super(configuration);
        //ErrorContext.instance().resource(resource);
        this.resource = resource;
    }

    public String getCurrentNamespace() {
        return this.currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        if (currentNamespace == null) {
            throw new BuilderException("The mapper element requires a namespace attribute to be specified.");
        }

        if ((this.currentNamespace != null) && (!this.currentNamespace.equals(currentNamespace))) {
            throw new BuilderException("Wrong namespace. Expected '" + this.currentNamespace + "' but found '" + currentNamespace + "'.");
        }

        this.currentNamespace = currentNamespace;
    }

    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference)
        {
            if (base.contains("."))
                return base;
        }
        else
        {
            if (base.startsWith(this.currentNamespace + ".")) {
                return base;
            }
            if (base.contains(".")) {
                throw new BuilderException("Dots are not allowed in element names, please remove it from " + base);
            }
        }
        return this.currentNamespace + "." + base;
    }

    public Cache useCacheRef(String namespace) {
        if (namespace == null)
            throw new BuilderException("cache-ref element requires a namespace attribute.");
        try
        {
            this.unresolvedCacheRef = true;
            Cache cache = this.configuration.getCache(namespace);
            if (cache == null) {
                throw new IncompleteElementException("No cache for namespace '" + namespace + "' could be found.");
            }
            this.currentCache = cache;
            this.unresolvedCacheRef = false;
            return cache;
        } catch (IllegalArgumentException e) {
            throw new IncompleteElementException("No cache for namespace '" + namespace + "' could be found.", e);
        }
    }

    //使用二级缓存
    public Cache useNewCache(Class<? extends Cache> typeClass, Class<? extends Cache> evictionClass,
                             Long flushInterval, Integer size, boolean readWrite, boolean blocking, Properties props)
    {
        Cache cache = new CacheBuilder(this.currentNamespace)
                .implementation((Class)valueOrDefault(typeClass, PerpetualCache.class))
                .addDecorator((Class)valueOrDefault(evictionClass, LruCache.class))
                .clearInterval(flushInterval)
                .size(size)
                .readWrite(readWrite)
                .blocking(blocking)
                .properties(props)
                .build();
        //加入到configuration的caches集合中
        this.configuration.addCache(cache);
        this.currentCache = cache;
        return cache;
    }

    //增加ParameterMap
    public ParameterMap addParameterMap(String id, Class<?> parameterClass, List<ParameterMapping> parameterMappings) {
        id = applyCurrentNamespace(id, false);
        ParameterMap parameterMap = new ParameterMap.Builder(this.configuration, id, parameterClass, parameterMappings).build();
        this.configuration.addParameterMap(parameterMap);
        return parameterMap;
    }

    public ParameterMapping buildParameterMapping(Class<?> parameterType, String property, Class<?> javaType, JdbcType jdbcType, String resultMap, ParameterMode parameterMode, Class<? extends TypeHandler<?>> typeHandler, Integer numericScale)
    {
        resultMap = applyCurrentNamespace(resultMap, true);

        Class javaTypeClass = resolveParameterJavaType(parameterType, property, javaType, jdbcType);
        TypeHandler typeHandlerInstance = resolveTypeHandler(javaTypeClass, typeHandler);

        return new ParameterMapping.Builder(this.configuration, property, javaTypeClass)
                //.jdbcType(jdbcType)
                //.resultMapId(resultMap)
                //.mode(parameterMode)
                //.numericScale(numericScale)
                //.typeHandler(typeHandlerInstance)
                .build();
    }

    //添加ResultMap
    public ResultMap addResultMap(String id, Class<?> type, String extend, Discriminator discriminator,
                                  List<ResultMapping> resultMappings, Boolean autoMapping)
    {
        id = applyCurrentNamespace(id, false);
        extend = applyCurrentNamespace(extend, true);

        if (extend != null) {
            if (!this.configuration.hasResultMap(extend)) {
                throw new IncompleteElementException("Could not find a parent resultmap with id '" + extend + "'");
            }
            ResultMap resultMap = this.configuration.getResultMap(extend);
            List extendedResultMappings = new ArrayList(resultMap.getResultMappings());
            extendedResultMappings.removeAll(resultMappings);

            boolean declaresConstructor = false;
            for (ResultMapping resultMapping : resultMappings) {
                if (resultMapping.getFlags().contains(ResultFlag.CONSTRUCTOR)) {
                    declaresConstructor = true;
                    break;
                }
            }
            if (declaresConstructor) {
                Iterator extendedResultMappingsIter = extendedResultMappings.iterator();
                while (extendedResultMappingsIter.hasNext()) {
                    if (((ResultMapping)extendedResultMappingsIter.next()).getFlags().contains(ResultFlag.CONSTRUCTOR)) {
                        extendedResultMappingsIter.remove();
                    }
                }
            }
            resultMappings.addAll(extendedResultMappings);
        }

        ResultMap resultMap = new ResultMap.Builder(this.configuration,id,type,resultMappings,autoMapping)
                //.discriminator(discriminator)
                .build();
        //存入Configuration对象的resultMaps容器
        this.configuration.addResultMap(resultMap);
        return resultMap;
    }

    public Discriminator buildDiscriminator(Class<?> resultType, String column, Class<?> javaType, JdbcType jdbcType, Class<? extends TypeHandler<?>> typeHandler, Map<String, String> discriminatorMap)
    {
        //...
        return null;
    }

    //增加MappedStatement
    public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap,
          Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered,
          KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets)
    {
        if (this.unresolvedCacheRef) {
            throw new IncompleteElementException("Cache-ref not yet resolved");
        }

        id = applyCurrentNamespace(id, false);
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(this.configuration, id, sqlSource, sqlCommandType)
                .resource(this.resource)
                //.fetchSize(fetchSize)
                //.timeout(timeout)
                //.statementType(statementType)
                //.keyGenerator(keyGenerator)
                //.keyProperty(keyProperty)
                //.keyColumn(keyColumn)
                //.databaseId(databaseId)
                //.lang(lang)
                //.resultOrdered(resultOrdered)
                //.resultSets(resultSets)
                //.resultMaps(getStatementResultMaps(resultMap, resultType, id))
                //.resultSetType(resultSetType)
                .flushCacheRequired(((Boolean)valueOrDefault(Boolean.valueOf(flushCache), Boolean.valueOf(!isSelect))).booleanValue())
                //useCache=true 或则 useCache为空但是select的statment，MappedStatement的useCache才设置成true
                .useCache(((Boolean)valueOrDefault(Boolean.valueOf(useCache), Boolean.valueOf(isSelect))).booleanValue())
                //这里用的cache,就是mapper.xml中配置的那cache。
                .cache(this.currentCache);

        ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
        if (statementParameterMap != null) {
            statementBuilder.parameterMap(statementParameterMap);
        }

        MappedStatement statement = statementBuilder.build();
        //存入configuration的mappedStatements中
        this.configuration.addMappedStatement(statement);
        return statement;
    }

    private <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    private ParameterMap getStatementParameterMap(String parameterMapName, Class<?> parameterTypeClass, String statementId)
    {
        parameterMapName = applyCurrentNamespace(parameterMapName, true);
        ParameterMap parameterMap = null;
        if (parameterMapName != null) {
            try {
                parameterMap = this.configuration.getParameterMap(parameterMapName);
            } catch (IllegalArgumentException e) {
                throw new IncompleteElementException("Could not find parameter map " + parameterMapName, e);
            }
        } else if (parameterTypeClass != null) {
            List parameterMappings = new ArrayList();
            parameterMap = new ParameterMap.Builder(this.configuration, statementId + "-Inline", parameterTypeClass, parameterMappings).build();
        }
        return parameterMap;
    }

    private List<ResultMap> getStatementResultMaps(String resultMap, Class<?> resultType, String statementId)
    {
        resultMap = applyCurrentNamespace(resultMap, true);
        List resultMaps = new ArrayList();
        if (resultMap != null) {
            String[] resultMapNames = resultMap.split(",");
            for (String resultMapName : resultMapNames)
                try {
                    resultMaps.add(this.configuration.getResultMap(resultMapName.trim()));
                } catch (IllegalArgumentException e) {
                    throw new IncompleteElementException("Could not find result map " + resultMapName, e);
                }
        }
        else if (resultType != null)
        {
            ResultMap inlineResultMap = new ResultMap.Builder(this.configuration, statementId + "-Inline", resultType,
                    new ArrayList(), null).build();
            resultMaps.add(inlineResultMap);
        }
        return resultMaps;
    }

    public ResultMapping buildResultMapping(Class<?> resultType, String property, String column, Class<?> javaType, JdbcType jdbcType,
           String nestedSelect, String nestedResultMap, String notNullColumn, String columnPrefix,
           Class<? extends TypeHandler<?>> typeHandler, List<ResultFlag> flags, String resultSet, String foreignColumn, boolean lazy)
    {
        Class javaTypeClass = resolveResultJavaType(resultType, property, javaType);
        TypeHandler typeHandlerInstance = resolveTypeHandler(javaTypeClass, typeHandler);
        List composites = parseCompositeColumnName(column);
        return new ResultMapping.Builder(this.configuration, property, column, javaTypeClass)
                //.jdbcType(jdbcType)
                //.nestedQueryId(applyCurrentNamespace(nestedSelect, true))
                //.nestedResultMapId(applyCurrentNamespace(nestedResultMap, true))
                //.resultSet(resultSet)
                //.typeHandler(typeHandlerInstance)
                //.flags(flags == null ? new ArrayList() : flags)
                //.composites(composites)
                //.notNullColumns(parseMultipleColumnNames(notNullColumn))
                //.columnPrefix(columnPrefix)
                //.foreignColumn(foreignColumn)
                //.lazy(lazy)
                .build();
    }

    private Set<String> parseMultipleColumnNames(String columnName) {
        Set columns = new HashSet();
        if (columnName != null) {
            if (columnName.indexOf(',') > -1) {
                StringTokenizer parser = new StringTokenizer(columnName, "{}, ", false);
                while (parser.hasMoreTokens()) {
                    String column = parser.nextToken();
                    columns.add(column);
                }
            } else {
                columns.add(columnName);
            }
        }
        return columns;
    }

    private List<ResultMapping> parseCompositeColumnName(String columnName) {
        List composites = new ArrayList();
        if ((columnName != null) && ((columnName.indexOf('=') > -1) || (columnName.indexOf(',') > -1))) {
            StringTokenizer parser = new StringTokenizer(columnName, "{}=, ", false);
            while (parser.hasMoreTokens()) {
                String property = parser.nextToken();
                String column = parser.nextToken();

                ResultMapping complexResultMapping = new ResultMapping.Builder(this.configuration, property, column, this.configuration
                        .getTypeHandlerRegistry().getUnknownTypeHandler()).build();
                composites.add(complexResultMapping);
            }
        }
        return composites;
    }

    private Class<?> resolveResultJavaType(Class<?> resultType, String property, Class<?> javaType) {
        if ((javaType == null) && (property != null))
            try {
                MetaClass metaResultType = MetaClass.forClass(resultType, this.configuration.getReflectorFactory());
                javaType = metaResultType.getSetterType(property);
            }
            catch (Exception localException)
            {
            }
        if (javaType == null) {
            javaType = Object.class;
        }
        return javaType;
    }

    private Class<?> resolveParameterJavaType(Class<?> resultType, String property, Class<?> javaType, JdbcType jdbcType) {
        if (javaType == null) {
            if (JdbcType.CURSOR.equals(jdbcType)) {
                javaType = ResultSet.class;
            } else if (Map.class.isAssignableFrom(resultType)) {
                javaType = Object.class;
            } else {
                MetaClass metaResultType = MetaClass.forClass(resultType, this.configuration.getReflectorFactory());
                javaType = metaResultType.getGetterType(property);
            }
        }
        if (javaType == null) {
            javaType = Object.class;
        }
        return javaType;
    }

    public ResultMapping buildResultMapping(Class<?> resultType, String property, String column, Class<?> javaType,
          JdbcType jdbcType, String nestedSelect, String nestedResultMap, String notNullColumn, String columnPrefix,
          Class<? extends TypeHandler<?>> typeHandler, List<ResultFlag> flags)
    {
        return buildResultMapping(resultType, property, column, javaType, jdbcType, nestedSelect, nestedResultMap, notNullColumn,
                columnPrefix, typeHandler, flags, null, null, this.configuration.isLazyLoadingEnabled());
    }

    public LanguageDriver getLanguageDriver(Class<?> langClass) {
        if (langClass != null)
            this.configuration.getLanguageRegistry().register(langClass);
        else {
            langClass = this.configuration.getLanguageRegistry().getDefaultDriverClass();
        }
        return this.configuration.getLanguageRegistry().getDriver(langClass);
    }

    public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType,
          SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType,
          String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered,
          KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang)
    {
        return addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout, parameterMap, parameterType,
                resultMap, resultType, resultSetType, flushCache, useCache, resultOrdered, keyGenerator, keyProperty, keyColumn,
                databaseId, lang, null);
    }
}
