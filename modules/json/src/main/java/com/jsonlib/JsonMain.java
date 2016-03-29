package com.jsonlib;

import com.model.SiteAccessInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * json-lib包的json相关操作
 * 优点：各种复杂json格式和对json处理的一般需求都可以做到
 * 缺点：所需支持的jar文件较多，并且关于jar包版本或许会出现一些杂七杂八的问题
 */
public class JsonMain {

    public static void main(String args []) throws Exception
    {
        //obj-->json
        SiteAccessInfo info =generalSiglData();
        JSONObject json = JSONObject.fromObject(info);
        System.out.println(json.toString());

        JSONArray array = getSiteInfoList();
        System.out.println(array.toString());

        //json-->obj
        SiteAccessInfo info2=(SiteAccessInfo)JSONObject.toBean(json,SiteAccessInfo.class);
        System.out.println(info2.toString());

        List<SiteAccessInfo> list2 = (List<SiteAccessInfo>)JSONArray.toCollection(array, SiteAccessInfo.class);
        if(list2!=null && list2.size()>0)
        {
            System.out.println("------------print list---------");
            for(SiteAccessInfo s:list2) {
                System.out.println(s.toString());
            }
        }

    }

    private static JSONArray getSiteInfoList() {

        List<SiteAccessInfo> list = generalTestData();
        //json装配的时候 排除site，wxExp，pcExp这些字段
        JSONArray array = JSONArray.fromObject(list,JsonConfigBuilder.createJsonConfig(new String[]{"site", "wxExp", "pcExp"}));
        return array;
    }

    public static List<SiteAccessInfo> generalTestData()
    {
        List<SiteAccessInfo> list = new ArrayList<SiteAccessInfo>();
        SiteAccessInfo info = new SiteAccessInfo();
        info.setSite("http://www.baidu.com");
        info.setName("百度");
        info.setUv(10000 * 10000l);
        info.setOut(10000 * 1000l);
        info.setPcExp(10000 * 9000l);
        info.setWxExp(10000 * 2000l);
        info.setAddTime(new Date());
        list.add(info);

        SiteAccessInfo info2 = new SiteAccessInfo();
        info2.setSite("http://www.yy.com");
        info2.setName("YY");
        info2.setUv(1000*5000l);
        info2.setOut(1000*3000l);
        info2.setPcExp(1000*700l);
        info2.setWxExp(1000*2000l);
        list.add(info2);

        return list;
    }

    public static SiteAccessInfo generalSiglData()
    {
        SiteAccessInfo info = new SiteAccessInfo();
        info.setSite("http://www.xxx.com");
        info.setName("xxx");
        info.setUv(3000 * 10000l);
        info.setOut(3000 * 1000l);
        info.setPcExp(3000 * 9000l);
        info.setWxExp(3000 * 2000l);
        info.setAddTime(new Date());
        return info;
    }

}
