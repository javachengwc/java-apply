package com.shop.book.manage.user.service;

import com.shop.book.manage.user.model.ShopManageUser;

public interface ShopManageUserService {

    public ShopManageUser getLoginUser();

    public ShopManageUser getLoginUser(String token);

    public boolean checkUserLogin();

    public boolean checkUserLogin(String token);

    public ShopManageUser queryById(Long id);
}
