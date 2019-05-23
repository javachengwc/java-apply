package com.micro.course.dao;

import com.micro.course.model.vo.UserCourseQueryVo;
import com.micro.course.model.vo.UserCourseVo;

import java.util.List;

public interface UserCourseDao {

    public List<UserCourseVo> queryPage(UserCourseQueryVo queryVo);
}
