package com.ocean.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.ocean.jdbc.DatabaseType;
import com.ocean.exception.SqlParserException;
import com.ocean.visitor.VisitorLogProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * sql解析器工厂
 */
public class SqlParserFactory {

    private static Logger logger = LoggerFactory.getLogger(SqlParserFactory.class);

    /**
     * 创建解析器引擎对象
     * @param databaseType 数据库类型
     * @param sql SQL语句
     * @param parameters SQL中参数的值
     * @param shardingColumns 分片列名称集合
     * @return 解析器引擎对象
     * @throws SqlParserException SQL解析异常
     */
    public static SqlParseEngine create(final DatabaseType databaseType, final String sql, final List<Object> parameters, final Collection<String> shardingColumns) throws SqlParserException {
        logger.info("SqlParserFactory create,sql="+sql);
        SQLStatement sqlStatement = getSQLStatementParser(databaseType, sql).parseStatement();
        logger.info("SqlParserFactory create getSQLStatementParser sqlStatment="+sqlStatement.getClass().getName());
        return new SqlParseEngine(sqlStatement, parameters, getSQLVisitor(databaseType, sqlStatement), shardingColumns);
    }

    private static SQLStatementParser getSQLStatementParser(final DatabaseType databaseType, final String sql) {
        switch (databaseType) {
            case H2:
            case MySQL:
                return new MySqlStatementParser(sql);
            case Oracle:
                return new OracleStatementParser(sql);
            case SQLServer:
                return new SQLServerStatementParser(sql);
            case DB2:
                return new DB2StatementParser(sql);
            default:
                throw new UnsupportedOperationException(String.format("Cannot support database type [%s]", databaseType));
        }
    }

    private static SQLASTOutputVisitor getSQLVisitor(final DatabaseType databaseType, final SQLStatement sqlStatement) {
        if (sqlStatement instanceof SQLSelectStatement) {
            return VisitorLogProxy.enhance(SqlVisitorRegistry.getSelectVistor(databaseType));
        }
        if (sqlStatement instanceof SQLInsertStatement) {
            return VisitorLogProxy.enhance(SqlVisitorRegistry.getInsertVistor(databaseType));
        }
        if (sqlStatement instanceof SQLUpdateStatement) {
            return VisitorLogProxy.enhance(SqlVisitorRegistry.getUpdateVistor(databaseType));
        }
        if (sqlStatement instanceof SQLDeleteStatement) {
            return VisitorLogProxy.enhance(SqlVisitorRegistry.getDeleteVistor(databaseType));
        }
        throw new SqlParserException("Unsupported SQL statement: [%s]", sqlStatement);
    }
}
