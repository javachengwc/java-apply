package com.solr.service;

import com.google.gson.Gson;
import com.solr.model.context.MyContext;
import com.solr.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class InitInvokeService implements SolrService {

	private static final String BASE_PACKAGE="com.solr.initialize";

	public boolean dealSolrService(HttpServletRequest request, HttpServletResponse response) {
	    String action=request.getParameter("action");
	    //从url中获取相应的类
		String core=request.getParameter("core");
		//从url中获取查询参数
		String id=request.getParameter("id");
		String className=BASE_PACKAGE+"."+core;
		MyContext context=new MyContext();
		Class clazz=null;
		Writer writer=null;
		Gson gson=new Gson();
		try {
			clazz = Class.forName(className);
			Object object=clazz.newInstance();
			Integer[] parameters=null;
			Class[] parametersType=null;
			if(StringUtils.isNotEmpty(id)){
				parameters = new Integer[]{Integer.parseInt(id)};
				parametersType=new Class[1];
				parametersType[0]=int.class;
			}
			ReflectionUtils.invokeMethod(object,action,parametersType,parameters);
			context.set("error","0");
			context.set("msg", "数据更新成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			context.set("error","1");
			context.set("msg", "数据更新失败,错误信息:" + e.getMessage());
		}finally{
			try {
				writer=response.getWriter();
				writer.write(gson.toJson(context.getValueMap()));
				writer.flush();
				if(writer!=null){
				  writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
