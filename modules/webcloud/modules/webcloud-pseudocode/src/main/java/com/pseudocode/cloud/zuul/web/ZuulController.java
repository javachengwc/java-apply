package com.pseudocode.cloud.zuul.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.http.ZuulServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;


public class ZuulController extends ServletWrappingController {

    public ZuulController() {
        setServletClass(ZuulServlet.class);
        setServletName("zuul");
        setSupportedMethods((String[]) null); // Allow all
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return super.handleRequestInternal(request, response);
        }
        finally {
            RequestContext.getCurrentContext().unset();
        }
    }

}
