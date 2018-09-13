package com.mybatis.pseudocode.mybatisspring;

import com.mybatis.pseudocode.mybatis.builder.xml.XMLConfigBuilder;
import com.mybatis.pseudocode.mybatis.builder.xml.XMLMapperBuilder;
import com.mybatis.pseudocode.mybatis.cache.Cache;
import com.mybatis.pseudocode.mybatis.mapping.Environment;
import com.mybatis.pseudocode.mybatis.plugin.Interceptor;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.SqlSessionFactory;
import com.mybatis.pseudocode.mybatis.session.SqlSessionFactoryBuilder;
import com.mybatis.pseudocode.mybatis.transaction.TransactionFactory;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import com.mybatis.pseudocode.mybatisspring.transaction.SpringManagedTransactionFactory;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

//<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
//    <property name="dataSource" ref="dataSource"/>
//    <property name="configLocation" value="classpath:mybatis/mybatis-config.xml"/>
//    <property name="mapperLocations">
//    <array>
//        <value>classpath*:mybatis/sqlmap/shop/*.xml</value>
//        <value>classpath*:mybatis/sqlmap/shop/ext/*.xml</value>
//    </array>
//    </property>
//</bean>
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent>
{
    private static final Log LOGGER = LogFactory.getLog(SqlSessionFactoryBean.class);

    //mybatis全局配置文件的配置信息
    private Resource configLocation;

    private Configuration configuration;

    //mapper.xml文件位置
    private Resource[] mapperLocations;
    //数据源
    private DataSource dataSource;
    //事务工厂
    private TransactionFactory transactionFactory;

    private Properties configurationProperties;
    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    private SqlSessionFactory sqlSessionFactory;

    private String environment = SqlSessionFactoryBean.class.getSimpleName();

    private boolean failFast;

    private Interceptor[] plugins;


    private TypeHandler<?>[] typeHandlers;

    private String typeHandlersPackage;

    private Class<?>[] typeAliases;

    private String typeAliasesPackage;

    private Class<?> typeAliasesSuperType;

    private DatabaseIdProvider databaseIdProvider;

    private Cache cache;

    private ObjectFactory objectFactory;

    private ObjectWrapperFactory objectWrapperFactory;

    public void setObjectFactory(ObjectFactory objectFactory)
    {
        this.objectFactory = objectFactory;
    }

    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory)
    {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    public DatabaseIdProvider getDatabaseIdProvider()
    {
        return this.databaseIdProvider;
    }

    public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider)
    {
        this.databaseIdProvider = databaseIdProvider;
    }


    public Cache getCache() {
        return this.cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setPlugins(Interceptor[] plugins)
    {
        this.plugins = plugins;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage)
    {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType)
    {
        this.typeAliasesSuperType = typeAliasesSuperType;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage)
    {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public void setTypeHandlers(TypeHandler<?>[] typeHandlers)
    {
        this.typeHandlers = typeHandlers;
    }

    public void setTypeAliases(Class<?>[] typeAliases)
    {
        this.typeAliases = typeAliases;
    }

    public void setFailFast(boolean failFast)
    {
        this.failFast = failFast;
    }

    public void setConfigLocation(Resource configLocation)
    {
        this.configLocation = configLocation;
    }

    public void setConfiguration(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public void setMapperLocations(Resource[] mapperLocations)
    {
        this.mapperLocations = mapperLocations;
    }

    public void setConfigurationProperties(Properties sqlSessionFactoryProperties)
    {
        this.configurationProperties = sqlSessionFactoryProperties;
    }

    public void setDataSource(DataSource dataSource)
    {
        if ((dataSource instanceof TransactionAwareDataSourceProxy))
        {
            this.dataSource = ((TransactionAwareDataSourceProxy)dataSource).getTargetDataSource();
        }
        else this.dataSource = dataSource;
    }

    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder)
    {
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory)
    {
        this.transactionFactory = transactionFactory;
    }

    public void setEnvironment(String environment)
    {
        this.environment = environment;
    }

    public void afterPropertiesSet() throws Exception
    {
        this.sqlSessionFactory = buildSqlSessionFactory();
    }

    protected SqlSessionFactory buildSqlSessionFactory() throws IOException
    {
        XMLConfigBuilder xmlConfigBuilder = null;
        Configuration configuration;
        if (this.configuration != null) {
            configuration = this.configuration;
            if (configuration.getVariables() == null)
                configuration.setVariables(this.configurationProperties);
            else if (this.configurationProperties != null)
                configuration.getVariables().putAll(this.configurationProperties);
        }
        else
        {
            if (this.configLocation != null) {
                xmlConfigBuilder = new XMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
                configuration = xmlConfigBuilder.getConfiguration();
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration");
                }
                configuration = new Configuration();
                if (this.configurationProperties != null) {
                    configuration.setVariables(this.configurationProperties);
                }
            }
        }
        if (this.objectFactory != null) {
            configuration.setObjectFactory(this.objectFactory);
        }

        if (this.objectWrapperFactory != null) {
            configuration.setObjectWrapperFactory(this.objectWrapperFactory);
        }
        //...
        // configuration.getTypeAliasRegistry().registerAliases(packageToScan, this.typeAliasesSuperType == null ? Object.class : this.typeAliasesSuperType);
        //...
        // configuration.getTypeAliasRegistry().registerAlias((Class)typeAlias);
        //...
        //configuration.addInterceptor((Interceptor)plugin);
        //...
        //configuration.getTypeHandlerRegistry().register(packageToScan);
        //...
        //configuration.getTypeHandlerRegistry().register((TypeHandler)typeHandler);

        if (this.cache != null) {
            configuration.addCache(this.cache);
        }

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Parsed configuration file: '" + this.configLocation + "'");
            }
            catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
            } finally {
                //ErrorContext.instance().reset();
            }
        }

        if (this.transactionFactory == null) {
            this.transactionFactory = new SpringManagedTransactionFactory();
        }

        //设置环境，数据源，事务工厂
        configuration.setEnvironment(new Environment(this.environment, this.transactionFactory, this.dataSource));

        if (!ObjectUtils.isEmpty(this.mapperLocations)) {
            for (Resource mapperLocation : this.mapperLocations) {
                if (mapperLocation == null)
                {
                    continue;
                }
                try
                {
                    //mapper.xml文件解析
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(), configuration,
                            mapperLocation.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (Exception e) {
                    throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
                } finally {
                    //ErrorContext.instance().reset();
                }
            }
        }
        return (SqlSessionFactory)(SqlSessionFactory)(SqlSessionFactory)this.sqlSessionFactoryBuilder.build(configuration);
    }

    public SqlSessionFactory getObject() throws Exception
    {
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }
        return this.sqlSessionFactory;
    }

    public Class<? extends SqlSessionFactory> getObjectType()
    {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    public boolean isSingleton()
    {
        return true;
    }

    public void onApplicationEvent(ApplicationEvent event)
    {
        if ((this.failFast) && ((event instanceof ContextRefreshedEvent)))
        {
            this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
        }
    }
}