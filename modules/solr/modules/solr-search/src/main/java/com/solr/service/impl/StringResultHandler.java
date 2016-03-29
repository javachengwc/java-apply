package com.solr.service.impl;

import com.google.gson.Gson;
import com.solr.model.context.MyContext;
import com.solr.service.ResultHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StringResultHandler implements ResultHandler {


	/**
	 *返回字符串的数据
	 */
	public void handlerResultData(HttpServletResponse response, MyContext context) {
		setHttpResponseHead(response);
		Gson gson=new Gson();
		PrintWriter writer = null;
		StringBuffer sb=new StringBuffer();
		try {
			writer = response.getWriter();
			sb.append("jsonCatCallback(");
			sb.append(gson.toJson(context.getValueMap()));
			//处理数组数据
			String[] valueArray=context.getValueArray();
			if(valueArray!=null){
				for(String value:valueArray){
					sb.append(",'"+value+"'");
				}
			}
			sb.append(")");
			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer!=null){
				writer.flush();
				writer.close();
				
			}
			gson=null;
            //System.gc();
			//jsonAarrayFormater=null;
		}
		
		

	}
	
	/**
	 * 设置请求头信息
	 * @param response
	 */
	private void setHttpResponseHead(HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
	}

}
