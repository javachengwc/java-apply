package com.shop.activity.service.impl;

import com.shop.activity.dao.mapper.UserDailyMapper;
import com.shop.activity.model.UserDailyInfo;
import com.shop.activity.model.pojo.UserDaily;
import com.shop.activity.model.pojo.UserDailyExample;
import com.shop.activity.service.UserDailyService;
import com.util.date.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserDailyServiceImpl implements UserDailyService{

    @Autowired
    private UserDailyMapper userDailyMapper;

    public UserDailyInfo queryUserDaily(Long userId,Date day) {
        UserDaily userDaily = getUserDaily(userId,day);
        if(userDaily==null) {
            return null;
        }
        UserDailyInfo userDailyInfo =new UserDailyInfo();
        BeanUtils.copyProperties(userDaily,userDailyInfo);

        String dayStr= DateUtil.formatDate(day,DateUtil.FMT_YMD);
        userDailyInfo.setDayDateStr(dayStr);

        return userDailyInfo;

    }

    public UserDaily getUserDaily(Long userId,Date day) {

        UserDailyExample example = new UserDailyExample();
        UserDailyExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andDayDateEqualTo(day);

        List<UserDaily> list = userDailyMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }
}
