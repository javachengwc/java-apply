package com.shop.book.manage.service;

import com.shop.book.manage.model.UserDailyInfo;

import java.util.Date;

public interface UserDailyService {

    public UserDailyInfo queryUserDaily(Long userId,Date day);
}
