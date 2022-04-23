package com.manageplat.model.mapper;

import com.manageplat.model.pojo.UserDistributeReport;
import com.manageplat.model.pojo.UserDistributeReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface UserDistributeReportMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    int countByExample(UserDistributeReportExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    int deleteByExample(UserDistributeReportExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    @Delete({
        "delete from user_distribute_report",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    @Insert({
        "insert into user_distribute_report (creator_id, creator_name, ",
        "create_date, data_path, ",
        "condit, note)",
        "values (#{creatorId,jdbcType=INTEGER}, #{creatorName,jdbcType=VARCHAR}, ",
        "#{createDate,jdbcType=TIMESTAMP}, #{dataPath,jdbcType=VARCHAR}, ",
        "#{condit,jdbcType=VARCHAR}, #{note,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(UserDistributeReport record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    int insertSelective(UserDistributeReport record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    List<UserDistributeReport> selectByExample(UserDistributeReportExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    @Select({
        "select",
        "id, creator_id, creator_name, create_date, data_path, condit, note",
        "from user_distribute_report",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    UserDistributeReport selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    int updateByExampleSelective(@Param("record") UserDistributeReport record, @Param("example") UserDistributeReportExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    int updateByExample(@Param("record") UserDistributeReport record, @Param("example") UserDistributeReportExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    int updateByPrimaryKeySelective(UserDistributeReport record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_distribute_report
     *
     * @mbggenerated Mon Jul 13 18:07:24 CST 2015
     */
    @Update({
        "update user_distribute_report",
        "set creator_id = #{creatorId,jdbcType=INTEGER},",
          "creator_name = #{creatorName,jdbcType=VARCHAR},",
          "create_date = #{createDate,jdbcType=TIMESTAMP},",
          "data_path = #{dataPath,jdbcType=VARCHAR},",
          "condit = #{condit,jdbcType=VARCHAR},",
          "note = #{note,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(UserDistributeReport record);
}