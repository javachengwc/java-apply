package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.Fund;

/**
 * 基金Mapper接口
 * 
 * @author gener
 * @date 2025-06-18
 */
public interface FundMapper 
{
    /**
     * 查询基金
     * 
     * @param id 基金主键
     * @return 基金
     */
    public Fund selectFundById(Long id);

    /**
     * 查询基金列表
     * 
     * @param fund 基金
     * @return 基金集合
     */
    public List<Fund> selectFundList(Fund fund);

    /**
     * 新增基金
     * 
     * @param fund 基金
     * @return 结果
     */
    public int insertFund(Fund fund);

    /**
     * 修改基金
     * 
     * @param fund 基金
     * @return 结果
     */
    public int updateFund(Fund fund);

    /**
     * 删除基金
     * 
     * @param id 基金主键
     * @return 结果
     */
    public int deleteFundById(Long id);

    /**
     * 批量删除基金
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFundByIds(Long[] ids);
}
