package com.ocean.parser;

import com.google.common.base.Optional;
import com.util.StringUtil;
import org.apache.commons.lang.builder.ToStringBuilder;

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

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode()
    {
        int code=0;
        code+=((name==null)?0:name.hashCode());
        code+=(alias.isPresent()?alias.get().toString().hashCode():0);
        return code;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj==null)
        {
            return false;
        }
        if(obj instanceof SqlTable)
        {
            SqlTable other =(SqlTable)obj;

            String aliasStr =alias.isPresent()?alias.get().toString():null;
            String otherAliasStr =other.getAlias().isPresent()?other.getAlias().get().toString():null;

            if(!StringUtil.strEquals(name, other.getName()) || !StringUtil.strEquals(aliasStr,otherAliasStr) )
            {
                return false;
            }
            return true;
        }
        return false;
    }
}
