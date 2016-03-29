package com.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsoupDemo {

    private List<Map<String,Object>> analyzeHtml(String htmlContent, String keyword, String jobCode, String relation) {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        try {
            Document doc = Jsoup.parse(htmlContent);
            Elements tables = doc.getElementsByAttributeValue("class", "table table-text-center table-hover table-resume-list table-detail");
            Element table = null;
            for (Element table1 : tables) {
                if (table1.select("table:has(tbody)").size() != 0) {
                    table = table1;
                    break;
                }
            }
            Elements trs = table.children().select("tbody").get(0).children();
            for (int i = 0; i < trs.size(); i += 2) {
                Element tr = trs.get(i);
                //两行显示一个人的信息，第一行显示基本信息；
                //第二行显示一些具体的信息可以过滤是否专升本
                Elements tds = tr.children();
                String resumeName = "";
                String resumeUrl = "http://lpt.liepin.com";
                //获得简历的链接地址
                resumeUrl = resumeUrl + tr.select("a").first().attr("href");
                //基数行，拼接简历名称
                for (int k = 2; k < tds.size(); k++) {
                    resumeName = resumeName + tds.get(k).text() + ",";
                }
                //偶数行，过滤是否专升本
                String xlms = trs.get(i + 1).select("td>table>tbody>tr>td").first().text();
                if (xlms.indexOf("专科") == -1 && xlms.indexOf("成考") == -1 && xlms.indexOf("大专") == -1 && xlms.indexOf("自考") == -1) {
                    //不是专升本
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("jobCode",jobCode);
                    map.put("keyword",keyword);
                    map.put("relation",relation);
                    map.put("resumeName",resumeName.substring(0, resumeName.lastIndexOf(",")));
                    map.put("resumeUrl",resumeUrl);

                    list.add(map);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    public void main(String args[])
    {

    }
}
