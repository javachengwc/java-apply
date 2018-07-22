package com.spring.pseudocode.web.context;

import com.spring.pseudocode.context.ApplicationContext;

import javax.servlet.ServletContext;

public abstract interface WebApplicationContext extends ApplicationContext {

    public abstract ServletContext getServletContext();
}
