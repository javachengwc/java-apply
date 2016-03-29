package com.solr.dao.callback;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;


public class PkStatementSetterImpl<PK> implements PreparedStatementSetter
{
	private PK id;

	public PkStatementSetterImpl(PK id)
	{
		this.id=id;
	}
	
	public PK getId()
	{
		return id;
	}

	public void setId(PK id)
	{
		this.id = id;
	}

	public void setValues(PreparedStatement preparedStatement) throws SQLException
	{
		if(id instanceof List){
			@SuppressWarnings("unchecked")
			LinkedList<Object> temp = (LinkedList<Object>) id;
			int i = 1;
			for(Object key : temp){
				preparedStatement.setObject(i, key);
				i++;
			}
		}else{
			preparedStatement.setObject(1, id);
		}
	}
	
}
