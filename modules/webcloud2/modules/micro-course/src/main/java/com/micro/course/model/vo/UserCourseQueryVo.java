package com.micro.course.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户课程查询条件", description = "用户课程查询条件")
public class UserCourseQueryVo extends PageParam {

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("老师名称")
    private String teacherName;

    @ApiModelProperty("课程状态 -1--全部 0--正常 1--停课 2--结束")
    private Integer statu;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

}
