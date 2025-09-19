package com.ground.entity.callback;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.ground.core.callback.IPreparedStatementSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreparedStatementSetterImpl implements IPreparedStatementSetter
{
	private static Logger m_logger = LoggerFactory.getLogger(PreparedStatementSetterImpl.class);


    private List<Object> values;
    
	public PreparedStatementSetterImpl(Object... values)
	{
		this.values = Arrays.asList(values);
	}

	@Override
	public void setValues(PreparedStatement ps)
		throws SQLException
	{ 
		if(values ==null || values.size() == 0){
			return;
		}
		
		try{
			int index=1;
			for(Object value:values)
			{
				SetterUtil.invokeSet(ps,value.getClass(),value,index);
		    	index++;
                if (m_logger.isDebugEnabled()) {
                    m_logger.debug("value is: " + value.toString());
                }
            }
		}catch(Exception e)
		{
			m_logger.error(e.getMessage());
			e.printStackTrace(System.out);
		}
		
	}
	
	
}
