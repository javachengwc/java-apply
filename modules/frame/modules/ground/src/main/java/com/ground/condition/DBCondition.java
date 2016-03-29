package com.ground.condition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBCondition
{
	
	/**
	 * 排序升序or降序
	 */
	public enum OrderType
	{
		DESC, ASC
	};
	
	/**
	 * where 条件
	 */
	public WhereCondition whereCondition = new WhereCondition();
	
	/**
	 * 设置赋值 
	 */
	public UpdateSet updateSet =new UpdateSet();
	/**
	 * 数据库开始指针
	 */
	public int offset;
	
	/**
	 * 获取条数
	 */
	public int num;
	
	/**
	 * 排序字段
	 */
	public Map<String, OrderType> orders = new LinkedHashMap<String, OrderType>(
		3);
	
	/**
	 * 表名
	 */
	public String table;
	
	/**
	 * 多条数据集关键字段，一般为主键
	 */
	public String keyColumnName;
	
	/**
	 * group by 字段列表
	 */
	public List<String> groups = new ArrayList<String>(2);
	
	public DBCondition()
	{
		// this.table=table;
	}
	
	public DBCondition(String table)
	{
		this.table = table;
	}
	
	public DBCondition(String table, String keyColumnName)
	{
		this.table = table;
		this.keyColumnName = keyColumnName;
	}
	
	public void addOrder(String orderColumn, OrderType orderType)
	{
		orders.put(orderColumn, orderType);
	}
	
	public void addGroup(String column)
	{
		groups.add(column);
		
	}
	
	public void addLimit(int offset, int num)
	{
		this.offset = offset;
		this.num = num;
	}
	
	public void addCondition(String column, String value, WhereCondition.CompareType whereType)
	{
		whereCondition.addCondition(column, value, whereType);
	}
	
	public void addCondition(String column, String value, WhereCondition.CompareType whereType,WhereCondition.SelectType stype)
	{
		whereCondition.addCondition(column, value, whereType,stype);
	}
	
	public void addCondition(String column, String value)
	{
		whereCondition.addCondition(column, value);
	}
	public void addCondition(String column, Number value)
	{
		whereCondition.addCondition(column, String.valueOf(value));
	}
	public void addConditionFirst(String column, String value)
	{
		whereCondition.addConditionFirst(column, value);
	}
	public void addSetValue(String column,String value)
	{
		updateSet.addSetValue(column, value);
	}
	
	public String toString(){
		String ret="table="+table+",groups="+groups+",orders="+orders+
				   ",whereCondition="+whereCondition+",updateSet="+updateSet;
		return ret;
		
	}
	
}
