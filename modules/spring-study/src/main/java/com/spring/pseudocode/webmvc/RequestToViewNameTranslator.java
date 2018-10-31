package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;

//ViewResolver根据ViewName寻找view，但是有的Handler并没有设置view，也没有设置viewName，
//这时就需要RequestToViewNameTranslator的作用是从request中获取viewName。
public interface RequestToViewNameTranslator {

    String getViewName(HttpServletRequest request) throws Exception;

}
