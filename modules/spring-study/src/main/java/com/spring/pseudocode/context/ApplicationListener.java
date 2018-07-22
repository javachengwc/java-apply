package com.spring.pseudocode.context;

import org.springframework.context.ApplicationEvent;

import java.util.EventListener;

public interface ApplicationListener <E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E e);
}
