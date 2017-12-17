package com.shop.activity.service;

import com.shop.activity.model.UserDailyInfo;

import java.util.Date;

public interface UserDailyService {

    public UserDailyInfo queryUserDaily(Long userId,Date day);
}
