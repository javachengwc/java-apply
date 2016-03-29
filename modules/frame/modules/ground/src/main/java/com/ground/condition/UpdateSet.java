package com.ground.condition;

import java.util.LinkedList;
import java.util.List;

public class UpdateSet {
	
	private LinkedList<SetValue> sets = new LinkedList<SetValue>();
	
	public static class SetValue
	{
		public String columnName;
		
		public String value;
		
		public SetValue(String columnName, String value)
		{
			this.columnName = columnName;
			this.value = value;
		}
		public String toString(){
			return "columnName="+columnName+",value="+value;
		}
	}
	
	/**
	 * 添加赋值
	 */
	public void addSetValue(String column, String value)
	{
		SetValue c = new SetValue(column, value);
		sets.add(c);
	}
	
	public List<SetValue> getSetValues()
	{
		return sets;
	}
}
