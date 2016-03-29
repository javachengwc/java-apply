package com.rule.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rule.dto.RuleResponse;
import com.rule.service.RuleService;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("rule")
public class RuleController {
	
	private static Logger logger = Logger.getLogger(RuleController.class);
	
	@Autowired
	private RuleService ruleService;
	
	/**
	 * 规则查询
	 * data json格式的规则查询参数
	 */
	@ResponseBody
	@RequestMapping("query")
	public Object query(HttpServletRequest request, HttpServletResponse response,String data)
	{
		if(StringUtils.isBlank(data))
		{
			return new RuleResponse(1,"参数不能为空");
		}
		JSONObject json = paramToJSONObject(data);
		if(json==null)
		{
		    return new RuleResponse(1,"参数效验失败,非json格式");
		}
		Object rt= ruleService.query(data);
		String format =json.getString("format");
		return renderResult(response,rt,format);
		
	}

	private Object renderResult(HttpServletResponse response,Object rt,String format)
	{
		if("json".equalsIgnoreCase(format))
		{
			return rt;
			
		}else if("xml".equalsIgnoreCase(format))
		{
			if(rt instanceof String)
			{
				String outStr=(String)rt;
				if(outStr.startsWith("<"))
				{
					HttpRenderUtil.renderXML(outStr, response);
				}else
				{
					JSONObject rtJson =paramToJSONObject(outStr);
					if(rtJson!=null)
					{
						String result=data2Xml(rtJson);
						HttpRenderUtil.renderXML(result, response);
					}else
					{
						HttpRenderUtil.renderText(outStr, response);
					}
				}
				
			}else
			{
				JSONObject jsonResult = (JSONObject)JSON.toJSON(rt);
				String result=data2Xml(jsonResult);
				HttpRenderUtil.renderXML(result, response);
			}
			return null;
		}else
		{
			return rt;
		}
	}
	
	@ResponseBody
	@RequestMapping("queryList")
	public Object queryList(HttpServletRequest request, HttpServletResponse response,String data)
	{
		if(StringUtils.isBlank(data))
		{
			return new RuleResponse(1,"参数不能为空");
		}
		JSONObject json = paramToJSONObject(data);
		if(json==null)
		{
		    return new RuleResponse(1,"参数效验失败,非json格式");
		}

		Object rt= ruleService.queryList(data);
		String format =json.getString("format");
		return renderResult(response,rt,format);
	}
	
	/**
	 * 检查参数是否json
	 */
	public JSONObject paramToJSONObject(String  data)
	{
		try{
			JSONObject json=JSON.parseObject(data);
			return json;
		}catch(Exception e)
		{
			logger.error("RuleController paramToJSONObject error,",e);
			return null;
		}
	}
	
	private String data2Xml(JSONObject jsonResult) {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("result");
        for (String key : jsonResult.keySet()) {
        	rootElement.addAttribute(key, ((jsonResult.get(key)==null)?"":jsonResult.get(key).toString()));
        }
        return document.asXML();
    }
}
