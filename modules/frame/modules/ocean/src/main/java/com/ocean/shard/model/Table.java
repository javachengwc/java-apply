package com.ocean.shard.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 表实体
 */
public class Table implements Serializable {

    //数据库
    private String db;

    //表名
    private String name;

    //逻辑名
    private String logicName;

    public Table()
    {

    }

    public Table(String db,String name)
    {
        this.db=db;
        this.name=name;
    }

    public Table(String db,String name,String logicName)
    {
        this.db=db;
        this.name=name;
        this.logicName=logicName;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode()
    {
        int code=0;
        code+=((db==null)?0:db.hashCode());
        code+=((name==null)?0:name.hashCode());
        code+=((logicName==null)?0:logicName.hashCode());
        return code;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj==null)
        {
            return false;
        }
        if(obj instanceof Table)
        {
            Table other =(Table)obj;
            if(!strEquals(db,other.getDb()) || !strEquals(name,other.getName()) || !strEquals(logicName,other.getLogicName()) )
            {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean strEquals(String a,String b)
    {
        if(a==null && b!=null)
        {
            return false;
        }
        if(a!=null && !a.equals(b))
        {
            return false;
        }
        return true;
    }
}
