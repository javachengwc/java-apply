package com.mybatis.pseudocode.mybatis.executor.kengen;

import com.mybatis.pseudocode.mybatis.binding.BindingException;
import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.executor.ExecutorException;
import com.mybatis.pseudocode.mybatis.mapping.JdbcType;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;
import org.apache.ibatis.reflection.MetaObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

//Jdbc3KeyGenerator对insert语句自动取回表的自增Id
public class Jdbc3KeyGenerator implements KeyGenerator
{
    public static final Jdbc3KeyGenerator INSTANCE = new Jdbc3KeyGenerator();

    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter)
    {
    }

    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter)
    {
        processBatch(ms, stmt, getParameters(parameter));
    }

    public void processBatch(MappedStatement ms, Statement stmt, Collection<Object> parameters) {
        ResultSet rs = null;
        try {
            //调用java.sql.Statement.getGeneratedKeys（）获取自动生成的主键创建此stmt对象执行的结果
            //如果此Statement对象没有产生任何键，则返回空的ResultSet对象。
            rs = stmt.getGeneratedKeys();
            Configuration configuration = ms.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            String[]  keyProperties = ms.getKeyProperties();
            ResultSetMetaData rsmd = rs.getMetaData();
            TypeHandler [] typeHandlers = null;
            if ((keyProperties != null) && (rsmd.getColumnCount() >= keyProperties.length))
                for (Iterator localIterator = parameters.iterator(); localIterator.hasNext(); ) { Object parameter = localIterator.next();
                    if (!rs.next()) {
                        break;
                    }
                    //MetaObject metaParam = configuration.newMetaObject(parameter);
                    MetaObject metaParam = null;
                    if (typeHandlers == null) {
                        typeHandlers = getTypeHandlers(typeHandlerRegistry, metaParam, keyProperties, rsmd);
                    }
                    populateKeys(rs, metaParam, keyProperties, typeHandlers);
                }
        }
        catch (Exception e)
        {
            Configuration configuration;
            TypeHandlerRegistry typeHandlerRegistry;
            String[] keyProperties;
            ResultSetMetaData rsmd;
            TypeHandler[] typeHandlers;
            Iterator localIterator;
            throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + e, e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                }
                catch (Exception localException2)
                {
                }
        }
    }

    private Collection<Object> getParameters(Object parameter) {
        Collection parameters = null;
        if ((parameter instanceof Collection)) {
            parameters = (Collection)parameter;
        } else if ((parameter instanceof Map)) {
            Map parameterMap = (Map)parameter;
            if (parameterMap.containsKey("collection"))
                parameters = (Collection)parameterMap.get("collection");
            else if (parameterMap.containsKey("list"))
                parameters = (List)parameterMap.get("list");
            else if (parameterMap.containsKey("array")) {
                parameters = Arrays.asList((Object[])(Object[])parameterMap.get("array"));
            }
        }
        if (parameters == null) {
            parameters = new ArrayList();
            parameters.add(parameter);
        }
        return parameters;
    }

    private TypeHandler<?>[] getTypeHandlers(TypeHandlerRegistry typeHandlerRegistry, MetaObject metaParam, String[] keyProperties,
                                             ResultSetMetaData rsmd) throws SQLException {
        TypeHandler[] typeHandlers = new TypeHandler[keyProperties.length];
        for (int i = 0; i < keyProperties.length; i++) {
            if (!metaParam.hasSetter(keyProperties[i])) continue; TypeHandler th;
            try {
                Class keyPropertyType = metaParam.getSetterType(keyProperties[i]);
                th = typeHandlerRegistry.getTypeHandler(keyPropertyType, JdbcType.forCode(rsmd.getColumnType(i + 1)));
            }
            catch (BindingException e)
            {
            }
        }

        return typeHandlers;
    }

    private void populateKeys(ResultSet rs, MetaObject metaParam, String[] keyProperties, TypeHandler<?>[] typeHandlers) throws SQLException {
        for (int i = 0; i < keyProperties.length; i++) {
            String property = keyProperties[i];
            TypeHandler th = typeHandlers[i];
            if (th != null) {
                Object value = th.getResult(rs, i + 1);
                metaParam.setValue(property, value);
            }
        }
    }
}
