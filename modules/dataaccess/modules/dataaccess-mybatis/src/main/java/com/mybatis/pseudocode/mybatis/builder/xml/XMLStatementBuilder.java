package com.mybatis.pseudocode.mybatis.builder.xml;

import com.mybatis.pseudocode.mybatis.builder.BaseBuilder;
import com.mybatis.pseudocode.mybatis.executor.kengen.KeyGenerator;
import com.mybatis.pseudocode.mybatis.mapping.ResultSetType;
import com.mybatis.pseudocode.mybatis.mapping.SqlCommandType;
import com.mybatis.pseudocode.mybatis.mapping.SqlSource;
import com.mybatis.pseudocode.mybatis.mapping.StatementType;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.parsing.XNode;

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
        //LanguageDriver langDriver = getLanguageDriver(lang);

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
//        processSelectKeyNodes(id, parameterTypeClass, langDriver);

//        SqlSource sqlSource = langDriver.createSqlSource(this.configuration, this.context, parameterTypeClass);
        SqlSource sqlSource =null;
        String resultSets = this.context.getStringAttribute("resultSets");
        String keyProperty = this.context.getStringAttribute("keyProperty");
        String keyColumn = this.context.getStringAttribute("keyColumn");

        String keyStatementId = id + "!selectKey";
        keyStatementId = this.builderAssistant.applyCurrentNamespace(keyStatementId, true);
        KeyGenerator keyGenerator;
        if (this.configuration.hasKeyGenerator(keyStatementId))
            keyGenerator = this.configuration.getKeyGenerator(keyStatementId);
        else {
//            keyGenerator = this.context.getBooleanAttribute("useGeneratedKeys",
//                    Boolean.valueOf((this.configuration.isUseGeneratedKeys())
//                            && (SqlCommandType.INSERT.equals(sqlCommandType)))).booleanValue() ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
        }
//        this.builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
//                parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache,
//                resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);
    }

}
