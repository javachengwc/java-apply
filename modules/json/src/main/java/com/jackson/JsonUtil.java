package com.jackson;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Jackson 1.x版本的包名是codehaus
 * Jackson 2.x版本的包名是fasterxml,Jackson从2.0开始改用新的包名fasterxml
 */
public class JsonUtil {
    private ObjectMapper mapper;

	public JsonUtil(JsonSerialize.Inclusion inclusion) {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(inclusion);
        //mapper.getSerializationConfig().setSerializationInclusion(inclusion);  //以前版本的用法
        mapper.getDeserializationConfig().without(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        //mapper.getDeserializationConfig().set(
        //		org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);   //以前版本的用法
	}

	/**
	 * 创建输出全部属性到Json字符串的Binder.
	 */
	public static JsonUtil buildNormalBinder() {
		return new JsonUtil(JsonSerialize.Inclusion.ALWAYS);
	}

	/**
	 * 创建只输出非空属性到Json字符串的Binder.
	 */
	public static JsonUtil buildNonNullBinder() {
		return new JsonUtil(JsonSerialize.Inclusion.NON_NULL);
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Binder.
	 */
	public static JsonUtil buildNonDefaultBinder() {
		return new JsonUtil(JsonSerialize.Inclusion.NON_DEFAULT);
	}

	/**
	 * 如果JSON字符串为Null或"null"字符串,返回Null.
	 * 如果JSON字符串为"[]",返回空集合.
	 *
	 * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句:
	 * List<MyBean> beanList = binder.getMapper().readValue(listString, new TypeReference<List<MyBean>>() {});
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			return null;
		}
	}

    public Object fromJson(String jsonString, TypeReference clazz) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString,clazz);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 如果对象为Null,返回"null".
	 * 如果集合为空集合,返回"[]".
	 */
	public String toJson(Object object) {
        try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 设置转换日期类型的format pattern,如果不设置默认打印Timestamp毫秒数.
	 */
	public void setDateFormat(String pattern) {
		if (!StringUtils.isBlank(pattern)) {
			DateFormat df = new SimpleDateFormat(pattern);
			mapper.getSerializationConfig().setDateFormat(df);
			mapper.getDeserializationConfig().setDateFormat(df);
		}
	}

	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}

}
