package com.spring.pseudocode.context;

import com.spring.pseudocode.beans.factory.Aware;

public abstract interface ApplicationContextAware extends Aware
{
    public abstract void setApplicationContext(ApplicationContext paramApplicationContext);
}
