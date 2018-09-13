package com.mybatis.pseudocode.mybatis.builder.xml;

import com.mybatis.pseudocode.mybatis.builder.BaseBuilder;
import com.mybatis.pseudocode.mybatis.builder.MapperBuilderAssistant;
import com.mybatis.pseudocode.mybatis.executor.kengen.KeyGenerator;
import com.mybatis.pseudocode.mybatis.executor.kengen.SelectKeyGenerator;
import com.mybatis.pseudocode.mybatis.mapping.*;
import com.mybatis.pseudocode.mybatis.scripting.LanguageDriver;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.parsing.XNode;

import java.util.List;
import java.util.Locale;

//XMLMapperBuilder中对Statement的解析(即SqlMap.xml中SELECT|INSERT|UPDATE|DELETE定义部分)委托给XMLStatementBuilder来完成
public class XMLStatementBuilder extends BaseBuilder {
    private final MapperBuilderAssistant builderAssistant;
    private final XNode context;
    private final String requiredDatabaseId;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context) {
        this(configuration, builderAssistant, context, null);
    }

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context, String databaseId) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
        this.requiredDatabaseId = databaseId;
    }

    public void parseStatementNode() {
        String id = this.context.getStringAttribute("id");
        String databaseId = this.context.getStringAttribute("databaseId");

//        if (!databaseIdMatchesCurrent(id, databaseId, this.requiredDatabaseId)) {
//            return;
//        }

        Integer fetchSize = this.context.getIntAttribute("fetchSize");
        Integer timeout = this.context.getIntAttribute("timeout");
        String parameterMap = this.context.getStringAttribute("parameterMap");
        String parameterType = this.context.getStringAttribute("parameterType");
        Class parameterTypeClass = resolveClass(parameterType);
        String resultMap = this.context.getStringAttribute("resultMap");
        String resultType = this.context.getStringAttribute("resultType");
        String lang = this.context.getStringAttribute("lang");
        LanguageDriver langDriver = getLanguageDriver(lang);

        Class resultTypeClass = resolveClass(resultType);
        String resultSetType = this.context.getStringAttribute("resultSetType");
        StatementType statementType = StatementType.valueOf(this.context.getStringAttribute("statementType", StatementType.PREPARED.toString()));
        ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);

        String nodeName = this.context.getNode().getNodeName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        boolean flushCache = this.context.getBooleanAttribute("flushCache", Boolean.valueOf(!isSelect)).booleanValue();
        boolean useCache = this.context.getBooleanAttribute("useCache", Boolean.valueOf(isSelect)).booleanValue();
        boolean resultOrdered = this.context.getBooleanAttribute("resultOrdered", Boolean.valueOf(false)).booleanValue();

//        XMLIncludeTransformer includeParser = new XMLIncludeTransformer(this.configuration, this.builderAssistant);
//        includeParser.applyIncludes(this.context.getNode());
//
        //解析Mapper.xml中statment节点中的<selectKey>节点
        //比如<insert id="addUser"><selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer">SELECT LAST_INSERT_ID()</selectKey>...
        processSelectKeyNodes(id, parameterTypeClass, langDriver);

//        SqlSource sqlSource = langDriver.createSqlSource(this.configuration, this.context, parameterTypeClass);
        SqlSource sqlSource =null;
        String resultSets = this.context.getStringAttribute("resultSets");
        String keyProperty = this.context.getStringAttribute("keyProperty");
        String keyColumn = this.context.getStringAttribute("keyColumn");

        String keyStatementId = id + "!selectKey";
        keyStatementId = this.builderAssistant.applyCurrentNamespace(keyStatementId, true);
        KeyGenerator keyGenerator=null;
        if (this.configuration.hasKeyGenerator(keyStatementId))
            keyGenerator = this.configuration.getKeyGenerator(keyStatementId);
        else {
            //dbc3KeyGenerator和NoKeyGenerator对应于insert语句是否取回表的自增Id，
            //KeyGenerator idbc3KeyGenerator= Jdbc3KeyGenerator.INSTANCE;
            //KeyGenerator noKeyGenerator=NoKeyGenerator.INSTANCE;
            KeyGenerator idbc3KeyGenerator=null;
            KeyGenerator noKeyGenerator=null;
            //当Mapper中insert操作的useGeneratedKeys属性为true或则没配置但全局配置了useGeneratedKeys=true则生成Jdbc3KeyGenerator对象，
            //否则生成NoKeyGenerator对象,NoKeyGenerator的方法都为空，不产生任何效果
            //比如:<insert id="addUser" useGeneratedKeys="true" keyProperty="id" parameterType="com.User">
            //就会生成Jdbc3KeyGenerator对象,取回表的自增Id赋值给对象的id属性
            keyGenerator = this.context.getBooleanAttribute("useGeneratedKeys",
                    Boolean.valueOf((this.configuration.isUseGeneratedKeys())
                            && (SqlCommandType.INSERT.equals(sqlCommandType)))).booleanValue() ?idbc3KeyGenerator:noKeyGenerator;
        }
        //解析成MappedStatement对象，并存入configuration的mappedStatements
        this.builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
                parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache,
                resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);
    }

    private LanguageDriver getLanguageDriver(String lang) {
        Class langClass = null;
        if (lang != null) {
            langClass = resolveClass(lang);
        }
        return this.builderAssistant.getLanguageDriver(langClass);
    }

    //解析Mapper.xml中statment节点中的<selectKey>节点
    private void processSelectKeyNodes(String id, Class<?> parameterTypeClass, LanguageDriver langDriver)
    {
        List selectKeyNodes = this.context.evalNodes("selectKey");
        parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, this.configuration.getDatabaseId());
        removeSelectKeyNodes(selectKeyNodes);
    }

    private void parseSelectKeyNodes(String parentId, List<XNode> list, Class<?> parameterTypeClass,
                                     LanguageDriver langDriver, String skRequiredDatabaseId) {
        for (XNode nodeToHandle : list) {
            String id = parentId + "!selectKey";
            String databaseId = nodeToHandle.getStringAttribute("databaseId");
            //if (databaseIdMatchesCurrent(id, databaseId, skRequiredDatabaseId))
                parseSelectKeyNode(id, nodeToHandle, parameterTypeClass, langDriver, databaseId);
        }
    }

    private void parseSelectKeyNode(String id, XNode nodeToHandle, Class<?> parameterTypeClass, LanguageDriver langDriver, String databaseId)
    {
        String resultType = nodeToHandle.getStringAttribute("resultType");
        Class resultTypeClass = resolveClass(resultType);
        StatementType statementType = StatementType.valueOf(nodeToHandle.getStringAttribute("statementType", StatementType.PREPARED.toString()));
        String keyProperty = nodeToHandle.getStringAttribute("keyProperty");
        String keyColumn = nodeToHandle.getStringAttribute("keyColumn");
        boolean executeBefore = "BEFORE".equals(nodeToHandle.getStringAttribute("order", "AFTER"));

        boolean useCache = false;
        boolean resultOrdered = false;
        //KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        KeyGenerator keyGenerator =null;
        Integer fetchSize = null;
        Integer timeout = null;
        boolean flushCache = false;
        String parameterMap = null;
        String resultMap = null;
        ResultSetType resultSetTypeEnum = null;

        SqlSource sqlSource = langDriver.createSqlSource(this.configuration, nodeToHandle, parameterTypeClass);
        SqlCommandType sqlCommandType = SqlCommandType.SELECT;

        this.builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout, parameterMap,
                parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache, resultOrdered,
                keyGenerator, keyProperty, keyColumn, databaseId, langDriver, null);
        id = this.builderAssistant.applyCurrentNamespace(id, false);

        MappedStatement keyStatement = this.configuration.getMappedStatement(id, false);
        this.configuration.addKeyGenerator(id, new SelectKeyGenerator(keyStatement, executeBefore));
    }

    private void removeSelectKeyNodes(List<XNode> selectKeyNodes) {
        for (XNode nodeToHandle : selectKeyNodes)
            nodeToHandle.getParent().getNode().removeChild(nodeToHandle.getNode());
    }

}
