package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.FundMonthMapper;
import com.manage.stock.domain.FundMonth;
import com.manage.stock.service.IFundMonthService;

/**
 * 基金月数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-19
 */
@Service
public class FundMonthServiceImpl implements IFundMonthService 
{
    @Autowired
    private FundMonthMapper fundMonthMapper;

    /**
     * 查询基金月数据
     * 
     * @param id 基金月数据主键
     * @return 基金月数据
     */
    @Override
    public FundMonth selectFundMonthById(Long id)
    {
        return fundMonthMapper.selectFundMonthById(id);
    }

    /**
     * 查询基金月数据列表
     * 
     * @param fundMonth 基金月数据
     * @return 基金月数据
     */
    @Override
    public List<FundMonth> selectFundMonthList(FundMonth fundMonth)
    {
        return fundMonthMapper.selectFundMonthList(fundMonth);
    }

    /**
     * 新增基金月数据
     * 
     * @param fundMonth 基金月数据
     * @return 结果
     */
    @Override
    public int insertFundMonth(FundMonth fundMonth)
    {
        fundMonth.setCreateTime(DateUtils.getNowDate());
        return fundMonthMapper.insertFundMonth(fundMonth);
    }

    /**
     * 修改基金月数据
     * 
     * @param fundMonth 基金月数据
     * @return 结果
     */
    @Override
    public int updateFundMonth(FundMonth fundMonth)
    {
        return fundMonthMapper.updateFundMonth(fundMonth);
    }

    /**
     * 批量删除基金月数据
     * 
     * @param ids 需要删除的基金月数据主键
     * @return 结果
     */
    @Override
    public int deleteFundMonthByIds(Long[] ids)
    {
        return fundMonthMapper.deleteFundMonthByIds(ids);
    }

    /**
     * 删除基金月数据信息
     * 
     * @param id 基金月数据主键
     * @return 结果
     */
    @Override
    public int deleteFundMonthById(Long id)
    {
        return fundMonthMapper.deleteFundMonthById(id);
    }
}
