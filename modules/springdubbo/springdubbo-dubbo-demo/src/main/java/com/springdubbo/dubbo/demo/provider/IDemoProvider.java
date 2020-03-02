package com.springdubbo.dubbo.demo.provider;

import com.model.base.Resp;

public interface IDemoProvider {

  public Resp<Integer> getDemo(Integer id);
}
