package com.socketio.util;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext; // Spring应用上下文环境


  public void setApplicationContext(final ApplicationContext applicationContext) {
    SpringContextUtil.applicationContext = applicationContext;
  }


  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }


  public static Object getBean(final String name) {
    if (applicationContext == null) {
      return null;
    }
    return applicationContext.getBean(name);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(final String name, final Class<T> requiredType) {
    if (applicationContext == null) {
      return null;
    }
    return (T) applicationContext.getBean(name, requiredType);
  }

  public static <T> T  getBean(final Class<T> requiredType) {
    if (applicationContext == null) {
      return null;
    }
    return applicationContext.getBean(requiredType);
  }
}