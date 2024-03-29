package com.otd.boot.tms.dao.mapper;

import com.otd.boot.tms.model.entity.TmsDeliveryPlan;
import com.otd.boot.tms.model.entity.TmsDeliveryPlanExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TmsDeliveryPlanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    int countByExample(TmsDeliveryPlanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    int deleteByExample(TmsDeliveryPlanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    @Delete({
        "delete from tms_delivery_plan",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    @Insert({
        "insert into tms_delivery_plan (id, delivery_no, ",
        "delivery_batch_number, warehouse_code, ",
        "dest_province, dest_city, ",
        "dest_county, route_id, ",
        "logistics_provider_code, logistics_type, ",
        "pick_mode, tran_type, ",
        "print_flag, print_time, ",
        "turn_wms_flag, turn_wms_time, ",
        "create_time, create_by, ",
        "update_time, update_by)",
        "values (#{id,jdbcType=VARCHAR}, #{deliveryNo,jdbcType=VARCHAR}, ",
        "#{deliveryBatchNumber,jdbcType=VARCHAR}, #{warehouseCode,jdbcType=VARCHAR}, ",
        "#{destProvince,jdbcType=VARCHAR}, #{destCity,jdbcType=VARCHAR}, ",
        "#{destCounty,jdbcType=VARCHAR}, #{routeId,jdbcType=VARCHAR}, ",
        "#{logisticsProviderCode,jdbcType=VARCHAR}, #{logisticsType,jdbcType=INTEGER}, ",
        "#{pickMode,jdbcType=INTEGER}, #{tranType,jdbcType=INTEGER}, ",
        "#{printFlag,jdbcType=INTEGER}, #{printTime,jdbcType=TIMESTAMP}, ",
        "#{turnWmsFlag,jdbcType=INTEGER}, #{turnWmsTime,jdbcType=TIMESTAMP}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=VARCHAR}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{updateBy,jdbcType=VARCHAR})"
    })
    int insert(TmsDeliveryPlan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    int insertSelective(TmsDeliveryPlan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    List<TmsDeliveryPlan> selectByExample(TmsDeliveryPlanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    @Select({
        "select",
        "id, delivery_no, delivery_batch_number, warehouse_code, dest_province, dest_city, ",
        "dest_county, route_id, logistics_provider_code, logistics_type, pick_mode, tran_type, ",
        "print_flag, print_time, turn_wms_flag, turn_wms_time, create_time, create_by, ",
        "update_time, update_by",
        "from tms_delivery_plan",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @ResultMap("BaseResultMap")
    TmsDeliveryPlan selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    int updateByExampleSelective(@Param("record") TmsDeliveryPlan record, @Param("example") TmsDeliveryPlanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    int updateByExample(@Param("record") TmsDeliveryPlan record, @Param("example") TmsDeliveryPlanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    int updateByPrimaryKeySelective(TmsDeliveryPlan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tms_delivery_plan
     *
     * @mbggenerated Mon Jan 22 19:30:03 CST 2024
     */
    @Update({
        "update tms_delivery_plan",
        "set delivery_no = #{deliveryNo,jdbcType=VARCHAR},",
          "delivery_batch_number = #{deliveryBatchNumber,jdbcType=VARCHAR},",
          "warehouse_code = #{warehouseCode,jdbcType=VARCHAR},",
          "dest_province = #{destProvince,jdbcType=VARCHAR},",
          "dest_city = #{destCity,jdbcType=VARCHAR},",
          "dest_county = #{destCounty,jdbcType=VARCHAR},",
          "route_id = #{routeId,jdbcType=VARCHAR},",
          "logistics_provider_code = #{logisticsProviderCode,jdbcType=VARCHAR},",
          "logistics_type = #{logisticsType,jdbcType=INTEGER},",
          "pick_mode = #{pickMode,jdbcType=INTEGER},",
          "tran_type = #{tranType,jdbcType=INTEGER},",
          "print_flag = #{printFlag,jdbcType=INTEGER},",
          "print_time = #{printTime,jdbcType=TIMESTAMP},",
          "turn_wms_flag = #{turnWmsFlag,jdbcType=INTEGER},",
          "turn_wms_time = #{turnWmsTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "create_by = #{createBy,jdbcType=VARCHAR},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "update_by = #{updateBy,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(TmsDeliveryPlan record);
}