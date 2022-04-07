package com.z7z8.service.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.z7z8.model.TagRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TagService {
	
	private static Logger logger = LoggerFactory.getLogger(TagService.class);

	public List<String> getTags(String url,int s)
	{
		String exactTag = TagManager.getInstance().getExactTag(""+s+"_"+url);
		List<String> tagList = TagManager.getInstance().getRuleTag(url,s);
		if(tagList==null && StringUtils.isBlank(exactTag))
		{
			return null;
		}
		List<String> list = new ArrayList<String>();
		if(!StringUtils.isBlank(exactTag))
		{
			list.addAll(Arrays.asList(exactTag.split(",")));
		}
		if(tagList!=null && tagList.size()>0)
		{
			list.addAll(tagList);
		}
		return list;
	}
	
	/**
     * 分页查询规则
     */
    public static List<TagRecord> queryTagPage(int start,int size) {
		
    	List<TagRecord> list = null;

		return list;
  	}
}
