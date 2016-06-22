package com.ocean.parser;

import com.google.common.base.Optional;

/**
 * 表解析对象
 */
public class SqlTable {

    private String name;

    private Optional<String> alias;

    public SqlTable()
    {

    }
    public SqlTable(String name,Optional<String> alias)
    {
        this.name=name;
        this.alias=alias;
    }

    public SqlTable(String name,String alias)
    {
        this.name=name;
        this.alias=Optional.fromNullable(alias);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getAlias() {
        return alias;
    }

    public void setAlias(Optional<String> alias) {
        this.alias = alias;
    }
}
