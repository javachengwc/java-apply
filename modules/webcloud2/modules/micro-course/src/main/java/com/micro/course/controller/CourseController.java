package com.micro.course.controller;

import com.micro.course.model.pojo.Course;
import com.micro.course.model.vo.CourseVo;
import com.micro.course.service.CourseService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api("课程接口")
@RequestMapping("/course")
@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "根据id查询课程", notes = "根据id查询课程")
    @RequestMapping(value = "/queryCourseById", method = RequestMethod.POST)
    public Resp<CourseVo> queryCourseById(@RequestBody Req<Long> req) {
        Resp<CourseVo> resp = new Resp<CourseVo>();
        Long courseId = req.getData();
        if(courseId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        Course course = courseService.getById(courseId);
        CourseVo courseVo = TransUtil.transEntity(course,CourseVo.class);
        resp.setData(courseVo);
        return resp;
    }
}
