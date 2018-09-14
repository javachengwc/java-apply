package com.boot.pseudocode.autoconfigure.jdbc;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix="spring.datasource")
public class DataSourceProperties implements BeanClassLoaderAware, EnvironmentAware, InitializingBean
{
    private ClassLoader classLoader;
    private Environment environment;
    private String name = "testdb";
    private boolean generateUniqueName;
    private Class<? extends DataSource> type;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String jndiName;
    private boolean initialize = true;

    private String platform = "all";
    private List<String> schema;
    private String schemaUsername;
    private String schemaPassword;
    private List<String> data;
    private String dataUsername;
    private String dataPassword;
    private boolean continueOnError = false;

    private String separator = ";";
    private Charset sqlScriptEncoding;
    private EmbeddedDatabaseConnection embeddedDatabaseConnection = EmbeddedDatabaseConnection.NONE;

    private Xa xa = new Xa();
    private String uniqueName;

    public void setBeanClassLoader(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }

    public void afterPropertiesSet() throws Exception
    {
        this.embeddedDatabaseConnection = EmbeddedDatabaseConnection.get(this.classLoader);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends DataSource> getType() {
        return this.type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public Xa getXa() {
        return this.xa;
    }

    public void setXa(Xa xa) {
        this.xa = xa;
    }

    public static class Xa
    {
        private String dataSourceClassName;
        private Map<String, String> properties = new LinkedHashMap();

        public String getDataSourceClassName() {
            return this.dataSourceClassName;
        }

        public void setDataSourceClassName(String dataSourceClassName) {
            this.dataSourceClassName = dataSourceClassName;
        }

        public Map<String, String> getProperties() {
            return this.properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
