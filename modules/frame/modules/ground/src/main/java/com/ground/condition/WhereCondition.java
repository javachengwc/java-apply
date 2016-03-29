package com.ground.condition;

import java.util.LinkedList;
import java.util.List;

/**
 * where 条件语句
 */
public class WhereCondition
{
	/**
	 * 对比条件
	 * 
	 * @author zhxing
	 */
	public static enum CompareType
	{
		GT(">"), LT("<"), EQ("="),NE("<>"),GTE(">="),LTE("<=");
		private String name;
		
		CompareType(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return this.name;
		}
	};
	
	/**
	 * 查询条件类型 and or
	 * 
	 * @author zhxing
	 */
	public enum SelectType
	{
		AND, OR
	};
	
	public static class Condition
	{
		public String columnName;
		
		public CompareType compareType = CompareType.EQ;
		
		public String value;
		
		public SelectType stype = SelectType.AND;
		
		public Condition(String columnName, String value)
		{
			this.columnName = columnName;
			this.value = value;
		}
		public String toString(){
			return "columnName="+columnName+",value="+value;
		}
	}
	
	private LinkedList<Condition> conditions = new LinkedList<Condition>();
	
	/**
	 * 添加where 条件
	 * 
	 * @param column 字段名
	 * @param value 字段值
	 * @param compareType 对比条件
	 * @param stype and ,or
	 */
	public void addCondition(String column, String value,
		CompareType compareType,SelectType stype)
	{
		Condition c = new Condition(column, value);
		c.compareType=compareType;
		c.stype=stype;
		conditions.add(c);
		
	}
	
	/**
	 * 添加where 条件
	 * 
	 * @param column 字段名
	 * @param value 字段值
	 * @param compareType 对比条件
	 */
	public void addCondition(String column, String value,
		CompareType compareType)
	{
		Condition c = new Condition(column, value);
		c.compareType=compareType;
		conditions.add(c);
	}
	
	
	/**
	 * 添加where 条件，默认对比为相等
	 * 
	 * @param column 字段名
	 * @param value 字段值
	 */
	public void addCondition(String column, String value)
	{
		Condition c = new Condition(column, value);
		conditions.add(c);
	}
	
	public void addConditionFirst(String column, String value)
	{
		Condition c = new Condition(column, value);
		conditions.addFirst(c);
	}
	
	
	/**
	 * 获取字段键值对
	 * 
	 * @return
	 */
	public List<Condition> getConditons()
	{
		return this.conditions;
	}
	
	public String toString(){
		return conditions.toString();
	}
}
