package com.ground.entity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ground.condition.DBCondition;
import com.ground.condition.UpdateSet;
import com.ground.condition.WhereCondition;
import com.util.BlankUtil;
import org.apache.log4j.Logger;


public class EntitySqlMarker
{
	private static Logger m_logger = Logger.getLogger(EntitySqlMarker.class);
	
	public static String findById(String table,Map<String, Boolean> pkColumn) throws Exception
	{
		String sql = "select * from "+table;
		if(pkColumn ==null || pkColumn.size() == 0){
			throw new SQLException("EntitySqlMarker findById pkColumn is null");
		}
		int i = 0;
		for(String pk : pkColumn.keySet())
		{
			if(i==0){
				sql += " where "+pk+" = ?";
			}else{
				sql +=" and "+pk+ " = ?";
			}
			i++;
		}
		m_logger.info("findById sql:"+sql);
		return sql;
	}
	
	public static String makeSelectSql(String table,List<String> columns)
	{
		StringBuilder sql=new StringBuilder();
		
		sql.append("select * from "+table);
		int i = 0;
		for(String column:columns)
		{
			if(i==0){
				sql.append(" where "+column+" = ?");
			}else{
				sql.append(" and "+column+ " = ?");
			}
			i++;
		}
		
		m_logger.info("makeSelectSql:"+sql.toString());
		return sql.toString();
	}
	
	public static String makeSelectAllSql(String table)
	{
        StringBuilder sql=new StringBuilder();
		sql.append("select * from "+table);
		m_logger.info("makeSelectAllSql:"+sql.toString());
		return sql.toString();
	}
	
	public static String makeUpdateSql(String table,List<String> columns,List<String> wheres) throws Exception
	{
		if(columns==null || columns.size() == 0){
			throw new SQLException("EntitySqlMarker makeUpdateSql columns is null or size is 0");
		}
		StringBuilder sql=new StringBuilder();
		sql.append("update "+table);
		int i = 0;
		int j = 0;
		for(String column:columns)
		{
			if(i==0){
				sql.append(" set "+column+" = ?");
			}else{
				sql.append(","+column+ " = ?");
			}
			i++;
		}
		sql.append(" where ");
		for(String w:wheres)
		{
			if(j==0){
				sql.append( w+" = ? ");
			}else{
				sql.append(" and "+ w + " = ?");
			}
			j++;
		}
		m_logger.info("makeUpdateSql:"+sql.toString());
		return sql.toString();
	}
	
	public static String makeDeleteSql(String table,List<String> columns) throws Exception
	{
		if(columns==null || columns.size() == 0){
			throw new SQLException("EntitySqlMarker makeDeleteSql columns is null or size is 0");
		}
		StringBuilder sql=new StringBuilder();
		sql.append("delete from "+table);
		int i = 0;
		for(String column:columns)
		{
			if(i==0){
				sql.append(" where "+column+" = ?");
			}else{
				sql.append(" and "+column+ " = ?");
			}
			i++;
		}
		
		m_logger.info("makeDeleteSql:"+sql.toString());
		return sql.toString();
	}
    
	public static String makeUpdateSqlByPk(String table ,Collection<String> columns,Map<String, Boolean> autoPkInfo) throws Exception
	{
		int size =columns.size();
		StringBuilder sql=new StringBuilder();
		sql.append("update "+table +" set ");
		int columnIndex = 0;
		for(String column:columns)
		{
			columnIndex++;
			//是否为主键,主键不能修改
			if(autoPkInfo.containsKey(column) && autoPkInfo.get(column))
			{
				continue;
			}
			//因为主键肯定是加到集合的最后位置,所以size需要减autoPkInfo.size
			if(columnIndex < size)
				sql.append(column + " =?,");
			else
				sql.append(column + "=?");
		}
		if(autoPkInfo ==null || autoPkInfo.size() == 0){
			throw new SQLException("EntitySqlMarker makeUpdateSqlByPk pkColumn is null");
		}
		int i = 0;
		for(String pk : autoPkInfo.keySet())
		{
			if(i==0){
				sql.append(" where "+pk+" = ?");
			}else{
				sql.append(" and "+pk+ " = ?");
			}
			i++;
		}
		m_logger.info("makeUpdateSqlByPk:"+sql.toString());
		return sql.toString();
	}
	

	public static String makeInsertSql(String table, Collection<String> columns,Map<String, Boolean> autoPkInfo)
	{
		int size =columns.size();
		StringBuilder sql =new StringBuilder();
		sql.append("insert into " + table + "(");
		int columnIndex = 0;
		int pkMark = 0; //有多少个自增的主键
		for(String key : autoPkInfo.keySet()){
			if(autoPkInfo.get(key) && columns.contains(key) ){
				pkMark++;
			}
		}

		for(String columnName :columns)
		{
			
			columnIndex++;
			//是主键而且是自增的
			if(autoPkInfo.containsKey(columnName)  && autoPkInfo.get(columnName)){
				continue;
			}

			if(columnIndex < size){
				sql.append(columnName + ",");
			}else{
				sql.append(columnName);
			}
			
		}
		sql.append(") values(");
		
		int mark =  size-pkMark;
		for (int i = 0; i < mark;i++) {
			if (i < mark-1) {
				sql.append("?,");
			} else {
				sql.append("?)");
			}
		}
		m_logger.info("makeInsertSql:"+sql.toString());
		
		return sql.toString();
	}
	
	public static String makeDeleteSqlByPk(String table,Collection<String> columns,Map<String, Boolean> autoPkInfo) throws Exception
	{
		StringBuilder sql =new StringBuilder();
		sql.append("delete from " + table);
		if(autoPkInfo ==null || autoPkInfo.size() == 0){
			throw new SQLException("EntitySqlMarker makeDeleteSqlByPk pkColumn is null");
		}
		int i = 0;
		for(String pk : autoPkInfo.keySet())
		{
			if(i==0){
				sql.append(" where "+pk+" = ?");
			}else{
				sql.append(" and "+pk+ " = ?");
			}
			i++;
		}
		m_logger.info("makeDeleteSqlByPk:"+sql.toString());		
		return sql.toString();
	}
	
	public static String makeCountSql(String table, List<String> columns)
	{
		StringBuilder sql=new StringBuilder();
		sql.append("select count(*) from " + table);
		if(columns!=null && columns.size()>0)
		{
			int i = 0;
			for(String column:columns)
			{
				if(i==0){
					sql.append(" where "+column+" = ?");
				}else{
					sql.append(" and "+column+ " = ?");
				}
				i++;
			}
		}
		m_logger.info("makeCountSql:"+sql.toString());
		return sql.toString();
	}
	
	public static String makeSelectSql(DBCondition dbCondition) throws Exception
	{
		String sql = addSelectSqlAndWhere(dbCondition);
		if(dbCondition.groups.size() != 0)
		{
			sql = sql + addGroupSql(dbCondition);
		}
		if( !dbCondition.orders.isEmpty())
		{
			sql = sql + addOrderSql(dbCondition);
		}
		if(dbCondition.num != 0)
		{
			sql = sql + addLimitSql(dbCondition.offset, dbCondition.num);
		}
		m_logger.debug(sql+",condition="+dbCondition.whereCondition);
		return sql;
	}
	
	public static String addSelectSqlAndWhere(DBCondition dbCondition)  throws Exception
	{
		
		String tableName =EntityFactory.getEntityTableName(dbCondition.table);
		if(BlankUtil.isBlank(tableName))
		{
			throw new SQLException("EntitySqlMarker addSelectSqlAndWhere "+dbCondition.table+" relation table is null");
		}
		String sql = "select * from " + tableName;
		
		if(dbCondition.whereCondition.getConditons() != null
			&& !dbCondition.whereCondition.getConditons().isEmpty())
		{
			sql += addWhereSql(dbCondition);
		}
		return sql;
	}
	
	public static String addWhereSql(DBCondition dbCondition) throws Exception
	{
		WhereCondition condition =dbCondition.whereCondition;
		
		if(condition.getConditons().size()==0)
			return "";
		StringBuilder str = new StringBuilder(" where ");
		int index = 0;
		for(WhereCondition.Condition c : condition.getConditons())
		{
			index++;
			String cName = EntityFactory.getEntityColumnName(dbCondition.table,c.columnName );
			if(BlankUtil.isBlank(cName))
			{
				throw new SQLException("EntitySqlMarker addWhereSql "+dbCondition.table+" "+c.columnName+" relation column is null");
			}
			str.append(cName).append(c.compareType.getName());
			str.append("?");
			
			if(index < condition.getConditons().size())
			{
				if(c.stype == WhereCondition.SelectType.OR)
					str.append(" or ");
				else
					str.append(" and ");
			}
			
		}
		return str.toString();
	}
	
	public static String addGroupSql(DBCondition dbCondition)  throws Exception
	{
		List<String> groups = dbCondition.groups;
		StringBuilder groupSql = new StringBuilder(" group by ");
		
		for(String group : groups)
		{
			String cName = EntityFactory.getEntityColumnName(dbCondition.table,group);
			if(BlankUtil.isBlank(cName))
			{
				throw new SQLException("EntitySqlMarker addGroupSql "+dbCondition.table+" "+group+" relation column is null");
			}
			groupSql.append(" ");
			groupSql.append(cName);
			groupSql.append(",");
		}
		String ret = groupSql.substring(0, groupSql.lastIndexOf(","));
		
		return ret;
	}
	
	public static String addOrderSql(DBCondition dbCondition) throws Exception
	{
		Map<String, DBCondition.OrderType> orders =dbCondition.orders;
		StringBuilder orderSql = new StringBuilder(" order by ");
		Iterator<String> it = orders.keySet().iterator();
		while(it.hasNext())
		{
			String column = it.next();
			String cName = EntityFactory.getEntityColumnName(dbCondition.table,column);
			if(BlankUtil.isBlank(cName))
			{
				throw new SQLException("EntitySqlMarker addOrderSql "+dbCondition.table+" "+column+" relation column is null");
			}
			orderSql.append(cName);
			orderSql.append(" ");
			orderSql.append(orders.get(column));
			orderSql.append(",");
		}
		String ret = orderSql.substring(0, orderSql.lastIndexOf(","));
		return ret;
	}
	
	public static String addLimitSql(int offset, int num)
	{
		return " limit " + offset + "," + num;
	}
	
	public static String makeUpdateSql(DBCondition dbCondition) throws Exception
	{
		String sql = addUpdateSqlAndWhere(dbCondition);
		m_logger.debug(sql+",set="+dbCondition.updateSet+",condition="+dbCondition.whereCondition);
		return sql;
	}
	
	public static String addUpdateSqlAndWhere(DBCondition dbCondition)  throws Exception
	{
		
		String tableName =EntityFactory.getEntityTableName(dbCondition.table);
		if(BlankUtil.isBlank(tableName))
		{
			throw new SQLException("EntitySqlMarker addUpdateSqlAndWhere "+dbCondition.table+" relation table is null");
		}
		String sql = "update " + tableName + " set ";
		
        UpdateSet set = dbCondition.updateSet;
		
		if(set.getSetValues().size()==0)
		{	
			throw new SQLException("EntitySqlMarker addUpdateSqlAndWhere "+dbCondition.table+" setValues is null");
		}
		int index = 0;
		StringBuffer str = new StringBuffer();
		for(UpdateSet.SetValue c : set.getSetValues())
		{
			index++;
			String cName = EntityFactory.getEntityColumnName(dbCondition.table,c.columnName );
			if(BlankUtil.isBlank(cName))
			{
				throw new SQLException("EntitySqlMarker addUpdateSetSql "+dbCondition.table+" "+c.columnName+" relation column is null");
			}
			str.append(cName).append("=?");
			
			if(index < set.getSetValues().size())
			{
				str.append(", ");
			}
			
		}
		sql =sql + str.toString()+" ";
		if(dbCondition.whereCondition.getConditons() != null
			&& !dbCondition.whereCondition.getConditons().isEmpty())
		{
			sql += addWhereSql(dbCondition);
		}
		return sql;
	}
}
