package com.ground.entity.callback;

import com.ground.core.callback.IPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class PkStatementSetterImpl<PK> implements IPreparedStatementSetter
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

	@Override
	public void setValues(PreparedStatement preparedStatement)
		throws SQLException
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
			
		}
	}
	
}
