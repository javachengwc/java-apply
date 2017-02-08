package com.ocean.parser;

import com.util.base.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * sql条件类
 */
public class SqlCondition {

    private Column column;

    private BinaryOperator operator;

    private List<Comparable<?>> values = new ArrayList<Comparable<?>>();

    public SqlCondition()
    {

    }

    public SqlCondition(Column column, BinaryOperator operator)
    {
        this.column=column;
        this.operator=operator;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public void setOperator(BinaryOperator operator) {
        this.operator = operator;
    }

    public List<Comparable<?>> getValues() {
        return values;
    }

    public void setValues(List<Comparable<?>> values) {
        this.values = values;
    }

    /**
     * 列对象
     */
    public static class Column {

        private String columnName;

        private String tableName;

        public Column()
        {

        }

        public Column(String columnName,String tableName)
        {
            this.columnName=columnName;
            this.tableName=tableName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }

    /**
     * 操作符枚举
     */
    public enum BinaryOperator {

        EQUAL("="), BETWEEN("BETWEEN"), IN("IN"), NOT_IN("NOT IN");

        private String expression;

        private BinaryOperator(String expression)
        {
            this.expression=expression;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public String toString() {
            return expression;
        }
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("column:");
        if(column!=null)
        {
            buf.append(column.getColumnName() +" "+column.getTableName());
        }
        buf.append(",operator:");
        if(operator!=null)
        {
            buf.append(operator.getExpression());
        }
        buf.append(",values:");
        if(values!=null && values.size()>0)
        {
            for(Comparable<?> s:values)
            {
                buf.append(s).append(" ");
            }
        }
        return buf.toString();
    }

    @Override
    public int hashCode()
    {
        int code=0;
        if(column!=null) {
            code += ((column.getTableName() == null) ? 0 : column.getTableName().hashCode());
            code += ((column.getColumnName()==null) ? 0: column.getColumnName().hashCode());
        }
        if(operator!=null)
        {
            code+=operator.ordinal();
        }
        int count = (values==null)?0:values.size();
        if(count==0)
        {
            return code;
        }

        int tmp=0;
        for(Comparable<?> per:values)
        {
            tmp+=per.hashCode();
        }
        tmp = tmp/count+count;
        code+=tmp;
        return code;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj==null)
        {
            return false;
        }
        if(obj instanceof SqlCondition)
        {
            SqlCondition other =(SqlCondition)obj;

            if((column==null && other.getColumn()!=null) || (column!=null && other.getColumn()==null) )
            {
                return false;
            }
            if(column!=null && other.getColumn()!=null)
            {
                if(!StringUtil.strEquals(column.getTableName(),other.getColumn().getTableName()) || !StringUtil.strEquals(column.getColumnName(),other.getColumn().getColumnName()) )
                {
                    return false;
                }
            }

            if((operator==null && other.getOperator()!=null) || (operator!=null && other.getOperator()==null) )
            {
                return false;
            }
            if(operator!=null && other.getOperator()!=null && operator.ordinal()!=other.getOperator().ordinal())
            {
                return false;
            }

            int count = (values==null)?0:values.size();
            int otherValuesCnt = (other.getValues()==null)?0:other.getValues().size();

            if(count!=otherValuesCnt)
            {
                return false;
            }
            if(count>0)
            {
                for(Comparable<?> per:values)
                {
                    if(!other.getValues().contains(per))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        return false;
    }
}