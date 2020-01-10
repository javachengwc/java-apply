package com.util.http;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class QueryParamUtil {

  public static Map<String,String> getParamMap(String queryStr) {
    Map<String,String> map = new HashMap<String,String>();
    if(StringUtils.isBlank(queryStr)) {
      return map;
    }
    String pers [] = queryStr.split("\\&");
    for(String p:pers) {
      if(StringUtils.isBlank(p))
      {
        continue;
      }
      String as [] = p.split("=");
      int len = as.length;
      String val=(len>1)?as[1]:"";
      map.put(as[0], val);
    }

    return map;
  }

  public static String getParam(String queryStr,String param) {
    Map<String,String> map = getParamMap(queryStr);
    if(map==null)
    {
      return null;
    }
    return map.get(param);
  }

}
