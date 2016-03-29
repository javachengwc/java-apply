package com.jsonlib;


import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

public class JsonConfigBuilder {

	public static JsonConfig createJsonConfig(String[] excludes) {
		JsonConfig jsonConfig = new JsonConfig();
        //json里面的类型过滤配置
		jsonConfig.setJsonPropertyFilter(new PrimitiveJsonFilter());
        //json里面的字段过滤
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        //json里的类型数据序列化配置
		DateJsonProcessor jsonValueProcessor = new DateJsonProcessor();
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, jsonValueProcessor);
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, jsonValueProcessor);
		return jsonConfig;
	}
	
	public static JsonConfig createJsonConfig() {
		return createJsonConfig(null);
	}
	
}
