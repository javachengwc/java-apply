package com.commonservice.invoke.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.system.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu> queryMenuTreeByUserId(Long userId);
}
