package com.ocean.router;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.ocean.jdbc.DatabaseType;
import com.ocean.shard.rule.ShardRule;
import com.ocean.exception.ShardException;
import com.ocean.exception.SqlParserException;
import com.ocean.parser.*;
import com.ocean.router.binding.BindingTablesRouter;
import com.ocean.router.mixed.MixedTablesRouter;
import com.ocean.router.single.SingleTableRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * sql路由引擎
 */
public class SqlRouteEngine {

    private static Logger logger = LoggerFactory.getLogger(SqlRouteEngine.class);

    private ShardRule shardRule;

    private DatabaseType databaseType;

    public SqlRouteEngine()
    {

    }

    public SqlRouteEngine(ShardRule shardRule,DatabaseType databaseType)
    {
        this.shardRule=shardRule;
        this.databaseType=databaseType;
    }

    public ShardRule getShardRule() {
        return shardRule;
    }

    public void setShardRule(ShardRule shardRule) {
        this.shardRule = shardRule;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * SQL路由
     */
    public SqlRouteResult route(final String logicSql, final List<Object> parameters) throws SqlParserException {

        logger.info("SqlRouteEngine route start ");
        return routeSQL(parseSQL(logicSql, parameters));
    }

    private SqlParsedResult parseSQL(final String logicSql, final List<Object> parameters) {
        logger.info("SqlRouteEngine parseSQL,logicSql= "+logicSql);
        Collection<String> shardColumns =shardRule.getAllShardingColumns();
        int shardColCnt = (shardColumns==null)?0:shardColumns.size();
        String shardColumnStr = "";
        if(shardColCnt>0)
        {
            shardColumnStr= Arrays.toString(shardColumns.toArray());
        }
        logger.info("SqlRouteEngine shardColumns count="+shardColCnt+",shardColumnStr="+shardColumnStr);
        SqlParsedResult result = SqlParserFactory.create(databaseType, logicSql, parameters,shardColumns ).parse();
        return result;
    }

    private SqlRouteResult routeSQL(SqlParsedResult parsedResult) {
        logger.info("SqlRouteEngine routeSQL parsedResult start ");
        SqlRouteResult result = new SqlRouteResult(parsedResult.getMergeContext());
        for (ConditionContext each : parsedResult.getConditionContexts()) {

            Collection<SqlExecutionUnit> units =routeSQL(each, Collections2.transform(parsedResult.getRouteContext().getTables(),
                    new Function<SqlTable, String>() {
                        public String apply(SqlTable input) {
                            return input.getName();
                        }
                    }), parsedResult.getRouteContext().getSqlBuilder());
            result.getExecutionUnits().addAll(units);
        }
        int exeUnitsCnt = (result.getExecutionUnits()==null)?0:result.getExecutionUnits().size();
        logger.info("SqlRouteEngine routeSQL parsedResult sqlRouteResult executionUnits count= "+exeUnitsCnt);
        return result;
    }

    private Collection<SqlExecutionUnit> routeSQL(ConditionContext conditionContext, Collection<String> logicTables, SqlBuilder sqlBuilder) {
        logger.info("SqlRouteEngine routeSQL conditionContext  start ,conditionContext="+conditionContext);
        RoutingResult result;
        if (1 == logicTables.size()) {
            result = new SingleTableRouter(shardRule, logicTables.iterator().next(), conditionContext).route();
        } else if (shardRule.isAllBindingTable(logicTables)) {
            result = new BindingTablesRouter(shardRule, logicTables, conditionContext).route();
        } else {
            logger.info("SqlRouteEngine routeSQL conditionContext  create MixedTablesRouter ");
            result = new MixedTablesRouter(shardRule, logicTables, conditionContext).route();
        }
        if (null == result) {
            throw new ShardException("SqlRouteEngine routeSQL cannot route any result, please check your shard rule.");
        }
        return result.getSqlExecutionUnits(sqlBuilder);
    }
}
