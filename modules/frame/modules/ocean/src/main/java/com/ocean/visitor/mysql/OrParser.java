package com.ocean.visitor.mysql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.google.common.base.Optional;
import com.ocean.parser.SqlParsedResult;
import com.ocean.visitor.AbstractOrASTNode;

/**
 * OR表达式解析类
 */
public class OrParser {

    private SQLStatement sqlStatement;

    private  OrVisitor orVisitor;

    public SQLStatement getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(SQLStatement sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public OrVisitor getOrVisitor() {
        return orVisitor;
    }

    public void setOrVisitor(OrVisitor orVisitor) {
        this.orVisitor = orVisitor;
    }

    public OrParser(final SQLStatement sqlStatement, final SQLASTOutputVisitor dependencyVisitor) {
        this.sqlStatement = sqlStatement;
        orVisitor = new OrVisitor(dependencyVisitor);
    }

    /**
     *  解析SQL
     */
    public SqlParsedResult parse() {
        SqlParsedResult result = orVisitor.getParseContext().getParsedResult();
        Optional<AbstractOrASTNode> rootASTNode = orVisitor.visitHandle(sqlStatement);
        if (rootASTNode.isPresent()) {
            result.getConditionContexts().addAll(rootASTNode.get().getCondition());
        }
        return result;
    }
}

