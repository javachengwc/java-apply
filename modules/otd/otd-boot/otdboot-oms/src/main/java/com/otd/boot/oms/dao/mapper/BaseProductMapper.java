package com.otd.boot.oms.dao.mapper;

import com.otd.boot.oms.model.entity.BaseProduct;
import com.otd.boot.oms.model.entity.BaseProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface BaseProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    int countByExample(BaseProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    int deleteByExample(BaseProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    @Delete({
        "delete from base_product",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    @Insert({
        "insert into base_product (product_code, product_name, ",
        "physical_flag, gift_flag, ",
        "self_product_flag, volume, ",
        "weight, product_type, ",
        "need_scan, on_sale, ",
        "tax_code, tax_rate, ",
        "specifications, barcode_remark, ",
        "benchmark_price, code69, ",
        "create_time, create_by, ",
        "update_time, update_by)",
        "values (#{productCode,jdbcType=VARCHAR}, #{productName,jdbcType=VARCHAR}, ",
        "#{physicalFlag,jdbcType=INTEGER}, #{giftFlag,jdbcType=INTEGER}, ",
        "#{selfProductFlag,jdbcType=INTEGER}, #{volume,jdbcType=DECIMAL}, ",
        "#{weight,jdbcType=DECIMAL}, #{productType,jdbcType=VARCHAR}, ",
        "#{needScan,jdbcType=INTEGER}, #{onSale,jdbcType=INTEGER}, ",
        "#{taxCode,jdbcType=VARCHAR}, #{taxRate,jdbcType=DECIMAL}, ",
        "#{specifications,jdbcType=VARCHAR}, #{barcodeRemark,jdbcType=VARCHAR}, ",
        "#{benchmarkPrice,jdbcType=DECIMAL}, #{code69,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=VARCHAR}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{updateBy,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(BaseProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    int insertSelective(BaseProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    List<BaseProduct> selectByExample(BaseProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    @Select({
        "select",
        "id, product_code, product_name, physical_flag, gift_flag, self_product_flag, ",
        "volume, weight, product_type, need_scan, on_sale, tax_code, tax_rate, specifications, ",
        "barcode_remark, benchmark_price, code69, create_time, create_by, update_time, ",
        "update_by",
        "from base_product",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    BaseProduct selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    int updateByExampleSelective(@Param("record") BaseProduct record, @Param("example") BaseProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    int updateByExample(@Param("record") BaseProduct record, @Param("example") BaseProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    int updateByPrimaryKeySelective(BaseProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table base_product
     *
     * @mbggenerated Tue Feb 20 17:09:01 CST 2024
     */
    @Update({
        "update base_product",
        "set product_code = #{productCode,jdbcType=VARCHAR},",
          "product_name = #{productName,jdbcType=VARCHAR},",
          "physical_flag = #{physicalFlag,jdbcType=INTEGER},",
          "gift_flag = #{giftFlag,jdbcType=INTEGER},",
          "self_product_flag = #{selfProductFlag,jdbcType=INTEGER},",
          "volume = #{volume,jdbcType=DECIMAL},",
          "weight = #{weight,jdbcType=DECIMAL},",
          "product_type = #{productType,jdbcType=VARCHAR},",
          "need_scan = #{needScan,jdbcType=INTEGER},",
          "on_sale = #{onSale,jdbcType=INTEGER},",
          "tax_code = #{taxCode,jdbcType=VARCHAR},",
          "tax_rate = #{taxRate,jdbcType=DECIMAL},",
          "specifications = #{specifications,jdbcType=VARCHAR},",
          "barcode_remark = #{barcodeRemark,jdbcType=VARCHAR},",
          "benchmark_price = #{benchmarkPrice,jdbcType=DECIMAL},",
          "code69 = #{code69,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "create_by = #{createBy,jdbcType=VARCHAR},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "update_by = #{updateBy,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(BaseProduct record);
}