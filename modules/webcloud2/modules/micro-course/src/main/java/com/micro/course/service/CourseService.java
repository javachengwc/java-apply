package com.micro.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.course.model.pojo.Course;
import com.micro.course.model.vo.CourseQueryVo;
import com.micro.course.model.vo.CourseVo;
import com.model.base.PageVo;

public interface CourseService extends IService<Course> {

    public PageVo<CourseVo> queryPage(CourseQueryVo queryVo);
}
