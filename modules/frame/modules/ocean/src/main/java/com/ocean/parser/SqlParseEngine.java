package com.ocean.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.google.common.base.Preconditions;
import com.ocean.visitor.SqlVisitor;
import com.ocean.visitor.mysql.OrParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * 此类很重要
 * Sql解析器
 * 将逻辑sql解析成实际的sql,
 */
public class SqlParseEngine {

    private static Logger logger = LoggerFactory.getLogger(SqlParseEngine.class);

    private SQLStatement sqlStatement;

    private List<Object> parameters;

    private SQLASTOutputVisitor visitor;

    private Collection<String> shardingColumns;

    public SqlParseEngine()
    {

    }

    public SqlParseEngine(SQLStatement sqlStatement,List<Object> parameters,SQLASTOutputVisitor visitor,Collection<String> shardingColumns)
    {
        this.sqlStatement=sqlStatement;
        this.parameters=parameters;
        this.visitor=visitor;
        this.shardingColumns=shardingColumns;
    }

    public SQLStatement getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(SQLStatement sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public SQLASTOutputVisitor getVisitor() {
        return visitor;
    }

    public void setVisitor(SQLASTOutputVisitor visitor) {
        this.visitor = visitor;
    }

    public Collection<String> getShardingColumns() {
        return shardingColumns;
    }

    public void setShardingColumns(Collection<String> shardingColumns) {
        this.shardingColumns = shardingColumns;
    }

    /**
     *  解析SQL
     */
    public SqlParsedResult parse() {

        logger.info("SqlParseEngine parse start");
        Preconditions.checkArgument(visitor instanceof SqlVisitor);
        SqlVisitor sqlVisitor = (SqlVisitor) visitor;
        visitor.setParameters(parameters);
        sqlVisitor.getParseContext().setShardColumns(shardingColumns);
        sqlStatement.accept(visitor);
        SqlParsedResult result;
        if (sqlVisitor.getParseContext().isHasOrCondition()) {
            result = new OrParser(sqlStatement, visitor).parse();
        } else {
            sqlVisitor.getParseContext().mergeCurrentConditionContext();
            result = sqlVisitor.getParseContext().getParsedResult();
        }
        logger.info("SqlParseEngine parsed SQL result: {}", result);
        logger.info("SqlParseEngine parsed SQL: {}", sqlVisitor.getSqlBuilder());
        result.getRouteContext().setSqlBuilder(sqlVisitor.getSqlBuilder());
        return result;
    }
}
