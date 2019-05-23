package com.micro.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.course.model.pojo.Course;
import com.micro.course.model.vo.CourseQueryVo;
import com.micro.course.model.vo.CourseVo;
import com.shop.base.model.Page;

public interface CourseService extends IService<Course> {

    public Page<CourseVo> queryPage(CourseQueryVo queryVo);
}
