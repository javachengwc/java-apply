package com.ocean.shard.rule;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Binding表规则配置对象
 */
public class BindingTableRule {

    private List<TableRule> tableRules;

    public List<TableRule> getTableRules() {
        return tableRules;
    }

    public void setTableRules(List<TableRule> tableRules) {
        this.tableRules = tableRules;
    }

    /**
     * 判断此绑定表规则是否包含该逻辑表
     */
    public boolean hasLogicTable(final String logicTableName) {
        for (TableRule each : tableRules) {
            if (each.getLogicTable().equals(logicTableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据其他Binding表真实表名称获取相应的真实Binding表名称
     * @param dataSource 数据源名称
     * @param logicTable 逻辑表名称
     * @param otherActualTable 其他真实Binding表名称
     * @return 真实Binding表名称
     */
    public String getBindingActualTable(final String dataSource, final String logicTable, final String otherActualTable) {
        int index = -1;
        for (TableRule each : tableRules) {
            index = each.getActualTableIndex(dataSource, otherActualTable);
            if (-1 != index) {
                break;
            }
        }
        Preconditions.checkState(-1 != index, String.format("Actual table [%s].[%s] is not in table config", dataSource, otherActualTable));
        for (TableRule each : tableRules) {
            if (each.getLogicTable().equals(logicTable)) {
                return each.getActualTables().get(index).getName();
            }
        }
        throw new IllegalStateException(String.format("Cannot find binding actual table, data source: %s, logic table: %s, other actual table: %s", dataSource, logicTable, otherActualTable));
    }

    Collection<String> getAllLogicTables() {
        return Lists.transform(tableRules, new Function<TableRule, String>() {

            @Override
            public String apply(final TableRule input) {
                return input.getLogicTable();
            }
        });
    }
}
