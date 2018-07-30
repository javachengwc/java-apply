package com.spring.pseudocode.webmvc.mvc.method;

import com.spring.pseudocode.webmvc.handle.AbstractHandlerMethodMapping;
import com.spring.pseudocode.webmvc.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public abstract class RequestMappingInfoHandlerMapping extends AbstractHandlerMethodMapping<RequestMappingInfo>
{

    protected void handleMatch(RequestMappingInfo info, String lookupPath, HttpServletRequest request)
    {
        //super.handleMatch(info, lookupPath, request);

        //Set patterns = info.getPatternsCondition().getPatterns();
        Set patterns =null;
        Map decodedUriVariables;
        String bestPattern;
        Map uriVariables;
        bestPattern = (String)patterns.iterator().next();
        uriVariables = getPathMatcher().extractUriTemplateVariables(bestPattern, lookupPath);
        decodedUriVariables = getUrlPathHelper().decodePathVariables(request, uriVariables);

    }

}
