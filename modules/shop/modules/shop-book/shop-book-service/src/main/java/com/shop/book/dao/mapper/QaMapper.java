package com.shop.book.dao.mapper;

import com.shop.book.model.pojo.Qa;
import com.shop.book.model.pojo.QaExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface QaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    int countByExample(QaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    int deleteByExample(QaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    @Delete({
        "delete from qa",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    @Insert({
        "insert into qa (question, type, ",
        "answer, create_time, ",
        "is_show, sort, modified_time)",
        "values (#{question,jdbcType=VARCHAR}, #{type,jdbcType=INTEGER}, ",
        "#{answer,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{isShow,jdbcType=INTEGER}, #{sort,jdbcType=INTEGER}, #{modifiedTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Qa record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    int insertSelective(Qa record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    List<Qa> selectByExample(QaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    @Select({
        "select",
        "id, question, type, answer, create_time, is_show, sort, modified_time",
        "from qa",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    Qa selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    int updateByExampleSelective(@Param("record") Qa record, @Param("example") QaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    int updateByExample(@Param("record") Qa record, @Param("example") QaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    int updateByPrimaryKeySelective(Qa record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qa
     *
     * @mbggenerated Fri Jun 29 11:02:09 CST 2018
     */
    @Update({
        "update qa",
        "set question = #{question,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=INTEGER},",
          "answer = #{answer,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "is_show = #{isShow,jdbcType=INTEGER},",
          "sort = #{sort,jdbcType=INTEGER},",
          "modified_time = #{modifiedTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Qa record);
}