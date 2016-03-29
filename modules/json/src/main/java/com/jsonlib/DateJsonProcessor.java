package com.jsonlib;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DateJsonProcessor implements JsonValueProcessor {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private DateFormat dateFormat;   
    
    private DateFormat timestampFormat;
  
    public DateJsonProcessor(){

    	dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
    	timestampFormat = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
    }
    
    public DateJsonProcessor(String datePattern) {

        try {   
            dateFormat = new SimpleDateFormat(datePattern);   
        } catch (Exception ex) {
        	dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        }
        timestampFormat = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
    }   
  
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {   
        return process(value);   
    }   
  
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);   
    }   
  
    private Object process(Object value) {
    	if(value == null) return null;
    	if("Timestamp".equals(value.getClass().getSimpleName())){
	        return timestampFormat.format((Timestamp) value);   
    	}
        return dateFormat.format((Date) value);   
    } 
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
} 
