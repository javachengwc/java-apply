package com.micro.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.course.model.pojo.UserCourse;
import com.micro.course.model.vo.UserCourseQueryVo;
import com.micro.course.model.vo.UserCourseVo;
import com.shop.base.model.Page;

public interface UserCourseService extends IService<UserCourse> {

    public Page<UserCourseVo> queryPage(UserCourseQueryVo queryVo);
}