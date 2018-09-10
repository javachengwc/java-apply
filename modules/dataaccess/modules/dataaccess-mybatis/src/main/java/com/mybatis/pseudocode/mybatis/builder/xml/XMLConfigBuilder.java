package com.mybatis.pseudocode.mybatis.builder.xml;


import com.mybatis.pseudocode.mybatis.builder.BaseBuilder;
import com.mybatis.pseudocode.mybatis.builder.BuilderException;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
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
            //propertiesElement(root.evalNode("properties"));
            //Properties settings = settingsAsProperties(root.evalNode("settings"));
            //loadCustomVfs(settings);
            //typeAliasesElement(root.evalNode("typeAliases"));
            //pluginElement(root.evalNode("plugins"));
            //objectFactoryElement(root.evalNode("objectFactory"));
            //objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
            //reflectorFactoryElement(root.evalNode("reflectorFactory"));
            //settingsElement(settings);

            //environmentsElement(root.evalNode("environments"));
            //databaseIdProviderElement(root.evalNode("databaseIdProvider"));
            //typeHandlerElement(root.evalNode("typeHandlers"));
            //mapperElement(root.evalNode("mappers"));
        } catch (Exception e) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
    }
}
