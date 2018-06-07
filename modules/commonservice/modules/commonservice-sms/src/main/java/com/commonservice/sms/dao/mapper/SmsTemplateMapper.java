package com.commonservice.sms.dao.mapper;

import com.commonservice.sms.model.pojo.SmsTemplate;
import com.commonservice.sms.model.pojo.SmsTemplateExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface SmsTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    int countByExample(SmsTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    int deleteByExample(SmsTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    @Delete({
        "delete from sms_template",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    @Insert({
        "insert into sms_template (code, content, ",
        "type, channel, create_time, ",
        "modified_time)",
        "values (#{code,jdbcType=VARCHAR}, #{content,jdbcType=VARCHAR}, ",
        "#{type,jdbcType=INTEGER}, #{channel,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{modifiedTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(SmsTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    int insertSelective(SmsTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    List<SmsTemplate> selectByExample(SmsTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    @Select({
        "select",
        "id, code, content, type, channel, create_time, modified_time",
        "from sms_template",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    SmsTemplate selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    int updateByExampleSelective(@Param("record") SmsTemplate record, @Param("example") SmsTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    int updateByExample(@Param("record") SmsTemplate record, @Param("example") SmsTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    int updateByPrimaryKeySelective(SmsTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sms_template
     *
     * @mbggenerated Fri Jun 01 10:37:01 CST 2018
     */
    @Update({
        "update sms_template",
        "set code = #{code,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=INTEGER},",
          "channel = #{channel,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "modified_time = #{modifiedTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(SmsTemplate record);
}