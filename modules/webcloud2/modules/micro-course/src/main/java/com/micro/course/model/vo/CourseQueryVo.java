package com.micro.course.model.vo;

import com.model.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "课程查询条件", description = "课程查询条件")
public class CourseQueryVo extends PageParam {

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("老师名称")
    private String teacherName;

    @ApiModelProperty("课程状态 -1--全部 0--正常 1--停课 2--结束")
    private Integer statu;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }
}
