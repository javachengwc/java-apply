package com.manage.service;

import javax.naming.NamingException;
import org.objectweb.jotm.Current;
import org.objectweb.jotm.Jotm;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

public class JotmFactoryBean implements FactoryBean, DisposableBean
{
  private Current jotmCurrent;
  private Jotm jotm;

  public JotmFactoryBean()
    throws NamingException
  {
    this.jotmCurrent = Current.getCurrent();

    if (this.jotmCurrent == null)
    {
      this.jotm = new Jotm(true, false);
      this.jotmCurrent = Current.getCurrent();
    }
  }

  public void setDefaultTimeout(int defaultTimeout)
  {
    this.jotmCurrent.setDefaultTimeout(defaultTimeout);
  }

  public Jotm getJotm()
  {
    return this.jotm;
  }

  public Object getObject()
  {
    return this.jotmCurrent;
  }

  public Class getObjectType() {
    return this.jotmCurrent.getClass();
  }

  public boolean isSingleton() {
    return true;
  }

  public void destroy()
  {
    if (this.jotm != null)
      this.jotm.stop();
  }
}