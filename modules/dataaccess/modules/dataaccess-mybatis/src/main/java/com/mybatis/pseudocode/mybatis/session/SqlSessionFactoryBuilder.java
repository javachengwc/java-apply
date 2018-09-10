package com.mybatis.pseudocode.mybatis.session;

import com.mybatis.pseudocode.mybatis.builder.xml.XMLConfigBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

//使用mabatis入口
public class SqlSessionFactoryBuilder
{
    public SqlSessionFactory build(Reader reader)
    {
        return build(reader, null, null);
    }

    public SqlSessionFactory build(Reader reader, String environment) {
        return build(reader, environment, null);
    }

    public SqlSessionFactory build(Reader reader, Properties properties) {
        return build(reader, null, properties);
    }

    public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
        try {
            XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
            SqlSessionFactory localSqlSessionFactory = build(parser.parse());
            return localSqlSessionFactory;
        } catch (Exception e) {
            //throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
           // ErrorContext.instance().reset();
            try {
                reader.close();
            } catch (IOException ioException) {

            }
        }
        return  null;
    }

    //使用mavbatis框架入口
    public SqlSessionFactory build(InputStream inputStream) {
        return build(inputStream, null, null);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment) {
        return build(inputStream, environment, null);
    }

    public SqlSessionFactory build(InputStream inputStream, Properties properties) {
        return build(inputStream, null, properties);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
        try {
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
            SqlSessionFactory localSqlSessionFactory = build(parser.parse());
            return localSqlSessionFactory;
        } catch (Exception e) {
            //throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            //ErrorContext.instance().reset();
            try {
                inputStream.close();
            } catch (IOException localIOException1) {
            }
        }
        return null;
    }

    public SqlSessionFactory build(Configuration config) {
        //return new DefaultSqlSessionFactory(config);
        return null;
    }
}
