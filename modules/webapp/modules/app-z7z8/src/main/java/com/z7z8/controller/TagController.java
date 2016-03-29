package com.z7z8.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.z7z8.model.TagRecord;
import com.z7z8.service.tag.TagManager;
import com.z7z8.service.tag.TagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("tag")
public class TagController {
	
	@Autowired
	private TagService tagService;
	
	@ResponseBody
	@RequestMapping("getTag")
	public Object getTag(String u,Integer s )
	{
		if(StringUtils.isBlank(u))
		{
			return null;
		}
		if(s==null)
		{
			s=1;
		}
		List<String> list = tagService.getTags(u,s);
		return transToJSONStr(list);
		
	}
	
	public String transToJSONStr(List<String> list)
	{
		if(list!=null && list.size()>0)
		{
			StringBuffer buf = new StringBuffer();
			buf.append("{");
			boolean start=true;
			for(String s:list)
			{
				if(!start)
				{
					buf.append(",");
				}
				buf.append("\"").append(s).append("\"").append(":").append("1");
				start=false;
			}
			buf.append("}");
			return buf.toString();
		}
		return null;
	}
	
	
	@ResponseBody
	@RequestMapping("queryTagPage")
	public Object queryTagPage(Integer pageNo ,Integer pageSize)
	{
		if(pageNo==null)
		{
			pageNo=1;
		}
		if(pageSize==null)
		{
			pageSize=50;
		}
		int start = (pageNo-1)*pageSize;
		
		Map<String,Object> map = new HashMap<String,Object>(2);
		List<TagRecord> list = tagService.queryTagPage(start, pageSize);
		map.put("error", 0);
		map.put("data", list);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("reloadTagData")
	public Object reloadTagData()
	{
		TagManager.getInstance().init();
		return getCacheTagInfo();
	}
	
	@ResponseBody
	@RequestMapping("getCacheTagInfo")
	public Object getCacheTagInfo()
	{
		String info = TagManager.getInstance().dataInfo();
        Map<String,Object> map = new HashMap<String,Object>(2);
        map.put("error", 0);
        map.put("info", info);
		return map;
	}

}
