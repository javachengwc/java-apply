package com.ground.entity;

import com.util.lang.PackageUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class RegistEntity
{

	public static void registPakageEntity(String scanPackage)
	{
		if(StringUtils.isBlank(scanPackage)) {
            return;
        }
		String []perPackage = scanPackage.split(",");
		Set<String> clazzs =new HashSet<String>();
		for(String pkg:perPackage)
		{
		    String [] pkgClazzs = PackageUtil.findClassesInPackage(pkg);
		    if(pkgClazzs!=null)
		        Collections.addAll(clazzs,pkgClazzs);
		}
		EntityFactory.regist(clazzs);
	}
}
