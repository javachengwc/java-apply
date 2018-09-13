package com.mybatis.pseudocode.mybatis.session;

import com.mybatis.pseudocode.mybatis.binding.MapperRegistry;
import com.mybatis.pseudocode.mybatis.builder.ResultMapResolver;
import com.mybatis.pseudocode.mybatis.builder.annotation.MethodResolver;
import com.mybatis.pseudocode.mybatis.builder.xml.XMLStatementBuilder;
import com.mybatis.pseudocode.mybatis.cache.Cache;
import com.mybatis.pseudocode.mybatis.executor.CachingExecutor;
import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.executor.SimpleExecutor;
import com.mybatis.pseudocode.mybatis.executor.kengen.KeyGenerator;
import com.mybatis.pseudocode.mybatis.executor.parameter.ParameterHandler;
import com.mybatis.pseudocode.mybatis.executor.resultset.DefaultResultSetHandler;
import com.mybatis.pseudocode.mybatis.executor.resultset.ResultSetHandler;
import com.mybatis.pseudocode.mybatis.executor.statement.RoutingStatementHandler;
import com.mybatis.pseudocode.mybatis.executor.statement.StatementHandler;
import com.mybatis.pseudocode.mybatis.mapping.*;
import com.mybatis.pseudocode.mybatis.plugin.Interceptor;
import com.mybatis.pseudocode.mybatis.plugin.InterceptorChain;
import com.mybatis.pseudocode.mybatis.scripting.LanguageDriverRegistry;
import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import com.mybatis.pseudocode.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.mybatis.pseudocode.mybatis.type.TypeAliasRegistry;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.executor.loader.javassist.JavassistProxyFactory;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.*;

//myBatis所有的配置信息都维持在Configuration中
public class Configuration
{
    //全局配置文件中的environment节点对应的配置,里面包含数据源，事务工厂的配置
    protected Environment environment;

    protected boolean safeRowBoundsEnabled;
    protected boolean safeResultHandlerEnabled = true;
    protected boolean mapUnderscoreToCamelCase;
    protected boolean aggressiveLazyLoading;

    protected boolean useGeneratedKeys;
    protected boolean useColumnLabel = true;

    //这些属性，可通过全局配置文件中的settings节点配置覆盖
    //是否启用二级缓存，默认true
    protected boolean cacheEnabled = true;
    //延迟加载，默认false
    protected boolean lazyLoadingEnabled = false;
    protected boolean multipleResultSetsEnabled = true;

    protected boolean callSettersOnNulls;
    protected boolean useActualParamName = true;
    protected boolean returnInstanceForEmptyRow;


    //一级缓存范围，也就是本地缓存换位，默认SESSION ，也可以设置成STATEMENT,
    //在全局配置文件中设置localCacheScope=STATEMENT
    protected LocalCacheScope localCacheScope = LocalCacheScope.SESSION;

    protected JdbcType jdbcTypeForNull = JdbcType.OTHER;
    protected Set<String> lazyLoadTriggerMethods = new HashSet(Arrays.asList(new String[] { "equals", "clone", "hashCode", "toString" }));

    //默认的响应超时时长(毫秒)
    //比如全局配置文件中设置<setting name="defaultStatementTimeout" value="25000" />
    protected Integer defaultStatementTimeout;
    protected Integer defaultFetchSize;

    //执行器类型，默认简单类型
    protected ExecutorType defaultExecutorType = ExecutorType.SIMPLE;

    //mybatis.xml全局配置文件中properties节点中配置的信息
    protected Properties variables = new Properties();

    protected ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    protected ProxyFactory proxyFactory = new JavassistProxyFactory();
    protected String databaseId;
    protected Class<?> configurationFactory;


    //mapper注册中心
    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);

    //拦截器链
    protected final InterceptorChain interceptorChain = new InterceptorChain();

    //类型转换注册中心
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    //类型别名中心
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();


    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();

    //所有MappedStatement的map集合,每在mapper.xml中发现一个statment,就会放到这里
    //key为mapper.xml的namespace+statment的id
    protected final Map<String, MappedStatement> mappedStatements = new StrictMap("Mapped Statements collection");

    //所有二级缓存的map集合，每在mapper.xml中发现一个cache,就会放到这里
    //key为mapper.xml的namespace
    protected final Map<String, Cache> caches = new StrictMap("Caches collection");

    //所有ResultMap的map集合,每在mapper.xml中发现一个resultMap,就会放到这里
    //key为mapper.xml的namespace+resultMap的id
    protected final Map<String, ResultMap> resultMaps = new StrictMap("Result Maps collection");

    //所有ParameterMap的map集合,每在mapper.xml中发现一个parameterMap,就会放到这里
    //key为mapper.xml的namespace+parameterMap的id
    protected final Map<String, ParameterMap> parameterMaps = new StrictMap("Parameter Maps collection");

    protected final Map<String, KeyGenerator> keyGenerators = new StrictMap("Key Generators collection");

    //加载的mapper.xml
    protected final Set<String> loadedResources = new HashSet();

    protected final Map<String, XNode> sqlFragments = new StrictMap("XML fragments parsed from previous mappers");

    //未完成解析的XMLStatementBuilder
    protected final Collection<XMLStatementBuilder> incompleteStatements = new LinkedList();

    protected final Collection<CacheRefResolver> incompleteCacheRefs = new LinkedList();

    //未完成解析的ResultMap
    protected final Collection<ResultMapResolver> incompleteResultMaps = new LinkedList();

    protected final Collection<MethodResolver> incompleteMethods = new LinkedList();

    protected final Map<String, String> cacheRefMap = new HashMap();

    public Configuration(Environment environment) {
        this();
        this.environment = environment;
    }

    public Configuration() {
        this.typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        //...

        //this.languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
        //this.languageRegistry.register(RawLanguageDriver.class);
    }


    public boolean isCallSettersOnNulls() {
        return this.callSettersOnNulls;
    }

    public void setCallSettersOnNulls(boolean callSettersOnNulls) {
        this.callSettersOnNulls = callSettersOnNulls;
    }

    public boolean isUseActualParamName() {
        return this.useActualParamName;
    }

    public void setUseActualParamName(boolean useActualParamName) {
        this.useActualParamName = useActualParamName;
    }

    public boolean isReturnInstanceForEmptyRow() {
        return this.returnInstanceForEmptyRow;
    }

    public void setReturnInstanceForEmptyRow(boolean returnEmptyInstance) {
        this.returnInstanceForEmptyRow = returnEmptyInstance;
    }

    public String getDatabaseId() {
        return this.databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public Class<?> getConfigurationFactory() {
        return this.configurationFactory;
    }

    public void setConfigurationFactory(Class<?> configurationFactory) {
        this.configurationFactory = configurationFactory;
    }

    public boolean isSafeResultHandlerEnabled() {
        return this.safeResultHandlerEnabled;
    }

    public void setSafeResultHandlerEnabled(boolean safeResultHandlerEnabled) {
        this.safeResultHandlerEnabled = safeResultHandlerEnabled;
    }

    public boolean isSafeRowBoundsEnabled() {
        return this.safeRowBoundsEnabled;
    }

    public void setSafeRowBoundsEnabled(boolean safeRowBoundsEnabled) {
        this.safeRowBoundsEnabled = safeRowBoundsEnabled;
    }

    public boolean isMapUnderscoreToCamelCase() {
        return this.mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public void addLoadedResource(String resource) {
        this.loadedResources.add(resource);
    }

    public boolean isResourceLoaded(String resource) {
        return this.loadedResources.contains(resource);
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public boolean isLazyLoadingEnabled() {
        return this.lazyLoadingEnabled;
    }

    public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
        this.lazyLoadingEnabled = lazyLoadingEnabled;
    }

    public ProxyFactory getProxyFactory() {
        return this.proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        if (proxyFactory == null) {
            proxyFactory = new JavassistProxyFactory();
        }
        this.proxyFactory = proxyFactory;
    }

    public boolean isAggressiveLazyLoading() {
        return this.aggressiveLazyLoading;
    }

    public void setAggressiveLazyLoading(boolean aggressiveLazyLoading) {
        this.aggressiveLazyLoading = aggressiveLazyLoading;
    }

    public boolean isMultipleResultSetsEnabled() {
        return this.multipleResultSetsEnabled;
    }

    public void setMultipleResultSetsEnabled(boolean multipleResultSetsEnabled) {
        this.multipleResultSetsEnabled = multipleResultSetsEnabled;
    }

    public Set<String> getLazyLoadTriggerMethods() {
        return this.lazyLoadTriggerMethods;
    }

    public void setLazyLoadTriggerMethods(Set<String> lazyLoadTriggerMethods) {
        this.lazyLoadTriggerMethods = lazyLoadTriggerMethods;
    }

    public boolean isUseGeneratedKeys() {
        return this.useGeneratedKeys;
    }

    public void setUseGeneratedKeys(boolean useGeneratedKeys) {
        this.useGeneratedKeys = useGeneratedKeys;
    }

    public ExecutorType getDefaultExecutorType() {
        return this.defaultExecutorType;
    }

    public void setDefaultExecutorType(ExecutorType defaultExecutorType) {
        this.defaultExecutorType = defaultExecutorType;
    }

    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public Integer getDefaultStatementTimeout() {
        return this.defaultStatementTimeout;
    }

    public void setDefaultStatementTimeout(Integer defaultStatementTimeout) {
        this.defaultStatementTimeout = defaultStatementTimeout;
    }

    public Integer getDefaultFetchSize()
    {
        return this.defaultFetchSize;
    }

    public void setDefaultFetchSize(Integer defaultFetchSize)
    {
        this.defaultFetchSize = defaultFetchSize;
    }

    public boolean isUseColumnLabel() {
        return this.useColumnLabel;
    }

    public void setUseColumnLabel(boolean useColumnLabel) {
        this.useColumnLabel = useColumnLabel;
    }

    public LocalCacheScope getLocalCacheScope() {
        return this.localCacheScope;
    }

    public void setLocalCacheScope(LocalCacheScope localCacheScope) {
        this.localCacheScope = localCacheScope;
    }

    public JdbcType getJdbcTypeForNull() {
        return this.jdbcTypeForNull;
    }

    public void setJdbcTypeForNull(JdbcType jdbcTypeForNull) {
        this.jdbcTypeForNull = jdbcTypeForNull;
    }

    public Properties getVariables() {
        return this.variables;
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return this.typeHandlerRegistry;
    }

    public void setDefaultEnumTypeHandler(Class<? extends TypeHandler> typeHandler)
    {
        if (typeHandler != null)
            getTypeHandlerRegistry().setDefaultEnumTypeHandler(typeHandler);
    }

    public TypeAliasRegistry getTypeAliasRegistry()
    {
        return this.typeAliasRegistry;
    }

    public MapperRegistry getMapperRegistry()
    {
        return this.mapperRegistry;
    }

    public ReflectorFactory getReflectorFactory() {
        return this.reflectorFactory;
    }

    public void setReflectorFactory(ReflectorFactory reflectorFactory) {
        this.reflectorFactory = reflectorFactory;
    }

    public ObjectFactory getObjectFactory() {
        return this.objectFactory;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return this.objectWrapperFactory;
    }

    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    public List<Interceptor> getInterceptors()
    {
        return this.interceptorChain.getInterceptors();
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return this.languageRegistry;
    }


    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        //ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
        ParameterHandler parameterHandler =null;
        parameterHandler = (ParameterHandler)this.interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql)
    {
        ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        resultSetHandler = (ResultSetHandler)this.interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
        statementHandler = (StatementHandler)this.interceptorChain.pluginAll(statementHandler);
        return statementHandler;
    }

    public Executor newExecutor(Transaction transaction) {
        return newExecutor(transaction, this.defaultExecutorType);
    }

    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? this.defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor=null;
        if (ExecutorType.BATCH == executorType) {
            //executor = new BatchExecutor(this, transaction);
        }
        else
        {
            if (ExecutorType.REUSE == executorType) {
                //    executor = new ReuseExecutor(this, transaction);
            }
            else
                executor = new SimpleExecutor(this, transaction);
        }
        if (this.cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
         executor = (Executor)this.interceptorChain.pluginAll(executor);
        return executor;
    }

    public void addKeyGenerator(String id, KeyGenerator keyGenerator) {
        this.keyGenerators.put(id, keyGenerator);
    }

    public Collection<String> getKeyGeneratorNames() {
        return this.keyGenerators.keySet();
    }

    public Collection<KeyGenerator> getKeyGenerators() {
        return this.keyGenerators.values();
    }

    public KeyGenerator getKeyGenerator(String id) {
        return (KeyGenerator)this.keyGenerators.get(id);
    }

    public boolean hasKeyGenerator(String id) {
        return this.keyGenerators.containsKey(id);
    }

    public void addCache(Cache cache) {
        this.caches.put(cache.getId(), cache);
    }

    public Collection<String> getCacheNames() {
        return this.caches.keySet();
    }

    public Collection<Cache> getCaches() {
        return this.caches.values();
    }

    public Cache getCache(String id) {
        return (Cache)this.caches.get(id);
    }

    public boolean hasCache(String id) {
        return this.caches.containsKey(id);
    }

    public void addResultMap(ResultMap rm) {
        this.resultMaps.put(rm.getId(), rm);
//        checkLocallyForDiscriminatedNestedResultMaps(rm);
//        checkGloballyForDiscriminatedNestedResultMaps(rm);
    }

    public Collection<String> getResultMapNames() {
        return this.resultMaps.keySet();
    }

    public Collection<ResultMap> getResultMaps() {
        return this.resultMaps.values();
    }

    public ResultMap getResultMap(String id) {
        return (ResultMap)this.resultMaps.get(id);
    }

    public boolean hasResultMap(String id) {
        return this.resultMaps.containsKey(id);
    }

    public void addParameterMap(ParameterMap pm) {
        this.parameterMaps.put(pm.getId(), pm);
    }

    public Collection<String> getParameterMapNames() {
        return this.parameterMaps.keySet();
    }

    public Collection<ParameterMap> getParameterMaps() {
        return this.parameterMaps.values();
    }

    public ParameterMap getParameterMap(String id) {
        return (ParameterMap)this.parameterMaps.get(id);
    }

    public boolean hasParameterMap(String id) {
        return this.parameterMaps.containsKey(id);
    }

    public void addMappedStatement(MappedStatement ms) {
        this.mappedStatements.put(ms.getId(), ms);
    }

    public Collection<String> getMappedStatementNames() {
        buildAllStatements();
        return this.mappedStatements.keySet();
    }

    public Collection<MappedStatement> getMappedStatements() {
        buildAllStatements();
        return this.mappedStatements.values();
    }

    public Collection<XMLStatementBuilder> getIncompleteStatements() {
        return this.incompleteStatements;
    }

    public void addIncompleteStatement(XMLStatementBuilder incompleteStatement) {
        this.incompleteStatements.add(incompleteStatement);
    }

    public Collection<CacheRefResolver> getIncompleteCacheRefs() {
        return this.incompleteCacheRefs;
    }

    public void addIncompleteCacheRef(CacheRefResolver incompleteCacheRef) {
        this.incompleteCacheRefs.add(incompleteCacheRef);
    }

    public Collection<ResultMapResolver> getIncompleteResultMaps() {
        return this.incompleteResultMaps;
    }

    public void addIncompleteResultMap(ResultMapResolver resultMapResolver) {
        this.incompleteResultMaps.add(resultMapResolver);
    }

    public void addIncompleteMethod(MethodResolver builder) {
        this.incompleteMethods.add(builder);
    }

    public Collection<MethodResolver> getIncompleteMethods() {
        return this.incompleteMethods;
    }

    public MappedStatement getMappedStatement(String id) {
        return getMappedStatement(id, true);
    }

    public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
        if (validateIncompleteStatements) {
            buildAllStatements();
        }
        return (MappedStatement)this.mappedStatements.get(id);
    }

    public Map<String, XNode> getSqlFragments() {
        return this.sqlFragments;
    }

    //添加拦截器
    public void addInterceptor(Interceptor interceptor) {
        this.interceptorChain.addInterceptor(interceptor);
    }

    public void addMappers(String packageName, Class<?> superType) {
        this.mapperRegistry.addMappers(packageName, superType);
    }

    //注册某个包下面所有的Mapper接口类
    public void addMappers(String packageName) {
        this.mapperRegistry.addMappers(packageName);
    }

    //增加mapper接口注册
    public <T> void addMapper(Class<T> type) {
        this.mapperRegistry.addMapper(type);
    }

    //获取type这个Mapper接口类的代理对象
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.mapperRegistry.getMapper(type, sqlSession);
    }

    //判断type这个Mapper接口类是否已注册
    public boolean hasMapper(Class<?> type) {
        return this.mapperRegistry.hasMapper(type);
    }

    public boolean hasStatement(String statementName) {
        return hasStatement(statementName, true);
    }

    public boolean hasStatement(String statementName, boolean validateIncompleteStatements) {
        if (validateIncompleteStatements) {
            buildAllStatements();
        }
        return this.mappedStatements.containsKey(statementName);
    }

    public void addCacheRef(String namespace, String referencedNamespace) {
        this.cacheRefMap.put(namespace, referencedNamespace);
    }

    protected void buildAllStatements()
    {
        if (!this.incompleteResultMaps.isEmpty()) {
            synchronized (this.incompleteResultMaps)
            {
                ((ResultMapResolver)this.incompleteResultMaps.iterator().next()).resolve();
            }
        }
        if (!this.incompleteCacheRefs.isEmpty()) {
            synchronized (this.incompleteCacheRefs)
            {
                ((CacheRefResolver)this.incompleteCacheRefs.iterator().next()).resolveCacheRef();
            }
        }
        if (!this.incompleteStatements.isEmpty()) {
            synchronized (this.incompleteStatements)
            {
                ((XMLStatementBuilder)this.incompleteStatements.iterator().next()).parseStatementNode();
            }
        }
        if (!this.incompleteMethods.isEmpty())
            synchronized (this.incompleteMethods)
            {
                ((MethodResolver)this.incompleteMethods.iterator().next()).resolve();
            }
    }

    protected String extractNamespace(String statementId)
    {
        int lastPeriod = statementId.lastIndexOf('.');
        return lastPeriod > 0 ? statementId.substring(0, lastPeriod) : null;
    }

    protected static class StrictMap<V> extends HashMap<String, V> {
        private static final long serialVersionUID = -4950446264854982944L;
        private final String name;

        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity,loadFactor);
            this.name = name;
        }

        public StrictMap(String name, int initialCapacity) {
            super();
            this.name = name;
        }

        public StrictMap(String name)
        {
            this.name = name;
        }

        public StrictMap(String name, Map<String, ? extends V> m) {
            super();
            this.name = name;
        }

        public V put(String key, V value)
        {
            if (containsKey(key)) {
                throw new IllegalArgumentException(this.name + " already contains value for " + key);
            }
            if (key.contains(".")) {
                String shortKey = getShortName(key);
                if (super.get(shortKey) == null)
                    super.put(shortKey, value);
                else {

                    V v = (V) new Ambiguity(shortKey);
                    super.put(shortKey,v);
                }
            }
            return super.put(key, value);
        }

        public V get(Object key) {
            Object value = super.get(key);
            if (value == null) {
                throw new IllegalArgumentException(this.name + " does not contain value for " + key);
            }
            if ((value instanceof Ambiguity)) {
                throw new IllegalArgumentException(((Ambiguity)value).getSubject() + " is ambiguous in " + this.name +
                        " (try using the full name including the namespace, or rename one of the entries)");
            }

            return (V) value;
        }

        private String getShortName(String key) {
            String[] keyParts = key.split("\\.");
            return keyParts[(keyParts.length - 1)];
        }
        protected static class Ambiguity {
            private final String subject;

            public Ambiguity(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return this.subject;
            }
        }
    }
}
