package com.solr.dao.callback;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.solr.util.SetterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;


public class EntitySpecFieldStatmentSetterImpl<T> implements PreparedStatementSetter{

	private static Logger m_logger = LoggerFactory.getLogger(EntitySpecFieldStatmentSetterImpl.class);
	
	private T entity;
	
	private List<Field> fields;
	
	public EntitySpecFieldStatmentSetterImpl(T entity)
	{
		this.entity=entity;
	}
	public EntitySpecFieldStatmentSetterImpl(T entity,List<Field> fields)
	{
		this(entity);
		this.fields=fields;
	}
	
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public void setValues(PreparedStatement ps) throws SQLException {
		try{
			int index=1;
			for(Field field:fields)
			{
		        Class<?> returnClass =(Class<?>)field.getGenericType();
				field.setAccessible(true);
			    Object value = field.get(entity);
                if (m_logger.isDebugEnabled()) {
                    m_logger.debug("field " + field.getName() + "'value is: " + value);
                }
			    SetterUtil.invokeSet(ps, returnClass, value, index);
				index++;
			}
		}catch(Exception e)
		{
			m_logger.error(e.getMessage());
			e.printStackTrace(System.out);
		}
	}

}
