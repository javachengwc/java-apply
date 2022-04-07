package com.manage.dao.ibatis;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.manage.dao.EntityDao;

public abstract class BaseIbatisDao<E,PK extends Serializable> extends SqlMapClientDaoSupport implements EntityDao<E,PK>
{
    
    protected Log log = LogFactory.getLog(getClass());
		
    public abstract Class<E> getEntityClass();

    /**
     * 获取某位置运行时泛型类型
     * @param index
     * @return
     */
    public Class getGenericType(int index) {

        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("BaseMybatisDao  getGenericType iIndex outof bounds");
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
    
	@SuppressWarnings("unchecked")
	public E getById(PK id) {
		return (E)getSqlMapClientTemplate().queryForObject(BaseKeyGenerator.generateGetById(getEntityClass()), id);
	}
	
	public void deleteById(PK id) {
		Object entity = getById(id);
		if(entity == null) {
			throw new ObjectRetrievalFailureException(getEntityClass(),id);
		}
		getSqlMapClientTemplate().delete(BaseKeyGenerator.generateDeleteById(getEntityClass()), id);
		
	}

	public void save(E entity) {
		getSqlMapClientTemplate().insert(BaseKeyGenerator.generateSave(getEntityClass()),entity);
	}
	
	public void update(E entity) {
		getSqlMapClientTemplate().update(BaseKeyGenerator.generateUpdate(getEntityClass()), entity);
	}
    
	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		List<E> list= (List<E>)getSqlMapClientTemplate().queryForList(BaseKeyGenerator.generateGetAll(getEntityClass()));
		return list;
	}	
}
