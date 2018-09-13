package com.mybatis.pseudocode.mybatis.builder.xml;

import com.mybatis.pseudocode.mybatis.builder.BaseBuilder;
import com.mybatis.pseudocode.mybatis.builder.BuilderException;
import com.mybatis.pseudocode.mybatis.builder.MapperBuilderAssistant;
import com.mybatis.pseudocode.mybatis.builder.ResultMapResolver;
import com.mybatis.pseudocode.mybatis.mapping.*;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Discriminator;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;

import java.io.InputStream;
import java.io.Reader;
import java.util.*;

//解析SqlMap.xml和所有的***Mapper.xml
public class XMLMapperBuilder extends BaseBuilder {

    private final XPathParser parser;

    private MapperBuilderAssistant builderAssistant;

    //存放Mapper.xml中<sql>节点信息，供解析sql语句时使用
    private final Map<String, XNode> sqlFragments;

    private final String resource;

    @Deprecated
    public XMLMapperBuilder(Reader reader, Configuration configuration, String resource, Map<String, XNode> sqlFragments, String namespace) {
        this(reader, configuration, resource, sqlFragments);
        this.builderAssistant.setCurrentNamespace(namespace);
    }

    @Deprecated
    public XMLMapperBuilder(Reader reader, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
        this(new XPathParser(reader, true, configuration.getVariables(), new XMLMapperEntityResolver()), configuration, resource, sqlFragments);
    }

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, Map<String, XNode> sqlFragments, String namespace) {
        this(inputStream, configuration, resource, sqlFragments);
        this.builderAssistant.setCurrentNamespace(namespace);
    }

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
        this(new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver()), configuration, resource, sqlFragments);
    }

    private XMLMapperBuilder(XPathParser parser, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
        this.parser = parser;
        this.sqlFragments = sqlFragments;
        this.resource = resource;
    }

    public void parse() {
        // 若当前Mapper.xml尚未加载，则加载
        if (!this.configuration.isResourceLoaded(this.resource)) {
            //解析<mapper>节点
            configurationElement(this.parser.evalNode("/mapper"));
            //解析<mapper>节点，并将解析结果注册进configuration中，标记为已加载
            this.configuration.addLoadedResource(this.resource);
            //将当前映射文件所对应的DAO接口的Class对象注册进configuration中
            bindMapperForNamespace();
        }

        //parsePendingResultMaps();
        //parsePendingCacheRefs();
        //parsePendingStatements();
    }

    private void bindMapperForNamespace() {
        // 获取当前映射文件对应的DAO接口的全限定名
        //String namespace = builderAssistant.getCurrentNamespace();
        String namespace= null;
        if (namespace != null) {
            // 将全限定名解析成Class对象
            Class<?> boundType = null;
            try {
                boundType = Resources.classForName(namespace);
            } catch (ClassNotFoundException e) {

            }
            if (boundType != null) {
                if (!configuration.hasMapper(boundType)) {
                    //将当前Mapper.xml标注为已加载
                    configuration.addLoadedResource("namespace:" + namespace);
                    //将DAO接口的Class对象注册进configuration中
                    configuration.addMapper(boundType);
                }
            }
        }
    }

    private void configurationElement(XNode context) {
        try {
            // 获取<mapper>节点上的namespace属性，该属性必须存在，表示当前映射文件对应的Mapper Class
            String namespace = context.getStringAttribute("namespace");
            if ((namespace == null) || (namespace.equals(""))) {
                throw new BuilderException("Mapper's namespace cannot be empty");
            }
            this.builderAssistant.setCurrentNamespace(namespace);
            // 解析<cache-ref>节点
            //cacheRefElement(context.evalNode("cache-ref"));
            // 解析<cache>节点
            cacheElement(context.evalNode("cache"));
            // 解析<parameterMap>节点
            parameterMapElement(context.evalNodes("/mapper/parameterMap"));
            // 解析<resultMap>节点
            resultMapElements(context.evalNodes("/mapper/resultMap"));
            // 解析<sql>节点
            //sqlElement(context.evalNodes("/mapper/sql"));
            //将mapper.xml中的的sql语句解析成MappedStatement对象，并存在configuration的mappedStatements
            buildStatementFromContext(context.evalNodes("select|insert|update|delete"));
        } catch (Exception e) {
            throw new BuilderException("Error parsing Mapper XML. The XML location is '" + this.resource + "'. Cause: " + e, e);
        }
    }

    public XNode getSqlFragment(String refid) {
        return (XNode) this.sqlFragments.get(refid);
    }

    //解析所有的<resultMap>节点
    private void resultMapElements(List<XNode> list) throws Exception {
        for (XNode resultMapNode : list) {
            try {
                //解析每一个<resultMap>节点
                resultMapElement(resultMapNode);
            } catch (IncompleteElementException localIncompleteElementException) {
            }
        }
    }

    //解析单个<resultMap>节点
    private ResultMap resultMapElement(XNode resultMapNode) throws Exception {
        return resultMapElement(resultMapNode, Collections.emptyList());
    }

    //解析单个<resultMap>节点
    private ResultMap resultMapElement(XNode resultMapNode, List<ResultMapping> additionalResultMappings) throws Exception {
        //ErrorContext.instance().activity("processing " + resultMapNode.getValueBasedIdentifier());
        String id = resultMapNode.getStringAttribute("id", resultMapNode.getValueBasedIdentifier());
        // 获取<ResultMap>上的type属性（即resultMap的返回值类型）
        String type = resultMapNode.getStringAttribute("type", resultMapNode
                .getStringAttribute("ofType", resultMapNode
                        .getStringAttribute("resultType", resultMapNode
                                .getStringAttribute("javaType"))));

        String extend = resultMapNode.getStringAttribute("extends");
        Boolean autoMapping = resultMapNode.getBooleanAttribute("autoMapping");
        //将resultMap的返回值类型转换成Class对象
        Class typeClass = resolveClass(type);

        //鉴别器discriminator  mybatis可以使用discriminator判断某列的值，然后根据某列的值改变封装行为
        Discriminator discriminator = null;

        // resultMappings用于存储<resultMap>下所有的子节点
        List<ResultMapping>  resultMappings = new ArrayList<ResultMapping>();
        resultMappings.addAll(additionalResultMappings);

        List<XNode> resultChildren = resultMapNode.getChildren();
        //遍历<resultMap>下所有的子节点
        for (XNode resultChild : resultChildren) {
            if ("constructor".equals(resultChild.getName())) {
                // 若当前节点为<constructor>，则将它的子节点们添加到resultMappings中去
                //processConstructorElement(resultChild, typeClass, resultMappings);
            } else if ("discriminator".equals(resultChild.getName())) {
                // 若当前节点为<discriminator>，则进行条件判断，并将命中的子节点添加到resultMappings中去
                //discriminator = processDiscriminatorElement(resultChild, typeClass, resultMappings);
            } else {
                List flags = new ArrayList();
                if ("id".equals(resultChild.getName())) {
                    flags.add(ResultFlag.ID);
                }
                // 若当前节点为<result>、<association>、<collection>，则将其添加到resultMappings中去
                resultMappings.add(buildResultMappingFromContext(resultChild, typeClass, flags));
            }
        }
        // ResultMapResolver的作用是生成ResultMap对象，并将其加入到Configuration对象的resultMaps容器中
        ResultMapResolver resultMapResolver = new ResultMapResolver(this.builderAssistant, id, typeClass, extend,
                discriminator, resultMappings, autoMapping);
        try {
            return resultMapResolver.resolve();
        } catch (IncompleteElementException e) {
            this.configuration.addIncompleteResultMap(resultMapResolver);
            throw e;
        }
    }

    //生成ResultMapping对象
    private ResultMapping buildResultMappingFromContext(XNode context, Class<?> resultType, List<ResultFlag> flags) throws Exception
    {
        String property;
        if (flags.contains(ResultFlag.CONSTRUCTOR))
            property = context.getStringAttribute("name");
        else {
            property = context.getStringAttribute("property");
        }
        String column = context.getStringAttribute("column");
        String javaType = context.getStringAttribute("javaType");
        String jdbcType = context.getStringAttribute("jdbcType");
        String nestedSelect = context.getStringAttribute("select");
//        String nestedResultMap = context.getStringAttribute("resultMap",
//                processNestedResultMappings(context, Collections.emptyList()));
        String nestedResultMap=null;
        String notNullColumn = context.getStringAttribute("notNullColumn");
        String columnPrefix = context.getStringAttribute("columnPrefix");
        String typeHandler = context.getStringAttribute("typeHandler");
        String resultSet = context.getStringAttribute("resultSet");
        String foreignColumn = context.getStringAttribute("foreignColumn");
        boolean lazy = "lazy".equals(context.getStringAttribute("fetchType", this.configuration.isLazyLoadingEnabled() ? "lazy" : "eager"));
        Class javaTypeClass = resolveClass(javaType);

        Class typeHandlerClass = resolveClass(typeHandler);
        JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);
//        return this.builderAssistant.buildResultMapping(resultType, property, column, javaTypeClass, jdbcTypeEnum,
//                nestedSelect, nestedResultMap, notNullColumn, columnPrefix, typeHandlerClass, flags, resultSet, foreignColumn, lazy);
        return null;
    }

    //将mapper.xml中的的sql语句解析成MappedStatement对象，并存入configuration的mappedStatements
    private void buildStatementFromContext(List<XNode> list) {
        if (this.configuration.getDatabaseId() != null) {
            buildStatementFromContext(list, this.configuration.getDatabaseId());
        }else {
            buildStatementFromContext(list, null);
        }
    }

    private void buildStatementFromContext(List<XNode> list, String requiredDatabaseId) {
        for (XNode context : list) {
            XMLStatementBuilder statementParser = new XMLStatementBuilder(this.configuration, this.builderAssistant, context, requiredDatabaseId);
            try {
                //对Statement的解析委托XMLMapperBuilder来完成
                statementParser.parseStatementNode();
            } catch (IncompleteElementException e) {
                this.configuration.addIncompleteStatement(statementParser);
            }
        }
    }

    // 解析<cache>节点
    private void cacheElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type", "PERPETUAL");
            Class typeClass = this.typeAliasRegistry.resolveAlias(type);
            String eviction = context.getStringAttribute("eviction", "LRU");
            Class evictionClass = this.typeAliasRegistry.resolveAlias(eviction);
            Long flushInterval = context.getLongAttribute("flushInterval");
            Integer size = context.getIntAttribute("size");
            boolean readWrite = !context.getBooleanAttribute("readOnly", Boolean.valueOf(false)).booleanValue();
            boolean blocking = context.getBooleanAttribute("blocking", Boolean.valueOf(false)).booleanValue();
            Properties props = context.getChildrenAsProperties();
            //生成Cache对象，并加入到configuration的caches集合中
            this.builderAssistant.useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props);
        }
    }

    //解析<parameterMap>节点
    private void parameterMapElement(List<XNode> list) throws Exception {
        for (XNode parameterMapNode : list) {
            String id = parameterMapNode.getStringAttribute("id");
            String type = parameterMapNode.getStringAttribute("type");
            Class parameterClass = resolveClass(type);
            List<XNode> parameterNodes = parameterMapNode.evalNodes("parameter");
            List parameterMappings = new ArrayList();
            for (XNode parameterNode : parameterNodes) {
                String property = parameterNode.getStringAttribute("property");
                String javaType = parameterNode.getStringAttribute("javaType");
                String jdbcType = parameterNode.getStringAttribute("jdbcType");
                String resultMap = parameterNode.getStringAttribute("resultMap");
                String mode = parameterNode.getStringAttribute("mode");
                String typeHandler = parameterNode.getStringAttribute("typeHandler");
                Integer numericScale = parameterNode.getIntAttribute("numericScale");
                ParameterMode modeEnum = resolveParameterMode(mode);
                Class javaTypeClass = resolveClass(javaType);
                JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);

                Class typeHandlerClass = resolveClass(typeHandler);
                ParameterMapping parameterMapping = this.builderAssistant.buildParameterMapping(parameterClass, property, javaTypeClass,
                        jdbcTypeEnum, resultMap, modeEnum, typeHandlerClass, numericScale);
                parameterMappings.add(parameterMapping);
            }
            //生成ParameterMap,并加入到configuration的parameterMaps集合中
            this.builderAssistant.addParameterMap(id, parameterClass, parameterMappings);
        }
    }

}

