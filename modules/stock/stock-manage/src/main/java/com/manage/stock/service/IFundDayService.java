package com.manage.stock.service;

import java.util.List;
import com.manage.stock.domain.FundDay;

/**
 * 基金天数据Service接口
 * 
 * @author gener
 * @date 2025-06-19
 */
public interface IFundDayService 
{
    /**
     * 查询基金天数据
     * 
     * @param id 基金天数据主键
     * @return 基金天数据
     */
    public FundDay selectFundDayById(Long id);

    /**
     * 查询基金天数据列表
     * 
     * @param fundDay 基金天数据
     * @return 基金天数据集合
     */
    public List<FundDay> selectFundDayList(FundDay fundDay);

    /**
     * 新增基金天数据
     * 
     * @param fundDay 基金天数据
     * @return 结果
     */
    public int insertFundDay(FundDay fundDay);

    /**
     * 修改基金天数据
     * 
     * @param fundDay 基金天数据
     * @return 结果
     */
    public int updateFundDay(FundDay fundDay);

    /**
     * 批量删除基金天数据
     * 
     * @param ids 需要删除的基金天数据主键集合
     * @return 结果
     */
    public int deleteFundDayByIds(Long[] ids);

    /**
     * 删除基金天数据信息
     * 
     * @param id 基金天数据主键
     * @return 结果
     */
    public int deleteFundDayById(Long id);
}
