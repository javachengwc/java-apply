package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.FundWeek;

/**
 * 基金周数据Mapper接口
 * 
 * @author gener
 * @date 2025-06-19
 */
public interface FundWeekMapper 
{
    /**
     * 查询基金周数据
     * 
     * @param id 基金周数据主键
     * @return 基金周数据
     */
    public FundWeek selectFundWeekById(Long id);

    /**
     * 查询基金周数据列表
     * 
     * @param fundWeek 基金周数据
     * @return 基金周数据集合
     */
    public List<FundWeek> selectFundWeekList(FundWeek fundWeek);

    /**
     * 新增基金周数据
     * 
     * @param fundWeek 基金周数据
     * @return 结果
     */
    public int insertFundWeek(FundWeek fundWeek);

    /**
     * 修改基金周数据
     * 
     * @param fundWeek 基金周数据
     * @return 结果
     */
    public int updateFundWeek(FundWeek fundWeek);

    /**
     * 删除基金周数据
     * 
     * @param id 基金周数据主键
     * @return 结果
     */
    public int deleteFundWeekById(Long id);

    /**
     * 批量删除基金周数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFundWeekByIds(Long[] ids);
}
