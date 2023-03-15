package com.druid.service;

import com.druid.model.pojo.Entity;

import java.util.List;

public interface EntityService {

    List<Entity> queryByName(String name);

    Entity addEntity(String name);
}
