package com.mybatis.pseudocode.mybatis.binding;


import com.mybatis.pseudocode.mybatis.annotations.MapKey;
import com.mybatis.pseudocode.mybatis.annotations.ResultMap;
import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.mapping.SqlCommandType;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.session.SqlSession;
import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.reflection.TypeParameterResolver;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//MapperMethod代表Mapper接口中的一个Method
public class MapperMethod
{
    private final SqlCommand command;
    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration config)
    {
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, mapperInterface, method);
    }

    //Mapper接口中的一个Method的执行，就是对应的一个增删改查sql
    public Object execute(SqlSession sqlSession, Object[] args)
    {
        Object result;
        switch (command.getType()) {
            case INSERT: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.insert(command.getName(), param));
                break;
            }
            case UPDATE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.update(command.getName(), param));
                break;
            }
            case DELETE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.delete(command.getName(), param));
                break;
            }
            case SELECT:
                if (method.returnsVoid() && method.hasResultHandler()) {
                    //如果有查询结果后置处理器
                    executeWithResultHandler(sqlSession, args);
                    result = null;
                } else if (method.returnsMany()) {
                    result = executeForMany(sqlSession, args);
                } else if (method.returnsMap()) {
                    result = executeForMap(sqlSession, args);
                } else if (method.returnsCursor()) {
                    result = executeForCursor(sqlSession, args);
                } else {
                    Object param = method.convertArgsToSqlCommandParam(args);
                    result = sqlSession.selectOne(command.getName(), param);
                }
                break;
            case FLUSH:
                result = sqlSession.flushStatements();
                break;
            default:
                throw new org.apache.ibatis.binding.BindingException("Unknown execution method for: " + command.getName());
        }
        if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
            throw new org.apache.ibatis.binding.BindingException("Mapper method '" + command.getName()
                    + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
        }
        return result;
    }

    private Object rowCountResult(int rowCount)
    {
        Object result;
        if (this.method.returnsVoid()) {
            result = null;
        }
        else
        {
            if ((Integer.class.equals(this.method.getReturnType())) || (Integer.TYPE.equals(this.method.getReturnType()))) {
                result = Integer.valueOf(rowCount);
            }
            else
            {
                if ((Long.class.equals(this.method.getReturnType())) || (Long.TYPE.equals(this.method.getReturnType()))) {
                    result = Long.valueOf(rowCount);
                }
                else
                {
                    if ((Boolean.class.equals(this.method.getReturnType())) || (Boolean.TYPE.equals(this.method.getReturnType())))
                        result = Boolean.valueOf(rowCount > 0);
                    else
                        throw new BindingException("Mapper method '" + this.command.getName() + "' has an unsupported return type: " + this.method.getReturnType());
                }
            }
        }
        return result;
    }

    private void executeWithResultHandler(SqlSession sqlSession, Object[] args) {
        MappedStatement ms = sqlSession.getConfiguration().getMappedStatement(this.command.getName());
//        if (Void.TYPE.equals(((ResultMap)ms.getResultMaps().get(0)).getType())) {
//            throw new BindingException("method " + this.command.getName() + " needs either a @ResultMap annotation, " +
//                    "a @ResultType annotation, or a resultType attribute in XML so a ResultHandler can be used as a parameter.");
//        }

        Object param = this.method.convertArgsToSqlCommandParam(args);
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            sqlSession.select(this.command.getName(), param, rowBounds, this.method.extractResultHandler(args));
        } else {
            sqlSession.select(this.command.getName(), param, this.method.extractResultHandler(args));
        }
    }

    //查询
    private <E> Object executeForMany(SqlSession sqlSession, Object[] args)
    {
        Object param = this.method.convertArgsToSqlCommandParam(args);
        List result;
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            //command.getName() 是对应的查询id
            result = sqlSession.selectList(this.command.getName(), param, rowBounds);
        } else {
            result = sqlSession.selectList(this.command.getName(), param);
        }

        if (!this.method.getReturnType().isAssignableFrom(result.getClass())) {
            if (this.method.getReturnType().isArray()) {
                return convertToArray(result);
            }
            return convertToDeclaredCollection(sqlSession.getConfiguration(), result);
        }

        return result;
    }

    private <T> Cursor<T> executeForCursor(SqlSession sqlSession, Object[] args)
    {
        Object param = this.method.convertArgsToSqlCommandParam(args);
        Cursor result;
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            result = sqlSession.selectCursor(this.command.getName(), param, rowBounds);
        } else {
            result = sqlSession.selectCursor(this.command.getName(), param);
        }
        return result;
    }

    private <E> Object convertToDeclaredCollection(Configuration config, List<E> list) {
        Object collection = config.getObjectFactory().create(this.method.getReturnType());
        //MetaObject metaObject = config.newMetaObject(collection);
        MetaObject metaObject =null;
        metaObject.addAll(list);
        return collection;
    }

    private <E> Object convertToArray(List<E> list)
    {
        Class arrayComponentType = this.method.getReturnType().getComponentType();
        Object array = Array.newInstance(arrayComponentType, list.size());
        if (arrayComponentType.isPrimitive()) {
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, list.get(i));
            }
            return array;
        }
        return list.toArray((Object[])(Object[])array);
    }

    private <K, V> Map<K, V> executeForMap(SqlSession sqlSession, Object[] args)
    {
        Object param = this.method.convertArgsToSqlCommandParam(args);
        Map result;
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            result = sqlSession.selectMap(this.command.getName(), param, this.method.getMapKey(), rowBounds);
        } else {
            result = sqlSession.selectMap(this.command.getName(), param, this.method.getMapKey());
        }
        return result;
    }

    public static class MethodSignature
    {
        private final boolean returnsMany;

        private final boolean returnsMap;

        private final boolean returnsVoid;

        private final boolean returnsCursor;

        private final Class<?> returnType;

        private final String mapKey;

        //如果有查询结果的后置处理器，此值是那后置处理器在代理方法的参数中的下标位置
        private final Integer resultHandlerIndex;

        private final Integer rowBoundsIndex;

        //private final ParamNameResolver paramNameResolver;

        public MethodSignature(Configuration configuration, Class<?> mapperInterface, Method method)
        {
            //获取返回类型
            Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface);
            if ((resolvedReturnType instanceof Class))
                this.returnType = ((Class)resolvedReturnType);
            else if ((resolvedReturnType instanceof ParameterizedType))
                this.returnType = ((Class)((ParameterizedType)resolvedReturnType).getRawType());
            else {
                this.returnType = method.getReturnType();
            }
            this.returnsVoid = Void.TYPE.equals(this.returnType);
            this.returnsMany = ((configuration.getObjectFactory().isCollection(this.returnType)) || (this.returnType.isArray()));
            this.returnsCursor = Cursor.class.equals(this.returnType);
            this.mapKey = getMapKey(method);
            this.returnsMap = (this.mapKey != null);
            this.rowBoundsIndex = getUniqueParamIndex(method, RowBounds.class);
            this.resultHandlerIndex = getUniqueParamIndex(method, ResultHandler.class);
            //this.paramNameResolver = new ParamNameResolver(configuration, method);
        }

        //把参数转换成sql命令中的参数
        public Object convertArgsToSqlCommandParam(Object[] args) {
            //return this.paramNameResolver.getNamedParams(args);
            return null;
        }

        public boolean hasRowBounds() {
            return this.rowBoundsIndex != null;
        }

        public RowBounds extractRowBounds(Object[] args) {
            return hasRowBounds() ? (RowBounds)args[this.rowBoundsIndex.intValue()] : null;
        }

        public boolean hasResultHandler() {
            return this.resultHandlerIndex != null;
        }

        public ResultHandler extractResultHandler(Object[] args) {
            return hasResultHandler() ? (ResultHandler)args[this.resultHandlerIndex.intValue()] : null;
        }

        public String getMapKey() {
            return this.mapKey;
        }

        public Class<?> getReturnType() {
            return this.returnType;
        }

        public boolean returnsMany() {
            return this.returnsMany;
        }

        public boolean returnsMap() {
            return this.returnsMap;
        }

        public boolean returnsVoid() {
            return this.returnsVoid;
        }

        public boolean returnsCursor() {
            return this.returnsCursor;
        }

        private Integer getUniqueParamIndex(Method method, Class<?> paramType) {
            Integer index = null;
            Class[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                if (paramType.isAssignableFrom(argTypes[i])) {
                    if (index == null)
                        index = Integer.valueOf(i);
                    else {
                        throw new BindingException(method.getName() + " cannot have multiple " + paramType.getSimpleName() + " parameters");
                    }
                }
            }
            return index;
        }

        private String getMapKey(Method method) {
            String mapKey = null;
            if (Map.class.isAssignableFrom(method.getReturnType())) {
                MapKey mapKeyAnnotation = (MapKey)method.getAnnotation(MapKey.class);
                if (mapKeyAnnotation != null) {
                    mapKey = mapKeyAnnotation.value();
                }
            }
            return mapKey;
        }
    }

    public static class SqlCommand
    {
        //对应statment语句id
        private final String name;

        //sql增删改查类型
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method)
        {
            String methodName = method.getName();
            //方法定义所在的类
            Class declaringClass = method.getDeclaringClass();
            MappedStatement ms = resolveMappedStatement(mapperInterface, methodName, declaringClass, configuration);

            if (ms == null) {
                if (method.getAnnotation(Flush.class) != null) {
                    this.name = null;
                    this.type = SqlCommandType.FLUSH;
                }
                else {
                    throw new BindingException("Invalid bound statement (not found): " + mapperInterface
                            .getName() + "." + methodName);
                }
            } else {
                this.name = ms.getId();
                this.type = ms.getSqlCommandType();
                if (this.type == SqlCommandType.UNKNOWN)
                    throw new BindingException("Unknown execution method for: " + this.name);
            }
        }

        public String getName()
        {
            return this.name;
        }

        public SqlCommandType getType() {
            return this.type;
        }

        private MappedStatement resolveMappedStatement(Class<?> mapperInterface, String methodName, Class<?> declaringClass, Configuration configuration)
        {
            String statementId = mapperInterface.getName() + "." + methodName;
            if (configuration.hasStatement(statementId))
                return configuration.getMappedStatement(statementId);
            if (mapperInterface.equals(declaringClass)) {
                return null;
            }
            for (Class superInterface : mapperInterface.getInterfaces()) {
                if (declaringClass.isAssignableFrom(superInterface)) {
                    MappedStatement ms = resolveMappedStatement(superInterface, methodName, declaringClass, configuration);

                    if (ms != null) {
                        return ms;
                    }
                }
            }
            return null;
        }
    }

    public static class ParamMap<V> extends HashMap<String, V>
    {
        private static final long serialVersionUID = -2212268410513556L;

        public V get(Object key)
        {
            if (!super.containsKey(key)) {
                throw new BindingException("Parameter '" + key + "' not found. Available parameters are " + keySet());
            }
            return super.get(key);
        }
    }
}
