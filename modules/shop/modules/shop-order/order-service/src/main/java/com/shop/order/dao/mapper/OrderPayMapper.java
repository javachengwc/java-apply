package com.shop.order.dao.mapper;

import com.shop.order.model.pojo.OrderPay;
import com.shop.order.model.pojo.OrderPayExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface OrderPayMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    int countByExample(OrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    int deleteByExample(OrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    @Delete({
        "delete from order_pay",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    @Insert({
        "insert into order_pay (order_no, pay_channel, ",
        "amount, result, pay_time, ",
        "pay_account, prepay_id, ",
        "trade_no, create_time, ",
        "modified_time)",
        "values (#{orderNo,jdbcType=VARCHAR}, #{payChannel,jdbcType=INTEGER}, ",
        "#{amount,jdbcType=BIGINT}, #{result,jdbcType=INTEGER}, #{payTime,jdbcType=TIMESTAMP}, ",
        "#{payAccount,jdbcType=VARCHAR}, #{prepayId,jdbcType=VARCHAR}, ",
        "#{tradeNo,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{modifiedTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(OrderPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    int insertSelective(OrderPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    List<OrderPay> selectByExample(OrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    @Select({
        "select",
        "id, order_no, pay_channel, amount, result, pay_time, pay_account, prepay_id, ",
        "trade_no, create_time, modified_time",
        "from order_pay",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    OrderPay selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    int updateByExampleSelective(@Param("record") OrderPay record, @Param("example") OrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    int updateByExample(@Param("record") OrderPay record, @Param("example") OrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    int updateByPrimaryKeySelective(OrderPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_pay
     *
     * @mbggenerated Sun Dec 17 17:00:39 CST 2017
     */
    @Update({
        "update order_pay",
        "set order_no = #{orderNo,jdbcType=VARCHAR},",
          "pay_channel = #{payChannel,jdbcType=INTEGER},",
          "amount = #{amount,jdbcType=BIGINT},",
          "result = #{result,jdbcType=INTEGER},",
          "pay_time = #{payTime,jdbcType=TIMESTAMP},",
          "pay_account = #{payAccount,jdbcType=VARCHAR},",
          "prepay_id = #{prepayId,jdbcType=VARCHAR},",
          "trade_no = #{tradeNo,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "modified_time = #{modifiedTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(OrderPay record);
}