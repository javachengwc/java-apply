package com.rule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rule.dto.RuleResponse;
import com.rule.util.GeneralHttpExecutor;
import com.rule.util.SysConfig;
import com.rule.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 规则服务类
 */
@Service
public class RuleService {

	private static Logger logger = LoggerFactory.getLogger(RuleService.class);

    public String getQueryUrl() {

        return SysConfig.getValue("query_url");
    }

    public String getQueryListUrl() {

        return SysConfig.getValue("query_list_url");
    }

    /**
     * 单条规则查询
     */
    public Object query(String data)
    {

        JSONObject json = paramToJSONObject(data);

        String result = null;

        try{
            result = queryRuleData(data,0);
            if(!StringUtils.isBlank(result))
            {
                //JSONObject jn = JSON.parseObject(result);可能会改变key的顺序
                return result;
            }
            return new RuleResponse(1,"result为空或转成json失败",result);

        }catch(Exception e)
        {
            logger.error("RuleService do http query error,",e);
            return new RuleResponse(1,"查询失败,"+e.getClass().getName()+" "+ e.getMessage());
        }
    }

    /**
     * 批量查询 jsonArray或则json格式
     */
    public Object queryList(String param)
    {
       JSONObject data = paramToJSONObject(param);
       JSONArray services= data.getJSONArray("serviceInfos");
       String format =data.getString("format");
       JSONArray passServices = new JSONArray();
       JSONArray passParams = new JSONArray();

       Map<Integer,RuleResponse> checkNoPass = new HashMap<Integer,RuleResponse>();
       int servicesCount = ( services==null)?0:services.size();

        if(servicesCount<=0)
       {
    	   return new RuleResponse(1,"无规则调用"); 
       }

       for(int i=0;i<servicesCount ;i++)
       {
    	   JSONObject json = (JSONObject)services.get(i);
    	   
    	    String serviceName = json.getString("serviceName");
    	   if(StringUtils.isBlank(serviceName))
    	   {
    		    return new RuleResponse(1,"参数错误，存在serviceName为空的规则数据");  
    	   }

       }

       data.put("serviceInfos", passServices);
       data.put("params", passParams);
 
       String result=null;
       try{  
    	   result = queryRuleData(param, 1);
	   		
	   }catch(Exception e)
	   {
	   		logger.error("RuleService do http queryList error,",e);
	   		return new RuleResponse(1,"查询失败,"+e.getClass().getName()+" "+ e.getMessage());
	   }
       if(StringUtils.isBlank(result))
       {
    	   return new RuleResponse(1,"查询结果为空");
       }
       
       Object out =null;
       if("json".equalsIgnoreCase(format))
       {
    	    out= dealQueryListJSONResult(result,checkNoPass,servicesCount);
       }
       if("xml".equalsIgnoreCase(format))
       {
    	   out = dealQueryListXmlResult(result,checkNoPass,servicesCount);
       }
       services= null;
       passServices = null;
       passParams = null;
       return out;
    }

    /**
     * 查询
     */
    public String queryRuleData(String param,int queryFlag) throws Exception
    {
        String url =getQueryUrl();
        if(queryFlag==1)
        {
            url = getQueryListUrl();
        }

        String result = GeneralHttpExecutor.post(url, param);

    	return result;
    }


	public JSONObject paramToJSONObject(String  data)
	{
		try{
			JSONObject json=JSON.parseObject(data);
			return json;
		}catch(Exception e)
		{
			logger.error("RuleService paramToJSONObject error,",e);
			return null;
		}
	}
	
	private String failInfo2Xml(JSONObject jsonResult) {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("result");
        for (String key : jsonResult.keySet()) {
        	rootElement.addAttribute(key, ((jsonResult.get(key)==null)?"":jsonResult.get(key).toString()));
        }
        return document.asXML();
    }

    /**
     * 批量查询处理json格式结果
     * @return
     */
    public Object dealQueryListJSONResult(String result, Map<Integer,RuleResponse> checkNoPass,  int servicesCount)
    {
        JSONObject out = null;
        try{
            if(!StringUtils.isBlank(result));
            {
                try{
                    out = JSON.parseObject(result);
                }catch(Exception e)
                {
                    logger.error("RuleService queryList result parse json  error");
                    return new RuleResponse(1,"result转成json失败",result);
                }
            }

        }catch(Exception e)
        {
            logger.error("RuleService do http queryList error,",e);
            return new RuleResponse(1,"查询失败,"+e.getClass().getName()+" "+ e.getMessage());
        }
        if(out==null)
        {
            return new RuleResponse(1,"查询结果为空");
        }
        if(checkNoPass==null || checkNoPass.size()<=0)
        {
            return out;
        }
        if(!"0".equals(out.getString("code")))
        {
            //返回错误
            return out;
        }
        JSONArray rowsList = out.getJSONArray("rowsList");
        if(rowsList==null)
        {
            rowsList= new JSONArray();
        }
        for(int i=0;i<servicesCount ;i++)
        {
            if(checkNoPass.keySet().contains(i))
            {
                if(rowsList.size()<i)
                {
                    rowsList.add(new JSONArray());
                }else
                {
                    rowsList.add(i, new JSONArray());
                }
            }
        }
        out.put("rowsList", rowsList);
        out.put("extraInfo", checkNoPass);
        StringBuffer buf = new StringBuffer();
        for(Integer i: checkNoPass.keySet())
        {
            buf.append(i).append(",");
        }
        String failRule =buf.toString();
        if(failRule.endsWith(","))
        {
            failRule =failRule.substring(0,failRule.length()-1);
        }
        out.put("failRule", failRule);
        rowsList=null;
        return out;
    }

    /**
     * 批量查询处理xml格式结果
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object dealQueryListXmlResult(String result, Map<Integer,RuleResponse> checkNoPass, int servicesCount)
    {
        if(checkNoPass==null || checkNoPass.size()<=0)
        {
            return result;
        }

        if(result.startsWith("<"))
        {
            Document doc = null;
            try{

                doc= XmlUtil.parse(result);

            }catch(Exception e)
            {
                logger.error("RuleService dealQueryListXmlResult trans to document error,",e);
                JSONObject obj = new JSONObject();
                obj.put("error", 1);
                obj.put("msg", "查询失败,"+e.getClass().getName()+" "+ e.getMessage());
                return failInfo2Xml(obj);
            }
            if(doc==null)
            {
                JSONObject obj = new JSONObject();
                obj.put("error", 1);
                obj.put("msg", "查询结果为空");
                return failInfo2Xml(obj);
            }

            Element root =doc.getRootElement();//result节点
            Element resultEle =root;
            if(!"result".equalsIgnoreCase(root.getName()))
            {
                resultEle =(Element)root.element("result");
            }

            if(resultEle.attribute("code")!=null && !"0".equals(resultEle.attribute("code").getValue()))
            {
                //返回错误
                return result;
            }

            List<Element> listDatas = (List<Element> )resultEle.elements("rows");  //

            if(listDatas==null)
            {
                listDatas= new ArrayList<Element>();
            }
            for(int i=0;i<servicesCount ;i++)
            {
                if(checkNoPass.keySet().contains(i))
                {
                    Element tmp = DocumentHelper.createElement("rows");//创建一个element
                    if(listDatas.size()<i)
                    {

                        listDatas.add(tmp);
                    }else
                    {
                        listDatas.add(i,tmp);
                    }
                }
            }

            Element extraInfoEle = resultEle.addElement("extraInfo");
            for(Integer key:checkNoPass.keySet())
            {
                Element pEle=extraInfoEle.addElement("info");
                pEle.addAttribute("index", ""+key);
                RuleResponse rsp =  checkNoPass.get(key);
                if(rsp!=null)
                {
                    JSONObject jsn = (JSONObject)JSON.toJSON(rsp);

                    for (String p : jsn.keySet()) {
                        pEle.addAttribute(p, ((jsn.get(p)==null)?"":jsn.get(p).toString()));
                    }
                }
            }

            StringBuffer buf = new StringBuffer();
            for(Integer i: checkNoPass.keySet())
            {
                buf.append(i).append(",");
            }
            String failRule =buf.toString();
            if(failRule.endsWith(","))
            {
                failRule =failRule.substring(0,failRule.length()-1);
            }
            Element failRuleEle = resultEle.addElement("failRule");
            failRuleEle.setText(failRule);
            listDatas=null;
            return doc.asXML();

        }else
        {
            return result;
        }
    }
}
