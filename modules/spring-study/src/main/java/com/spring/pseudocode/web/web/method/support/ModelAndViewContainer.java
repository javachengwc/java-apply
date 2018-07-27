package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.context.ui.ModelMap;

public class ModelAndViewContainer {

    private boolean ignoreDefaultModelOnRedirect = false;

    private boolean requestHandled = false;

    private Object view;

    private ModelMap redirectModel;

    public void setViewName(String viewName)
    {
        this.view = viewName;
    }

    public String getViewName()
    {
        return (this.view instanceof String) ? (String)this.view : null;
    }

    public void setView(Object view)
    {
        this.view = view;
    }

    public Object getView()
    {
        return this.view;
    }

    public boolean isViewReference()
    {
        return this.view instanceof String;
    }

    public ModelMap getModel()
    {
        if (this.redirectModel == null) {
            this.redirectModel = new ModelMap();
        }
        return this.redirectModel;
    }

    public boolean isRequestHandled() {
        return requestHandled;
    }

    public boolean containsAttribute(String name)
    {
        return getModel().containsAttribute(name);
    }

    public ModelAndViewContainer addAttribute(String name, Object value)
    {
        getModel().addAttribute(name, value);
        return this;
    }
}
