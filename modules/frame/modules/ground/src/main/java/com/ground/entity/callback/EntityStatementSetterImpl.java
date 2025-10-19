package com.ground.entity.callback;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ground.core.callback.IPreparedStatementSetter;
import com.ground.entity.EntityFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntityStatementSetterImpl<T> implements IPreparedStatementSetter
{
	private static Logger m_logger = LoggerFactory.getLogger(EntityStatementSetterImpl.class);
	
	private T entity;
	
    protected Class<T> entityClass;

    private List<String> fields;
    
	public EntityStatementSetterImpl(Class<T> entityClass,T entity)
	{
		this.entityClass = entityClass;
		this.entity=entity;
	}
	
	public EntityStatementSetterImpl(Class<T> entityClass,T entity,List<String> fields)
	{
		this(entityClass,entity);
		this.fields = fields;
	}

	public T getEntity()
	{
		return entity;
	}

	public void setEntity(T entity)
	{
		this.entity = entity;
	}

	@Override
	public void setValues(PreparedStatement ps)
		throws SQLException
	{ 
		if(entityClass==null)
		{
			System.out.println("entityClass is null");
		}
		if(fields ==null || fields.size() == 0){
			fields=new ArrayList<String>();
			Set<String> entityFields= EntityFactory.getEntityColumnMap(entityClass).keySet();
			//Map<String, String> pkField = EntityFactory.getKeyFiledName(entityClass);
			Map<String, Boolean> autoPk =  EntityFactory.getEntityAutoPkMap(entityClass);
			
			for(String f:entityFields)
			{
				//如果是自增主键就移除
				if(autoPk==null || !autoPk.containsKey(f) || !autoPk.get(f)){
					fields.add(f);
				}
			}
		}
		
		try{
			int index=1;
			for(String field:fields)
			{
		        String methodStr="get"+StringUtils.capitalize(field);
				Method getMethod =entityClass.getMethod(methodStr);
				if(getMethod==null)
				{
					methodStr="is"+StringUtils.capitalize(field);
					getMethod =entityClass.getMethod(methodStr);
				}
				if(getMethod==null)
					throw new IllegalArgumentException("sql set value fail!");
				
				Class<?> returnClass = getMethod.getReturnType();
			    Object value = getMethod.invoke(entity);
			    if(null !=value) {
                    if (m_logger.isDebugEnabled()) {
                        m_logger.debug("field " + field + "'value is: " + value);
                    }
                    SetterUtil.invokeSet(ps, returnClass, value, index);
                    index++;
                }
            }
		}catch(Exception e)
		{
			m_logger.error(e.getMessage());
			e.printStackTrace(System.out);
		}
		
	}
	
	
}
