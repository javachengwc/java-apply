package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.FundMonth;

/**
 * 基金月数据Mapper接口
 * 
 * @author gener
 * @date 2025-06-19
 */
public interface FundMonthMapper 
{
    /**
     * 查询基金月数据
     * 
     * @param id 基金月数据主键
     * @return 基金月数据
     */
    public FundMonth selectFundMonthById(Long id);

    /**
     * 查询基金月数据列表
     * 
     * @param fundMonth 基金月数据
     * @return 基金月数据集合
     */
    public List<FundMonth> selectFundMonthList(FundMonth fundMonth);

    /**
     * 新增基金月数据
     * 
     * @param fundMonth 基金月数据
     * @return 结果
     */
    public int insertFundMonth(FundMonth fundMonth);

    /**
     * 修改基金月数据
     * 
     * @param fundMonth 基金月数据
     * @return 结果
     */
    public int updateFundMonth(FundMonth fundMonth);

    /**
     * 删除基金月数据
     * 
     * @param id 基金月数据主键
     * @return 结果
     */
    public int deleteFundMonthById(Long id);

    /**
     * 批量删除基金月数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFundMonthByIds(Long[] ids);
}
