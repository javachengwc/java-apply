package com.ocean.router;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.ocean.core.DatabaseType;
import com.ocean.core.rule.ShardRule;
import com.ocean.exception.ShardException;
import com.ocean.exception.SqlParserException;
import com.ocean.merger.MergeContext;
import com.ocean.parser.SqlParsedResult;
import com.ocean.parser.SqlParserFactory;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * sql路由引擎
 */
public class SqlRouteEngine {

    private ShardRule shardRule;

    private DatabaseType databaseType;

    /**
     * SQL路由
     */
    public SqlRouteResult route(final String logicSql, final List<Object> parameters) throws SqlParserException {
        return routeSQL(parseSQL(logicSql, parameters));
    }

    private SqlParsedResult parseSQL(final String logicSql, final List<Object> parameters) {
        SqlParsedResult result = SqlParserFactory.create(databaseType, logicSql, parameters, shardRule.getAllShardingColumns()).parse();
        return result;
    }

    private SqlRouteResult routeSQL(SqlParsedResult parsedResult) {
        SqlRouteResult result = new SqlRouteResult(parsedResult.getMergeContext());
        for (ConditionContext each : parsedResult.getConditionContexts()) {
            result.getExecutionUnits().addAll(routeSQL(each, Collections2.transform(parsedResult.getRouteContext().getTables(), new Function<Table, String>() {

                @Override
                public String apply(final Table input) {
                    return input.getName();
                }
            }), parsedResult.getRouteContext().getSqlBuilder()));
        }
        return result;
    }

    private Collection<SqlExecutionUnit> routeSQL(final ConditionContext conditionContext, final Collection<String> logicTables, final SQLBuilder sqlBuilder) {
        RoutingResult result;
        if (1 == logicTables.size()) {
            result = new SingleTableRouter(shardRule, logicTables.iterator().next(), conditionContext).route();
        } else if (shardRule.isAllBindingTable(logicTables)) {
            result = new BindingTablesRouter(shardRule, logicTables, conditionContext).route();
        } else {
            // TODO 可配置是否执行笛卡尔积
            result = new MixedTablesRouter(shardRule, logicTables, conditionContext).route();
        }
        if (null == result) {
            throw new ShardException("cannot route any result, please check your shard rule.");
        }
        return result.getSqlExecutionUnits(sqlBuilder);
    }
}
