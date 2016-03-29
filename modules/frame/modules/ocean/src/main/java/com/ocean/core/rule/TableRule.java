package com.ocean.core.rule;

import com.ocean.core.model.Table;
import com.ocean.core.strategy.ShardStrategy;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 表规则
 */
public class TableRule {

    //逻辑表
    private String logicTable;

    //实际表
    private  List<Table> actualTables;

    //分库策略
    private ShardStrategy databaseShardStrategy;

    //分表策略
    private ShardStrategy tableShardStrategy;

    public String getLogicTable() {
        return logicTable;
    }

    public void setLogicTable(String logicTable) {
        this.logicTable = logicTable;
    }

    public List<Table> getActualTables() {
        return actualTables;
    }

    public void setActualTables(List<Table> actualTables) {
        this.actualTables = actualTables;
    }

    public ShardStrategy getDatabaseShardStrategy() {
        return databaseShardStrategy;
    }

    public void setDatabaseShardStrategy(ShardStrategy databaseShardStrategy) {
        this.databaseShardStrategy = databaseShardStrategy;
    }

    public ShardStrategy getTableShardStrategy() {
        return tableShardStrategy;
    }

    public void setTableShardStrategy(ShardStrategy tableShardStrategy) {
        this.tableShardStrategy = tableShardStrategy;
    }


    public TableRule(String logicTable, List<Table> actualTables,ShardStrategy databaseShardStrategy, ShardStrategy tableShardStrategy) {

        this.logicTable=logicTable;
        this.actualTables=actualTables;
        this.databaseShardStrategy=databaseShardStrategy;
        this.tableShardStrategy=tableShardStrategy;
    }

    //根据数据库，表名获取真实数据单元
    public List<Table> getActualTables(Collection<String> dbs, Collection<String> tables) {
        List<Table> result = new LinkedList<Table>();
        for (Table table: actualTables) {
            if (dbs.contains(table.getDb()) && tables.contains(table.getName()))
            {
                result.add(table);
            }
        }
        return result;
    }

    //根据数据库获取真实表名称
    public List<String> getActualTableNames(Collection<String> dbs) {
        List<String> result = new LinkedList<String>();
        for (Table table: actualTables) {
            if (dbs.contains(table.getDb()))
            {
                result.add(table.getName());
            }
        }
        return result;
    }

    //根据数据库，真实表名称查找真实表顺序
    public int getActualTableIndex(String db, String actualTable) {
        int result = 0;
        for (Table table : actualTables) {
            if (table.getDb().equals(db) && table.getName().equals(actualTable))
            {
                return result;
            }
            result++;
        }
        return -1;
    }
}
