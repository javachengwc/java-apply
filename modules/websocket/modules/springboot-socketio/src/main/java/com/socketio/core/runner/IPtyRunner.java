package com.socketio.core.runner;

public interface IPtyRunner {

  public void init();

  public void destroy();

  public Long getUid();

  public String getSessionId();

  public boolean isFirstInfo();

  public void firstSended();

  public void setCanInput(boolean can);

  public void resize(String size);

  public boolean exec(String code);

  public boolean input(String param);
}
