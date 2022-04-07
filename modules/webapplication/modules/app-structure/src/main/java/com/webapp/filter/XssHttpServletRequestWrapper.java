package com.webapp.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {  
    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
    public String[] getParameterValues(String parameter) {
      String[] values = super.getParameterValues(parameter);
      if (values==null)  {
                  return null;
          }
      int count = values.length;
      String[] encodedValues = new String[count];
      for (int i = 0; i < count; i++) {
                 encodedValues[i] = cleanXSS(values[i]);
       }
      return encodedValues;
    }
    public String getParameter(String parameter) {
          String value = super.getParameter(parameter);
          if (value == null) {
                 return null;
                  }
          return cleanXSS(value);
    }
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return cleanXSS(value);
    }
    private String cleanXSS(String value){
        //对特殊字符进行转义
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("((?i)SELECT.*FROM|UPDATE.*SET|DELETE.*FROM|UNION.*SELECT|SLEEP\\s*\\()", "");
        value = value.replaceAll("((?i)DROP\\s*TABLE|DROP\\s*DATABASE|CREATE\\s*TABLE|CREATE\\s*DATABASE|TRUNCATE|ALERT\\s*TABLE)", "");
        value = value.replaceAll("((?i)ALERT\\s*DATABASE|SHOW\\s*TABLE|SHOW\\s*DATABASE|INSERT.*INTO|REPLACE.*INTO)", "");
        
        return value;
    }

} 