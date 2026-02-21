package com.boot.pseudocode.autoconfigure.mongo;

import com.mongodb.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix="spring.data.mongodb")
public class MongoProperties
{
    public static final int DEFAULT_PORT = 27017;
    public static final String DEFAULT_URI = "mongodb://localhost/test";
    private String host;
    private Integer port = null;
    private String uri;
    private String database;
    private String authenticationDatabase;
    private String gridFsDatabase;
    private String username;
    private char[] password;
    private Class<?> fieldNamingStrategy;

    public String getHost()
    {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getAuthenticationDatabase() {
        return this.authenticationDatabase;
    }

    public void setAuthenticationDatabase(String authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public Class<?> getFieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }

    public void setFieldNamingStrategy(Class<?> fieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy;
    }

    public void clearPassword() {
        if (this.password == null) {
            return;
        }
        for (int i = 0; i < this.password.length; i++)
            this.password[i] = '\000';
    }

    public String getUri()
    {
        return this.uri;
    }

    public String determineUri() {
        return this.uri != null ? this.uri : "mongodb://localhost/test";
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getGridFsDatabase() {
        return this.gridFsDatabase;
    }

    public void setGridFsDatabase(String gridFsDatabase) {
        this.gridFsDatabase = gridFsDatabase;
    }

    public String getMongoClientDatabase() {
        if (this.database != null) {
            return this.database;
        }
        return new MongoClientURI(determineUri()).getDatabase();
    }

    public MongoClient createMongoClient(MongoClientOptions options, Environment environment) throws UnknownHostException
    {
        try
        {
            Integer embeddedPort = getEmbeddedPort(environment);
            if (embeddedPort != null) {
                MongoClient localMongoClient = createEmbeddedMongoClient(options, embeddedPort.intValue());
                return localMongoClient;
            }
            MongoClient localMongoClient = createNetworkMongoClient(options);
            return localMongoClient;
        }
        finally {
            clearPassword();
        }
    }

    private Integer getEmbeddedPort(Environment environment)
    {
        if (environment != null) {
            String localPort = environment.getProperty("local.mongo.port");
            if (localPort != null) {
                return Integer.valueOf(localPort);
            }
        }
        return null;
    }

    private MongoClient createEmbeddedMongoClient(MongoClientOptions options, int port) {
        if (options == null) {
            options = MongoClientOptions.builder().build();
        }
        String host = this.host == null ? "localhost" : this.host;
        //return new MongoClient(Collections.singletonList(new ServerAddress(host, port)), Collections.emptyList(), options);
        return null;
    }

    private MongoClient createNetworkMongoClient(MongoClientOptions options) {
        if ((hasCustomAddress()) || (hasCustomCredentials())) {
            if (this.uri != null) {
                throw new IllegalStateException("Invalid mongo configuration, either uri or host/port/credentials must be specified");
            }

            if (options == null) {
                options = MongoClientOptions.builder().build();
            }
            List credentials = new ArrayList();
            if (hasCustomCredentials())
            {
                String database = this.authenticationDatabase == null ?
                        getMongoClientDatabase() : this.authenticationDatabase;
                credentials.add(MongoCredential.createCredential(this.username, database, this.password));
            }

            String host = this.host == null ? "localhost" : this.host;
            int port = this.port != null ? this.port.intValue() : 27017;
            return  new MongoClient(Collections.singletonList(new ServerAddress(host, port)),
                            credentials, options);
        }

        return new MongoClient(new MongoClientURI(determineUri(), builder(options)));
    }

    private boolean hasCustomAddress() {
        return (this.host != null) || (this.port != null);
    }

    private boolean hasCustomCredentials() {
        return (this.username != null) && (this.password != null);
    }

    private MongoClientOptions.Builder builder(MongoClientOptions options) {
        if (options != null) {
            return MongoClientOptions.builder(options);
        }
        return MongoClientOptions.builder();
    }
}
