package com.micro.webcore.matcher;

import com.micro.webcore.util.PathUtil;
import com.micro.webcore.util.WebUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathMatchProcesser {

  private static final Logger logger = LoggerFactory.getLogger(PathMatchProcesser.class);

  //匹配复合器
  protected PatternMatcherComposite patternMatcherComposite = new PatternMatcherComposite();

  //需要登录的path集合
  protected List<String> appliedPaths = new ArrayList<String>();

  public void init() {

  }

  public PathMatchProcesser configPath(String config) {
    if(config==null || "".equals(config)) {
      return this;
    }
    String[] values = PathUtil.split(config);
    if (values!=null && values.length>0) {
      for(String per:values) {
        this.appliedPaths.add(per);
      }
    }
    return this;
  }

  private String getPathWithinApplication(ServletRequest request) {
    HttpServletRequest httpRequest = (HttpServletRequest )request;
    String contextPath = WebUtil.getContextPath(httpRequest);
    String requestUri = WebUtil.getRequestUri(httpRequest);
    if (StringUtils.startsWithIgnoreCase(requestUri, contextPath)) {
      String path = requestUri.substring(contextPath.length());
      return (PathUtil.hasText(path) ? path : "/");
    } else {
      return requestUri;
    }
  }

  private boolean pathMatch(String pattern, String path) {
    return patternMatcherComposite.matches(pattern, path);
  }

  //匹配
  public boolean requestMatch(HttpServletRequest request) {

    if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
        return false;
    }
    String requestURI = getPathWithinApplication(request);
    for (String pattern : this.appliedPaths) {
      if(StringUtils.isBlank(pattern)) {
        continue;
      }
      if (this.pathMatch(pattern, requestURI)) {
        return  true;
      }
    }
    return false;
  }
}
