package com.ground.entity.callback;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ground.condition.DBCondition;
import com.ground.condition.UpdateSet;
import com.ground.condition.WhereCondition;
import com.ground.core.callback.IPreparedStatementSetter;
import com.ground.entity.EntityFactory;
import org.apache.log4j.Logger;


public class EntityConditionStatmentSetterImpl implements IPreparedStatementSetter {

	private static Logger m_logger = Logger.getLogger(EntityConditionStatmentSetterImpl.class);
	
	private DBCondition dbCondition;
	
	private Class<?> clazz;
	
	public EntityConditionStatmentSetterImpl(Class<?> clazz,DBCondition dbCondition)
	{
		this.clazz=clazz;
		this.dbCondition=dbCondition;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public DBCondition getDbCondition() {
		return dbCondition;
	}
     

	public void setDbCondition(DBCondition dbCondition) {
		this.dbCondition = dbCondition;
	}

	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
		try{
			int index=1;
			if(dbCondition.updateSet.getSetValues()!=null && !dbCondition.updateSet.getSetValues().isEmpty())
			{
				for(UpdateSet.SetValue c:dbCondition.updateSet.getSetValues())
				{
					
					String fieldName =c.columnName;
					Class<?> returnClass = EntityFactory.getEntityFieldType(clazz.getName(), fieldName);
					Object value =c.value;
					SetterUtil.invokeSet(ps,returnClass,value,index);
					index++;
				}
			}
			
			if(dbCondition.whereCondition.getConditons() != null
					&& !dbCondition.whereCondition.getConditons().isEmpty())
			{
				
				for(WhereCondition.Condition c : dbCondition.whereCondition.getConditons())
				{
					String fieldName =c.columnName;
					Class<?> returnClass =EntityFactory.getEntityFieldType(clazz.getName(), fieldName);
					Object value =c.value;
                    if (m_logger.isDebugEnabled()) {
                        m_logger.debug("field " + fieldName + "'value is: " + value);
                    }
					SetterUtil.invokeSet(ps,returnClass,value,index);
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