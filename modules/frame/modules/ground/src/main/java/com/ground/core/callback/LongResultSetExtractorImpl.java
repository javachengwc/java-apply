package com.ground.core.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongResultSetExtractorImpl  implements IResultSetExtractor<Long> {

	public Long extractData(ResultSet rs) throws SQLException
	{
		if(rs.next())
		{
		    Long value = rs.getLong(1);
		    return value;
		}
		else
			return null;
		
	}
	
}