package com.manage.dao.ibatis;

public class BaseKeyGenerator {
	
	public static String generateGetById(Class clazz)
	{
		return "get"+clazz.getSimpleName()+"ById";
	}
	public static String generateDeleteById(Class clazz)
	{
		return "delete"+clazz.getSimpleName()+"ById";
	}
	public static String generateGetAll(Class clazz)
	{
		return "get"+clazz.getSimpleName()+"All";
	}
	public static String generateSave(Class clazz)
	{
		return "insert"+clazz.getSimpleName();
	}
	public static String generateUpdate(Class clazz)
	{
		return "update"+clazz.getSimpleName();
	}
    
	public static String generateFindPage(Class clazz)
	{
		return "findPage"+clazz.getSimpleName();
	}
	
	public static String generatePageTotalCount(Class clazz)
	{
		return "findPage"+clazz.getSimpleName()+"TotalCount";
	}
}
