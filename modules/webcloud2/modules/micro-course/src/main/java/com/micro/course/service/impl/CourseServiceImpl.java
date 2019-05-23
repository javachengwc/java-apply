package com.micro.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.course.dao.mapper.CourseMapper;
import com.micro.course.model.pojo.Course;
import com.micro.course.model.vo.CourseQueryVo;
import com.micro.course.model.vo.CourseVo;
import com.micro.course.service.CourseService;
import com.shop.base.model.Page;
import com.shop.base.util.TransUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    public Page<CourseVo> queryPage(CourseQueryVo queryVo) {
        Course courseCdn = new Course();
        courseCdn.setStatu(queryVo.getStatu());
        courseCdn.setCourseName(queryVo.getCourseName());
        courseCdn.setTeacherName(queryVo.getTeacherName());

        IPage plusPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page(queryVo.getPageNum(),queryVo.getPageSize());
        IPage<Course> resultPage =this.page(plusPage, Wrappers.query(courseCdn));

        Page<CourseVo> rtPage = new Page<CourseVo>();
        rtPage.setTotalCount(new Long(resultPage.getTotal()).intValue());
        List<CourseVo> list = TransUtil.transList(resultPage.getRecords(),CourseVo.class);
        rtPage.setList(list);
        return rtPage;
    }
}
