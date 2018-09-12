package com.mybatis.pseudocode.mybatis.builder.xml;


import com.mybatis.pseudocode.mybatis.builder.BaseBuilder;
import com.mybatis.pseudocode.mybatis.builder.BuilderException;
import com.mybatis.pseudocode.mybatis.mapping.JdbcType;
import com.mybatis.pseudocode.mybatis.plugin.Interceptor;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.ReflectorFactory;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

//解析SqlMapConfig.xml或mybatis-config.xml
public class XMLConfigBuilder extends BaseBuilder {
    private boolean parsed;
    private final XPathParser parser;
    private String environment;
    private final ReflectorFactory localReflectorFactory = new DefaultReflectorFactory();

    public XMLConfigBuilder(Reader reader) {
        this(reader, null, null);
    }

    public XMLConfigBuilder(Reader reader, String environment) {
        this(reader, environment, null);
    }

    public XMLConfigBuilder(Reader reader, String environment, Properties props) {
        this(new XPathParser(reader, true, props, new XMLMapperEntityResolver()), environment, props);
    }

    public XMLConfigBuilder(InputStream inputStream) {
        this(inputStream, null, null);
    }

    public XMLConfigBuilder(InputStream inputStream, String environment) {
        this(inputStream, environment, null);
    }

    public XMLConfigBuilder(InputStream inputStream, String environment, Properties props) {
        this(new XPathParser(inputStream, true, props, new XMLMapperEntityResolver()), environment, props);
    }

    private XMLConfigBuilder(XPathParser parser, String environment, Properties props) {
        super(new Configuration());
        //ErrorContext.instance().resource("SQL Mapper Configuration");
        this.configuration.setVariables(props);
        this.parsed = false;
        this.environment = environment;
        this.parser = parser;
    }

    public Configuration parse() {
        if (this.parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once.");
        }
        this.parsed = true;
        parseConfiguration(this.parser.evalNode("/configuration"));
        return this.configuration;
    }

    private void parseConfiguration(XNode root) {
        try {
            // 解析<properties>节点
            //propertiesElement(root.evalNode("properties"));
            // 解析<settings>节点
            //Properties settings = settingsAsProperties(root.evalNode("settings"));
            //loadCustomVfs(settings);
            // 解析<typeAliases>节点
            typeAliasesElement(root.evalNode("typeAliases"));
            // 解析<plugins>节点
            pluginElement(root.evalNode("plugins"));
            // 解析<objectFactory>节点
            //objectFactoryElement(root.evalNode("objectFactory"));
            //objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
            // 解析<reflectorFactory>节点
            //reflectorFactoryElement(root.evalNode("reflectorFactory"));
            //settingsElement(settings);

            // 解析<environments>节点
            //environmentsElement(root.evalNode("environments"));
            //databaseIdProviderElement(root.evalNode("databaseIdProvider"));
            // 解析<typeHandlers>节点
            typeHandlerElement(root.evalNode("typeHandlers"));
            // 解析<mappers>节点
            mapperElement(root.evalNode("mappers"));
        } catch (Exception e) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
    }

    //解析<typeAliases>节点
    private void typeAliasesElement(XNode parent)
    {
        if(parent==null) {
            return ;
        }
        for (XNode child : parent.getChildren()) {
            if ("package".equals(child.getName())) {
                String typeAliasPackage = child.getStringAttribute("name");
                this.configuration.getTypeAliasRegistry().registerAliases(typeAliasPackage);
            } else {
                String alias = child.getStringAttribute("alias");
                String type = child.getStringAttribute("type");
                try {
                    Class clazz = Resources.classForName(type);
                    //直接通过别名注册器来注册别名
                    if (alias == null)
                        this.typeAliasRegistry.registerAlias(clazz);
                    else
                        this.typeAliasRegistry.registerAlias(alias, clazz);
                } catch (ClassNotFoundException e) {
                    throw new BuilderException("Error registering typeAlias for '" + alias + "'. Cause: " + e, e);
                }
            }
        }
    }

    //解析<plugins>节点
    private void pluginElement(XNode parent) throws Exception
    {
        if(parent==null) {
            return ;
        }
        for (XNode child : parent.getChildren()) {
            String interceptor = child.getStringAttribute("interceptor");
            Properties properties = child.getChildrenAsProperties();
            Interceptor interceptorInstance = (Interceptor)resolveClass(interceptor).newInstance();
            interceptorInstance.setProperties(properties);
            //添加拦截器
            this.configuration.addInterceptor(interceptorInstance);
        }
    }

    // 解析<typeHandlers>节点
    private void typeHandlerElement(XNode parent) throws Exception {
        if(parent==null) {
            return ;
        }
        for (XNode child : parent.getChildren()) {
            if ("package".equals(child.getName())) {
                String typeHandlerPackage = child.getStringAttribute("name");
                //注册包下面所有实现了TypeHandler接口的typeHandler类
                this.typeHandlerRegistry.register(typeHandlerPackage);
            } else {
                String javaTypeName = child.getStringAttribute("javaType");
                String jdbcTypeName = child.getStringAttribute("jdbcType");
                String handlerTypeName = child.getStringAttribute("handler");
                Class javaTypeClass = resolveClass(javaTypeName);
                JdbcType jdbcType = resolveJdbcType(jdbcTypeName);
                Class typeHandlerClass = resolveClass(handlerTypeName);
                if (javaTypeClass != null) {
                    if (jdbcType == null)
                        this.typeHandlerRegistry.register(javaTypeClass, typeHandlerClass);
                    else
                        this.typeHandlerRegistry.register(javaTypeClass, jdbcType, typeHandlerClass);
                } else
                    this.typeHandlerRegistry.register(typeHandlerClass);
            }
        }
    }

    //解析<mappers>节点
    private void mapperElement(XNode parent) throws Exception
    {
        if(parent==null) {
            return ;
        }
        for (XNode child : parent.getChildren()) {
            if ("package".equals(child.getName())) {
                String mapperPackage = child.getStringAttribute("name");
                //注册某个包下面所有的Mapper接口类
                this.configuration.addMappers(mapperPackage);
            } else {
                String resource = child.getStringAttribute("resource");
                String url = child.getStringAttribute("url");
                String mapperClass = child.getStringAttribute("class");
                if ((resource != null) && (url == null) && (mapperClass == null)) {
                    //ErrorContext.instance().resource(resource);
                    InputStream inputStream = Resources.getResourceAsStream(resource);
                    //交由XMLMapperBuilder解析mapper
                    XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, this.configuration, resource, this.configuration.getSqlFragments());
                    mapperParser.parse();
                } else if ((resource == null) && (url != null) && (mapperClass == null)) {
                    //ErrorContext.instance().resource(url);
                    InputStream inputStream = Resources.getUrlAsStream(url);
                    //交由XMLMapperBuilder解析mapper
                    XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, this.configuration, url, this.configuration.getSqlFragments());
                    mapperParser.parse();
                } else if ((resource == null) && (url == null) && (mapperClass != null)) {
                    Class mapperInterface = Resources.classForName(mapperClass);
                    //增加mapper接口注册
                    this.configuration.addMapper(mapperInterface);
                } else {
                    throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
                }
            }
        }
    }
}
