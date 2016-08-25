package com.mountain.core;

import com.mountain.model.SpecUrl;

public interface Invoker {

    public SpecUrl getUrl();

    public Object getProvider();
}
