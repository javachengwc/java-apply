package com.ground.core.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerResultSetExtractorImpl implements IResultSetExtractor<Integer> {

	public Integer extractData(ResultSet rs) throws SQLException
	{
		if(rs.next())
		{
		    Integer value = rs.getInt(1);
		    return value;
		}
		else
			return null;
		
	}
	
}