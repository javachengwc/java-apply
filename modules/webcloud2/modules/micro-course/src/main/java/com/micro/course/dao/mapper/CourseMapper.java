package com.micro.course.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.course.model.pojo.Course;
import com.micro.course.model.pojo.CourseExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface CourseMapper  extends BaseMapper<Course> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    int countByExample(CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    int deleteByExample(CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    @Delete({
        "delete from course",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    @Insert({
        "insert into course (course_item_id, course_name, ",
        "teacher_id, teacher_name, ",
        "statu, begin_time, ",
        "end_time, create_time, ",
        "modified_time)",
        "values (#{courseItemId,jdbcType=BIGINT}, #{courseName,jdbcType=VARCHAR}, ",
        "#{teacherId,jdbcType=BIGINT}, #{teacherName,jdbcType=VARCHAR}, ",
        "#{statu,jdbcType=INTEGER}, #{beginTime,jdbcType=TIMESTAMP}, ",
        "#{endTime,jdbcType=TIMESTAMP}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{modifiedTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Course record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    int insertSelective(Course record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    List<Course> selectByExample(CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    @Select({
        "select",
        "id, course_item_id, course_name, teacher_id, teacher_name, statu, begin_time, ",
        "end_time, create_time, modified_time",
        "from course",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    Course selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    int updateByExampleSelective(@Param("record") Course record, @Param("example") CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    int updateByExample(@Param("record") Course record, @Param("example") CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    int updateByPrimaryKeySelective(Course record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table course
     *
     * @mbggenerated Thu May 23 10:27:19 CST 2019
     */
    @Update({
        "update course",
        "set course_item_id = #{courseItemId,jdbcType=BIGINT},",
          "course_name = #{courseName,jdbcType=VARCHAR},",
          "teacher_id = #{teacherId,jdbcType=BIGINT},",
          "teacher_name = #{teacherName,jdbcType=VARCHAR},",
          "statu = #{statu,jdbcType=INTEGER},",
          "begin_time = #{beginTime,jdbcType=TIMESTAMP},",
          "end_time = #{endTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "modified_time = #{modifiedTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Course record);
}