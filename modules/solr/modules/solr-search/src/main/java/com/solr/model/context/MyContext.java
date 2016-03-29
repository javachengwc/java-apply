package com.solr.model.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存数据集合Context
 */
public class MyContext {
	
	public MyContext(){
	}
	 /**数据存在集合*/
    private Map<String,Object> valueMap = new HashMap<String,Object>(64);
    private String[] valueArray;

	public void set(String name,Object value){
    	Object vobj = valueMap.put(name, value);
    	vobj = null;
    }
	public Map<String, Object> getValueMap() {
		return valueMap;
	}
	public void setValueMapNull(){
		valueMap.clear();
		this.valueMap=null;
		this.valueArray=null;
	}
	public String[] getValueArray() {
		return valueArray;
	}
	public void setValueArray(String[] valueArray) {
		this.valueArray = valueArray;
	}
}

