package com.z7z8.service.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.z7z8.model.TagRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TagManager {
	
	private static Logger logger = LoggerFactory.getLogger(TagManager.class);
	
	private static TagManager inst = new TagManager();

	//精确的url-tag数据
	private ConcurrentHashMap<String,String> exactDataMap = new ConcurrentHashMap<String,String>(5000);

	//正则tag数据
	private List<TagRecord> ruleDataList = new ArrayList<TagRecord>();
	
	
	public static TagManager getInstance()
	{
		return inst;
	}
	
	private TagManager()
	{
		
	}
	
	public void init()
	{
		logger.error("TagManager init start");
        List<TagRecord> pageList = new ArrayList<TagRecord>();
		int per_num=1000;
		int start =0;
		int t=0;//次数
		List<TagRecord> tmpRuleDataList =new ArrayList<TagRecord>();
		boolean isFirst=true;
		while(true)
		{
            pageList =TagService.queryTagPage(start,per_num);
            int count = ( (pageList==null)?0:pageList.size() );
            if(count>0)
            {
            	if(isFirst)
            	{
            		exactDataMap.clear();
            		isFirst=false;
            	}
            	for(TagRecord r: pageList)
    			{
            		if(StringUtils.isBlank(r.getTag()) || StringUtils.isBlank(r.getUrl()))
            		{
            			continue;
            		}
            		if(r.getMatchType()==2)
            		{
            			r.producePattern();
            			tmpRuleDataList.add(r);
            		}
            		else
            		{
            			String oldTag= exactDataMap.putIfAbsent(r.getSiteType()+"_"+r.getUrl(),r.getTag());
        				
        				//表示原来有值
        				if(!StringUtils.isBlank(oldTag))
        				{
        					exactDataMap.put(r.getSiteType()+"_"+r.getUrl(),oldTag+","+r.getTag());
        				}
            		}
    			}
            }
            if(pageList!=null) {
                pageList.clear();
            }

	     	if(count<per_num)
	     	{
	     		break;
	     	}
	     	
	     	t++;
	     	start =t*per_num; 
		}
		ruleDataList = tmpRuleDataList;
		
		logger.error("TagManager init end");
	}
	
	public String getExactTag(String combinUrl)
	{
		return exactDataMap.get(combinUrl);
	}
	
	public List<String> getRuleTag(String url,int siteType)
	{
		List<String> tags = null;
		for(TagRecord tagRecord:ruleDataList)
		{
			Pattern p = tagRecord.getUrlPattern();
			Matcher m  =p.matcher(url);
			if(m.find())
			{
				if(tagRecord.getSiteType()!=null  && tagRecord.getSiteType()==siteType)
				{
					if(tags==null)
					{
						tags= new ArrayList<String>();
					}
					tags.add(tagRecord.getTag());
				}
			}
		}
		return tags;
	}
	
	public String dataInfo()
	{
		return "exactDataMap:"+exactDataMap.size()+",ruleDataList:"+ruleDataList.size();
	}
}
