package com.spring.pseudocode.webmvc;

import com.spring.pseudocode.context.ui.ModelMap;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public class ModelAndView {

    private Object view;

    private ModelMap model;

    private boolean cleared = false;

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public ModelMap getModel() {
        return model;
    }

    public void setModel(ModelMap model) {
        this.model = model;
    }


    public void setViewName(String viewName)
    {
        this.view = viewName;
    }

    public String getViewName()
    {
        return (this.view instanceof String) ? (String)this.view : null;
    }


    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    public boolean hasView()
    {
        return this.view != null;
    }

    public boolean wasCleared()
    {
        return (this.cleared) && (isEmpty());
    }

    public boolean isEmpty()
    {
        return (this.view == null) && (CollectionUtils.isEmpty(this.model));
    }

    public boolean isReference()
    {
        return this.view instanceof String;
    }

    protected Map<String, Object> getModelInternal()
    {
        return this.model;
    }

}
