package com.util.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.pool.ObjectPool;

public class MapObjectPool
{
	private Map<Object, ObjectPool> m_pools = new HashMap<Object, ObjectPool>();
	
	public MapObjectPool()
	{
		
	}
	
	public void putObjectPool(Object k, ObjectPool pool)
	{
		m_pools.put(k, pool);
	}
	
	public int size()
	{
		return m_pools.size();
	}
	
	public boolean containsKey(Object k)
	{
		return m_pools.containsKey(k);
	}
	
	public Object borrowObject(Object k)
		throws Exception
	{
		ObjectPool op = m_pools.get(k);
		if(op != null)
			return op.borrowObject();
		
		return null;
	}
	
	public void returnObject(Object k, Object o)
		throws Exception
	{
		ObjectPool op = m_pools.get(k);
		if(op != null)
			op.returnObject(o);
	}
	/**
	 * 获得pool的状态
	 * @return
	 */
	public String getPoolsStatus()
	{
		Iterator<Entry<Object, ObjectPool>> its = m_pools.entrySet().iterator();
		StringBuilder dblog = new StringBuilder();
		while(its.hasNext())
		{
			Entry<Object, ObjectPool> en = its.next();
			Object index = en.getKey();
			ObjectPool value = en.getValue();
			int numActive = value.getNumActive();
			int numIdle = value.getNumIdle();
			dblog.append("key=" + index + ",numActive=" + numActive
				+ ",numIdle=" + numIdle);
			dblog.append("\n");
		}
		return dblog.toString();
		
	}
}
