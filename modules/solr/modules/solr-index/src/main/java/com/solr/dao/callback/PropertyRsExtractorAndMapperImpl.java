package com.solr.dao.callback;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.solr.model.base.Property;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class PropertyRsExtractorAndMapperImpl implements ResultSetExtractor<Property>,RowMapper<Property>{

	public Property wrapRs(ResultSet rs)  throws SQLException
	{
		ResultSetMetaData rsmt = rs.getMetaData();
		Property property = new Property();
		
		int columnCount = rsmt.getColumnCount();
	
		for(int i = 1; i <= columnCount; i++)
		{
			String name = rsmt.getColumnName(i);
			String value = rs.getString(i);
			property.put(name, value);
		}
	
		return property;	
	}
	public Property extractData(ResultSet rs) throws SQLException
	{
		if(rs.next())
		    return wrapRs(rs);
		else
			return null;
		
	}
	public Property mapRow(ResultSet rs, int paramInt) throws SQLException
    {
	    return wrapRs(rs);	
    }
}
