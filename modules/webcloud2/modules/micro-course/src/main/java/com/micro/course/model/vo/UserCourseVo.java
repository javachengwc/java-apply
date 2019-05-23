package com.micro.course.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "用户课程信息", value = "userCourseVo")
public class UserCourseVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(name = "userId", value = "用户id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("科目id")
    private Long courseItemId;

    @ApiModelProperty("老师名称")
    private String teacherName;

    @ApiModelProperty("课程状态 0--正常 1--停课 2--结束")
    private Integer statu;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
