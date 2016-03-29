package com.ground.core.callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractorImpl<T> implements IResultSetExtractor<List<T>> {
	
	  private final IRowMapper<T> rowMapper;

	  public RowMapperResultSetExtractorImpl(IRowMapper<T> rowMapper)
	  {
	    this.rowMapper = rowMapper;
	  }

	  public List<T> extractData(ResultSet rs) throws SQLException
	  {
	    List<T> results =new ArrayList<T>();
	    int rowNum = 0;
	    while (rs.next()) {
	      results.add(this.rowMapper.mapRow(rs, rowNum++));
	    }
	    return results;
	  }
}
