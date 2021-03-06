package com.solr.service.impl;

import com.solr.model.context.MyContext;
import com.solr.service.ResultHandler;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 
 * 默认json的dataHandler
 * 
 */
public class JsonArrayResultHandler implements ResultHandler {
    
	/**
	 * 默认使用json来处理结果数据
	 */
	public void handlerResultData(HttpServletResponse response, MyContext context) {
		setHttpResponseHead(response);
		//Gson gson=new Gson();
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
//			writer.write(gson.toJson(context.getValueMap()));
			JSONArray jsonAarrayFormater = JSONArray.fromObject(context.getValueMap());
			writer.write(jsonAarrayFormater.toString());
			jsonAarrayFormater=null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(writer!=null){
				writer.flush();
				writer.close();
			}
			//gson=null;
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
